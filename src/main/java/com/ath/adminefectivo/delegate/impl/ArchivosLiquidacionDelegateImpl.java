package com.ath.adminefectivo.delegate.impl;


import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.IArchivosLiquidacionDelegate;
import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosLiquidacionListDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoListDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.BancoSimpleInfoEntity;
import com.ath.adminefectivo.entities.Transportadoras;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IBancoSimpleInfoRepository;
import com.ath.adminefectivo.repositories.ITransportadorasRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IValidacionArchivoService;
import java.text.ParseException;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;
import com.ath.adminefectivo.dto.compuestos.SummaryArchivoLiquidacionDTO;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class ArchivosLiquidacionDelegateImpl implements IArchivosLiquidacionDelegate {

	@Autowired
	IParametroService parametrosService;
	
    @Autowired
    IFilesService filesService;
	
    @Autowired
    IMaestroDefinicionArchivoService maestroDefinicionArchivoService;
    
	@Autowired
	IValidacionArchivoService validacionArchivoService;
	
	@Autowired
	ILecturaArchivoService lecturaArchivoService;
	
	@Autowired
	IArchivosCargadosService archivosCargadosService;
	
    @Autowired
    IBancoSimpleInfoRepository bancosRepository;

    @Autowired
    ITransportadorasRepository transportadorasRepository;
    
    private ValidacionArchivoDTO validacionArchivo;
    private ValidacionArchivoListDTO validacionArchivoList;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<ArchivosLiquidacionDTO> getAll(int start, int end, boolean content, String fileName) {

		// Se obtienen los Objetos necesarios que se utilizarán para la verificación de los campos extraídos del nombre del archivo
		List<BancoSimpleInfoEntity> bancos;
		List<Transportadoras> transportadoras;
		try{
			bancos = bancosRepository.findByEsAvalEqualsOne();
			transportadoras = transportadorasRepository.findAll();
		}
		catch (Exception e){
			throw new NegocioException(ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getCode(),
					ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getDescription(),
					ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getHttpStatus());
		}


		List<String> cadenaTrasportadoras = transportadoras.stream()
				.map(Transportadoras::getNombreTransportadora)
				.toList();
		List<String> cadenaEntidades = bancos.stream()
				.map(BancoSimpleInfoEntity::getAbreviatura)
				.toList();
		List<String> cadenaTipos = new ArrayList<>();

		// se buscan las deficiones en la bd y se extraen los datos, como extensión, agrupador, url
		List<MaestrosDefinicionArchivoDTO> maestrosDefinicion =
				maestroDefinicionArchivoService.consultarDefinicionArchivoByAgrupador(null, Constantes.LIQUIDACION_AGRUPADOR);

		String urlPendientes = filesService.consultarPathArchivos(Constantes.ESTADO_CARGUE_PENDIENTE);
		var url = maestrosDefinicion.get(0).getUbicacion().concat(urlPendientes);
		String requiredFileExtension = maestrosDefinicion.get(0).getExtension();

		// se hace el llamado a s3 para obtener un summary de los archivos del directorio
		List<ArchivosLiquidacionDTO> dtoResponseList =
				this.InicializarDtoList(filesService.obtenerContenidoCarpetaSummaryS3Object(url, start, end, content, fileName), url);

		if (dtoResponseList.isEmpty()) {
			return new PageImpl<>(dtoResponseList);
		}

		// Se obtienen todas las máscaras por maestro deficinion
		List<String> cadenaMascara = maestrosDefinicion.stream()
				.map(MaestrosDefinicionArchivoDTO::getMascaraArch)
				.toList();
		log.info("Archivos en directorio Pendientes: url:{} - cantidad:{}", url, dtoResponseList.size());

		//empiea lógica
		for (String mascara : cadenaMascara) {

			// Defino el patrón para encontrar el primer segmento entre corchetes
			Pattern pattern = Pattern.compile(Constantes.REGEX_EXTRAER_MASCARA);
			Matcher matcher = pattern.matcher(mascara);

			// Encunetro el primer segmento entre corchetes
			if (matcher.find()) {
				String primerSegmento = matcher.group(1);
				cadenaTipos.add(primerSegmento);
			}
		}

		String[][] estructuraMascara = segmentaCadena(cadenaMascara.get(0));

		List<ArchivosLiquidacionDTO> responseList = new ArrayList<>();

		for (ArchivosLiquidacionDTO dto : dtoResponseList) {

			String cadena = dto.getNombreArchivo();
			dto.setNombreArchivo(cadena.substring(0, cadena.lastIndexOf('.')));

			for (String[] keyValue : estructuraMascara) {

				String nombreCampo = keyValue[0];
				String valorCampo = keyValue[1];
				switch (nombreCampo) {
				case "TIPO":
					//validar si extensión es la misma que la que aparece en base de datos (txt)
					String[] nameAndExtension = getNameAndExtension(cadena, requiredFileExtension);
					//remover extensión de cadena
					cadena = nameAndExtension[0];
					try {
						// Validacion para el campo tipo
						String existeTipo = validaCadena(cadenaTipos, cadena);
						String idMaestroArchivo = (existeTipo.equals("LIQ_TRANSPORTE")) ? Constantes.MAESTRO_ARCHIVO_TRANSPORTE : Constantes.MAESTRO_ARCHIVO_PROCESAMIENTO;
						int indiceCaracter = existeTipo.indexOf('_');
						if (indiceCaracter != -1) {
							dto.setTipoArchivo(existeTipo.substring(indiceCaracter + 1));
							dto.setIdMaestroArchivo(idMaestroArchivo);                                
							cadena = cadena.replace(existeTipo, "");
						}
					} catch (Exception e) {
						throw new NegocioException(ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getCode(),
								ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getDescription(),
								ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getHttpStatus());
					}
					break;

				case "TRANSPORTADORA":
					// Validación para el campo TRANSPORTADORA
					String existeTransportadora = validaCadena(cadenaTrasportadoras, cadena);
					if (existeTransportadora != null) {
						dto.setTdv(existeTransportadora);
						cadena = cadena.replace(existeTransportadora, "");
					} else {
						throw new NegocioException(ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getCode(),
								ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getDescription(),
								ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getHttpStatus());
					}
					break;
				case "BANCO":
					// Validación para el campo BANCO
					String existeEntidad = validaCadena(cadenaEntidades, cadena);
					if (existeEntidad != null) {
						Optional<BancoSimpleInfoEntity> result = bancos.stream()
								.filter(banco -> existeEntidad.equals(banco.getAbreviatura()))
								.findFirst();
						dto.setBanco(result.get().getNombreBanco());
						cadena = cadena.replace(existeEntidad, "");
					} else {
						throw new NegocioException(ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getCode(),
								ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getDescription(),
								ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getHttpStatus());

					}
					break;

				case "FECHA":
					// Validación para el campo FECHA
					dto.setFechaArchivo(validaFecha(valorCampo, cadena));
					break;
				default:
					break;
				}

			}
			dto.setEstado(Constantes.ESTADO_PROCESO_PENDIENTE);
			responseList.add(dto);
		}

		responseList.sort(Comparator.comparing(ArchivosLiquidacionDTO::getFechaArchivo)
				.thenComparing(ArchivosLiquidacionDTO::getNombreArchivo));

		// Crea un contador
		AtomicLong counter = new AtomicLong(1);

		// Asigna el valor del contador a idArchivo para cada objeto en la lista
		responseList.forEach(dto -> dto.setIdArchivo(counter.getAndIncrement()));

		return new PageImpl<>(responseList);
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public boolean eliminarArchivo(Long idArchivo) {
		return false;
	}

	private String[] getNameAndExtension(String cadena, String requiredFileExtension) {

		try{
			String[] nameAndExtension = cadena.split("\\.");
			if (!nameAndExtension[1].equals(requiredFileExtension)) {
				throw new NegocioException(ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getCode(),
						ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getDescription(),
						ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getHttpStatus());
			}
			return nameAndExtension;
		}
		catch (Exception e){
			throw new NegocioException(ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getCode(),
					ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getDescription(),
					ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getHttpStatus());
		}

	}

	public String validaCadena(List<String> cadenaElementos, String archivo) {
		// Crear un patrón de las cadenas
		String patronRegex = String.join("|", cadenaElementos);
		Pattern patron = Pattern.compile(patronRegex);

		Matcher matcher = patron.matcher(archivo);
		String entidadEncontrada = "";
		// Verificar si la cadena de la lista existe
		while (matcher.find()) {
			entidadEncontrada = matcher.group();
			break;
		}

		if (!matcher.hitEnd()) {
			return entidadEncontrada;
		} else {
			return null;
		}

	}

	public String[][] segmentaCadena(String nombrArchivo) {
		String[] partes = nombrArchivo.substring(1, nombrArchivo.length() - 1).split("]\\[");
		String[][] resultado = new String[partes.length][2];

		// Nombres de campos
		String[] nombresCampos = {"TIPO", "TRANSPORTADORA", "BANCO", "FECHA"};

		for (int i = 0; i < partes.length; i++) {
			String valor = partes[i].trim();

			resultado[i][0] = nombresCampos[i];
			resultado[i][1] = valor;
		}

		return resultado;
	}
	
	private List<ArchivosLiquidacionDTO> InicializarDtoList (List<SummaryArchivoLiquidacionDTO> objetos, String url){

		List<ArchivosLiquidacionDTO> dtoResponseList = new ArrayList<>();

		for (SummaryArchivoLiquidacionDTO archivoDTO : objetos) {

			String nombreArchivo = obtenerNombreArchivo(archivoDTO.getS3ObjectSummary().getKey());

			ArchivosLiquidacionDTO archivosLiquidacionDTO = ArchivosLiquidacionDTO.builder()

					.url(url)
					.nombreArchivo(nombreArchivo)
					.contenidoArchivo(archivoDTO.getContenidoArchivo())
					.nombreArchivoCompleto(nombreArchivo)
					.fechaTransferencia(archivoDTO.getS3ObjectSummary().getLastModified())
					.build();

			dtoResponseList.add(archivosLiquidacionDTO);
		}

		return dtoResponseList;

	}

	private String obtenerNombreArchivo(String rutaCompleta) {
		// Obtén el nombre del archivo desde la ruta completa
		int index = rutaCompleta.lastIndexOf("/");
		return (index == -1) ? rutaCompleta : rutaCompleta.substring(index + 1);
	}

	private Date validaFecha(String formatoFecha, String cadenaFecha) {
		String dateFormat = formatoFecha.replace("AAAA", "yyyy").replace("DD", "dd");
		if (dateFormat.length() != cadenaFecha.length()) {
			throw new NegocioException(ApiResponseCode.ERROR_FECHA_NO_VALIDA.getCode(),
					ApiResponseCode.ERROR_FECHA_NO_VALIDA.getDescription(),
					ApiResponseCode.ERROR_FECHA_NO_VALIDA.getHttpStatus());
		} else {
			SimpleDateFormat formato = new SimpleDateFormat(dateFormat);
			formato.setLenient(false);
			try {
				return formato.parse(cadenaFecha);
			} catch (ParseException e) {
				throw new NegocioException(ApiResponseCode.ERROR_FECHA_NO_VALIDA.getCode(),
						ApiResponseCode.ERROR_FECHA_NO_VALIDA.getDescription(),
						ApiResponseCode.ERROR_FECHA_NO_VALIDA.getHttpStatus());
			}
		}

	}

	/**
	 * Metodo encargado de iterar todos los archivos enviados para procesar
	 * 
	 * @param procesarAchivos
	 * @param archivosProcesar
	 * @return ValidacionArchivoDTO
	 * @author hector.mercado
	 */
	@Override
	public Page<ArchivosLiquidacionDTO> procesarAchivos(ArchivosLiquidacionListDTO archivosProcesar) {
	
		 List<ArchivosLiquidacionDTO> responseList = new ArrayList<>();
		 
		 archivosProcesar.getValidacionArchivo().forEach(f->{
			
		 this.ProcesarAchivoCargado(f);
		 
		 f.setEstado(this.validacionArchivo.getEstadoValidacion());
		 f.setIdArchivodb(this.validacionArchivo.getIdArchivo());
			
			responseList.add(f);
			
		 });
		
		 return new PageImpl<>(responseList);
	}

	/**
	 * Metodo encargado de realizar la validaciones y procesamiento de un archivo cargado
	 * 
	 * @param ProcesarAchivoCargado
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return void
	 * @author hector.mercado
	 */
	@Override
	public void ProcesarAchivoCargado(ArchivosLiquidacionDTO archivoProcesar) {
		this.validacionArchivo = new ValidacionArchivoDTO();
		
		String idMaestroDefinicion = archivoProcesar.getIdMaestroArchivo();
		String nombreArchivo       = archivoProcesar.getNombreArchivoCompleto();

		//1. Obtener el tipo de archivo
		var maestroDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoById(idMaestroDefinicion);
		var urlPendinetes = parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_PENDIENTES);
		var url = maestroDefinicion.getUbicacion().concat(urlPendinetes).concat(nombreArchivo);
		
		//2. validar el nombre
		validacionArchivoService.validarNombreArchivo(maestroDefinicion, nombreArchivo);
		//3. descargar temporal
		var dowloadFile = filesService.downloadFile(DownloadDTO.builder().url(url).build());

		//4. obtener el contenido
		String delimitador = lecturaArchivoService.obtenerDelimitadorArchivo(maestroDefinicion);
		List<String[]> contenido = lecturaArchivoService.leerArchivo(dowloadFile.getFile(), delimitador, maestroDefinicion);
		Date fechaActual = parametrosService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);
		Date fechaArchivo = validacionArchivoService.obtenerFechaArchivo(nombreArchivo, maestroDefinicion.getMascaraArch());

		this.validacionArchivo = ValidacionArchivoDTO.builder().nombreArchivo(nombreArchivo)
				.descripcion(maestroDefinicion.getDescripcionArch())
				.fechaArchivo(fechaArchivo)
				.maestroDefinicion(maestroDefinicion)
				.url(url)
				.numeroRegistros(obtenerNumeroRegistros(maestroDefinicion, contenido.size())).build();

		//5. Validar Contenido
		if (this.validarCantidadRegistros(maestroDefinicion, this.validacionArchivo.getNumeroRegistros())) {
			this.validacionArchivo = validacionArchivoService.validar(maestroDefinicion, contenido, this.validacionArchivo);
			
			//Si estado de validacion es OK persistir la informacion
			//if (Objects.equals(this.validacionArchivo.getEstadoValidacion(), Dominios.ESTADO_VALIDACION_CORRECTO)) {
				boolean alcance = false;
				Long idArchivo = archivosCargadosService.persistirDetalleArchivoCargado(validacionArchivo, false, alcance);
				this.validacionArchivo.setIdArchivo(idArchivo);
				
				/*
				String urlDestino = (Objects.equals(this.validacionArchivo.getEstadoValidacion(),
						Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO))
								? parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_ERRADOS)
								: parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_PROCESADOS);

				this.filesService.moverArchivos(this.validacionArchivo.getUrl(),
						this.validacionArchivo.getMaestroDefinicion().getUbicacion().concat(urlDestino),
						this.validacionArchivo.getNombreArchivo(), idArchivo.toString());
				*/
			//}
			
		}
		
	}
    
	/**
	 * Obtiene la cantidad de registros, verificando si tiene cabecera y control
	 * final
	 * 
	 * @param maestroDefinicion
	 * @param contenido
	 * @return
	 * @return int
	 * @author hector.mercado
	 */
	private int obtenerNumeroRegistros(MaestrosDefinicionArchivoDTO maestroDefinicion, int cantidad) {
		if (maestroDefinicion.isCabecera())
			cantidad--;
		if (maestroDefinicion.isControlFinal())
			cantidad--;

		return cantidad;
	}
  
	/**
	 * Valida si que el valor del contenido sea mayor al minimo paramtrizado
	 * 
	 * @param maestroDefinicion
	 * @param contenido
	 * @return
	 * @return boolean
	 * @author CamiloBenavides
	 */
	private boolean validarCantidadRegistros(MaestrosDefinicionArchivoDTO maestroDefinicion, int contenido) {
		var validacionCantidad = !maestroDefinicion.isCantidadMinima()
				|| contenido >= maestroDefinicion.getNumeroCantidadMinima();
		if (!validacionCantidad) {
			this.validacionArchivo.setEstadoValidacion(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO);
			this.validacionArchivo
					.setDescripcionErrorEstructura("El numero de lineas es menor al esperado, o parametrizado");
			this.validacionArchivo.setNumeroErrores(1);
		}
		return validacionCantidad;
	}


}

