package com.ath.adminefectivo.delegate.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.IArchivosLiquidacionDelegate;
import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.DetallesDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.ProcesarCampoDTO;
import com.ath.adminefectivo.dto.RegistrosCargadosDTO;
import com.ath.adminefectivo.dto.compuestos.ArchivosLiquidacionListDTO;
import com.ath.adminefectivo.dto.compuestos.ErroresCamposDTO;
import com.ath.adminefectivo.dto.compuestos.SummaryArchivoLiquidacionDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionLineasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.BancoSimpleInfoEntity;
import com.ath.adminefectivo.entities.Transportadoras;
import com.ath.adminefectivo.entities.id.RegistrosCargadosPK;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IBancoSimpleInfoRepository;
import com.ath.adminefectivo.repositories.ITransportadorasRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IArchivosLiquidacionService;
import com.ath.adminefectivo.service.IDetalleDefinicionArchivoService;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IRegistrosCargadosService;
import com.ath.adminefectivo.service.IValidacionArchivoService;

import lombok.extern.log4j.Log4j2;

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
	IArchivosLiquidacionService archivosLiquidacionService;
	
    @Autowired
    IBancoSimpleInfoRepository bancosRepository;

    @Autowired
    ITransportadorasRepository transportadorasRepository;
    
    @Autowired
	IRegistrosCargadosService registrosCargadosService;
    
    @Autowired
	IDetalleDefinicionArchivoService detalleDefinicionArchivoService;
       
    private ValidacionArchivoDTO validacionArchivo;
   	
	/**
	 * {@inheritDoc}
	 */
	@Override	
	public Page<ArchivosLiquidacionDTO> getAll(int start, int end, boolean content, String fileName) {
		
		List<BancoSimpleInfoEntity> bancos;
	    List<Transportadoras> transportadoras;
	    try {
	        bancos = bancosRepository.findByEsAvalEqualsOne();
	        transportadoras = transportadorasRepository.findAll();
	    } catch (Exception e) {
	        throw new NegocioException(ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getCode(),
	                ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getDescription(),
	                ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getHttpStatus());
	    }

	    List<String> cadenaTrasportadoras = obtenerCadenaTransportadoras(transportadoras);
	    List<String> cadenaEntidades = obtenerCadenaEntidades(bancos);
	    List<MaestrosDefinicionArchivoDTO> maestrosDefinicion = consultarMaestrosDefinicion();
	    String urlPendientes = filesService.consultarPathArchivos(Constantes.ESTADO_CARGUE_PENDIENTE);
	    String url = maestrosDefinicion.get(0).getUbicacion().concat(urlPendientes);
	    String requiredFileExtension = maestrosDefinicion.get(0).getExtension();
	    List<ArchivosLiquidacionDTO> dtoResponseList = obtenerDtoResponseList(start, end, content, fileName, url);
	    if (dtoResponseList.isEmpty()) {
	        return new PageImpl<>(dtoResponseList);
	    }

	    List<String> cadenaMascara = obtenerCadenaMascara(maestrosDefinicion);
	    String[][] estructuraMascara = procesarMascaras(cadenaMascara);
	    List<String> cadenaTipos = getSegmentosCadena(cadenaMascara);

	    log.info("Archivos en directorio Pendientes: url:{} - cantidad:{}", url, dtoResponseList.size());

	    List<ArchivosLiquidacionDTO> responseList = procesarNombreArchivos(dtoResponseList, estructuraMascara, cadenaTrasportadoras, cadenaEntidades, requiredFileExtension, cadenaTipos, bancos);
	    ordenarYAsignarIds(responseList);

	    return new PageImpl<>(responseList);
	}
	
	/**
	 * Este método se encarga de eliminar los registros que el usuario 
	 * ha seleccionado en la pantalla de "Archivos Pendientes de Carga"
	 * 
	 * @param archivosLiquidacion
	 * @return ArchivosLiquidacionListDTO
	 * @author johan.chaparro
	 */
	@Override
	public ArchivosLiquidacionListDTO eliminarArchivo(ArchivosLiquidacionListDTO archivosLiquidacion) {		
		
		List<ArchivosLiquidacionDTO> archivosCargados = archivosCargadosService
	            .guardarArchivosLiquidacion(archivosLiquidacion);

		return ArchivosLiquidacionListDTO.builder()
	            .validacionArchivo(archivosCargados)
	            .build();	
	}
	
	/**
	 * Metodo encargado de consultar el detalle de los errores por archivo
	 * 
	 * @param idArchivoCargado
	 * @return ValidacionArchivoDTO
	 * @author johan.chaparro
	 */
	@Override
	public ValidacionArchivoDTO consultarDetalleError(Long idArchivoCargado) {

		 ValidacionArchivoDTO validacionArchivoDTO = archivosCargadosService.consultarDetalleArchivo(idArchivoCargado);

		    if (validacionArchivoDTO != null && validacionArchivoDTO.getValidacionLineas() != null) {
		        for (ValidacionLineasDTO validacionLinea : validacionArchivoDTO.getValidacionLineas()) {
		            if (validacionLinea.getCampos() != null) {
		                for (ErroresCamposDTO campo : validacionLinea.getCampos()) {
		                    campo.setNumeroLinea(validacionLinea.getNumeroLinea());
		                }
		            }
		        }
		    }

		    return validacionArchivoDTO;

	}
	
	/**
	 * Metodo encargado de consultar el detalle de los registros por archivo
	 * 
	 * @param idArchivoCargado
	 * @return RegistrosCargadosDTO
	 * @author johan.chaparro
	 */
	@Override
	public List<RegistrosCargadosDTO> consultarDetalleArchivo(Long idArchivoCargado) {
				
		RegistrosCargadosDTO registrosCargados = new RegistrosCargadosDTO();
		RegistrosCargadosPK primaryKey = new RegistrosCargadosPK();		
		registrosCargados.setContenido(getCabecera(idArchivoCargado));

		List<RegistrosCargadosDTO> listaRegistros = registrosCargadosService
				.consultarRegistrosCargadosPorIdArchivo(idArchivoCargado);

		if (listaRegistros != null && !listaRegistros.isEmpty()) {
			
			primaryKey.setConsecutivoRegistro(0);
			
			listaRegistros.stream().findFirst() // Busca el primer elemento en la lista, si existe
					.ifPresent(siguienteRegistro -> {
						registrosCargados.setEstadoRegistro(siguienteRegistro.getEstadoRegistro());
						registrosCargados.setTipo(siguienteRegistro.getTipo());
						registrosCargados.setEstado(siguienteRegistro.getEstado());
						registrosCargados.setId(primaryKey);
					});

			// Agrega el nuevo registro al principio de la lista
			listaRegistros.add(0, registrosCargados);
			
			// Itera sobre la lista de registros
		    for (RegistrosCargadosDTO registro : listaRegistros) {
		        // Agrega un salto de línea al contenido
		        String contenido = registro.getContenido();
		        if (contenido != null) {
		            registro.setContenido(contenido + "\n");
		        }
		    }
		}
		
		return listaRegistros;
	}
	
	private String getCabecera(long idArchivoCargado) {
		
		StringBuilder cabecera = new StringBuilder();
		ArchivosCargados archivosCargados = archivosCargadosService.consultarArchivoById(idArchivoCargado);

		var maestroDefinicion = maestroDefinicionArchivoService
				.consultarDefinicionArchivoById(archivosCargados.getIdModeloArchivo());

		if (maestroDefinicion.isCabecera()) {
			List<DetallesDefinicionArchivoDTO> listadoDetalleArchivo = detalleDefinicionArchivoService
					.consultarDetalleDefinicionArchivoByIdMaestro(archivosCargados.getIdModeloArchivo());

			for (int i = 0; i < listadoDetalleArchivo.size(); i++) {
				DetallesDefinicionArchivoDTO detalle = listadoDetalleArchivo.get(i);
				cabecera.append(detalle.getNombreCampo().toUpperCase());

				if (i < listadoDetalleArchivo.size() - 1) {
					cabecera.append(",");
				}
			}
		}

		return cabecera.toString();
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
			if (entidadEncontrada != null) {
	            break;
	        }
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
	
	private List<ArchivosLiquidacionDTO> inicializarDtoList (List<SummaryArchivoLiquidacionDTO> objetos, String url){

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
			return null;
		} else {
			SimpleDateFormat formato = new SimpleDateFormat(dateFormat);
			formato.setLenient(false);
			try {
				return formato.parse(cadenaFecha);
			} catch (ParseException e) {
				return null;
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
			
			 this.procesarAchivoCargado(f);
		
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
	public void procesarAchivoCargado(ArchivosLiquidacionDTO archivoProcesar) {
		this.validacionArchivo = new ValidacionArchivoDTO();
		
		String idMaestroDefinicion = archivoProcesar.getIdMaestroArchivo();
		String nombreArchivo       = archivoProcesar.getNombreArchivoCompleto();
		Date fechaArchivo =  archivoProcesar.getFechaArchivo();
		

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
	
	
		this.validacionArchivo = ValidacionArchivoDTO.builder().nombreArchivo(nombreArchivo)
				.descripcion(maestroDefinicion.getDescripcionArch())
				.fechaArchivo(fechaArchivo)
				.maestroDefinicion(maestroDefinicion)
				.url(url)
				.numeroRegistros(obtenerNumeroRegistros(maestroDefinicion, contenido.size())).build();
		
		//5. Validar Contenido,  validar que archivo no se encuentra aceptado
		if (this.validarCantidadRegistros(maestroDefinicion, this.validacionArchivo.getNumeroRegistros())) {
			
			//si validacion de archivo es true = (no ha sido aceptado)
			if (this.validarArchivoAceptado(nombreArchivo,idMaestroDefinicion)) {
				this.validacionArchivo = validacionArchivoService.validar(maestroDefinicion, contenido, this.validacionArchivo);
			}
			
			//Si validacion de estructura y contenido fue exitosa, validar registros aceptados ya conciliados
			if (Objects.equals(this.validacionArchivo.getEstadoValidacion(), Dominios.ESTADO_VALIDACION_CORRECTO)) {
				this.validacionArchivo = archivosLiquidacionService.validarCostoConciliado(maestroDefinicion, this.validacionArchivo);
			}
			
			/*Persistir el estado de cargue del archivo validado*/
			var alcance = false;
			Long idArchivo = archivosCargadosService.persistirDetalleArchivoCargado(validacionArchivo, false, alcance);
			this.validacionArchivo.setIdArchivo(idArchivo);
			
			//Si estado de validacion es OK persistir la informacion a tablas de proceso
			if (Objects.equals(this.validacionArchivo.getEstadoValidacion(), Dominios.ESTADO_VALIDACION_CORRECTO)) {
				
				//Persistir datos correctos en costos procesamiento y costos transporte
				archivosLiquidacionService.persistirCostos(this.validacionArchivo);
				
				/*Mover archivo a carpeta de procesados*/
				String urlDestino = parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_PROCESADOS);
				
				this.filesService.moverArchivosS3(this.validacionArchivo.getUrl(),
												this.validacionArchivo.getMaestroDefinicion().getUbicacion().concat(urlDestino),
												this.validacionArchivo.getNombreArchivo(), 
												idArchivo.toString());
			}
			
		}
		
	}
    
	/**
	 * Obtiene la cantidad de registros, verificando si tiene cabecera y control
	 * final
	 * 
	 * @param maestroDefinicion
	 * @param contenido
	 * @return<
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
	
	/**
	 * Valida si el archivo ya existe en estado aceptado // No se puede procesar
	 * 
	 * @param nombreArchivo
	 * @param idModeloArchivo
	 * @return
	 * @return boolean
	 * @author hector.mercado
	 */
	private boolean validarArchivoAceptado(String nombreArchivo, String idModeloArchivo ) {
		
		List<ArchivosCargados> listaAC =  archivosCargadosService
				.getRegistrosCargadosPorEstadoCargueyNombreUpperyModelo(Dominios.ESTADO_VALIDACION_ACEPTADO,
				nombreArchivo.toUpperCase(), 
				idModeloArchivo);
		
		//true no encontro archivos con mismo nombre en estado aceptado
		boolean validacionAceptado = (Objects.isNull(listaAC) || listaAC.isEmpty());
		
			if (!validacionAceptado)
			{
				this.validacionArchivo.setEstadoValidacion(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO);
				this.validacionArchivo.setDescripcionErrorEstructura("Archivo ya se encuentra conciliado");
				this.validacionArchivo.setNumeroErrores(1);
			}

		return validacionAceptado;
	}
	
	//--------------------------------------------------------------------------------------
	
	private String[][] procesarMascaras(List<String> cadenaMascara) {		
		return segmentaCadena(cadenaMascara.get(0));
	}

	private List<String> obtenerCadenaTransportadoras(List<Transportadoras> transportadoras) {
	    return transportadoras.stream()
	            .map(Transportadoras::getNombreTransportadora)
	            .toList();
	}

	private List<String> obtenerCadenaEntidades(List<BancoSimpleInfoEntity> bancos) {
	    return bancos.stream()
	            .map(BancoSimpleInfoEntity::getAbreviatura)
	            .toList();
	}

	private List<MaestrosDefinicionArchivoDTO> consultarMaestrosDefinicion() {
	    return maestroDefinicionArchivoService.consultarDefinicionArchivoByAgrupador(null, Constantes.LIQUIDACION_AGRUPADOR);
	}

	private List<ArchivosLiquidacionDTO> obtenerDtoResponseList(int start, int end, boolean content, String fileName, String url) {
	    return inicializarDtoList(filesService.obtenerContenidoCarpetaSummaryS3Object(url, start, end, content, fileName), url);
	}

	private List<String> obtenerCadenaMascara(List<MaestrosDefinicionArchivoDTO> maestrosDefinicion) {
	    return maestrosDefinicion.stream()
	            .map(MaestrosDefinicionArchivoDTO::getMascaraArch)
	            .toList();
	}

	private List<String> getSegmentosCadena(List<String> cadenaMascara) {
	    List<String> cadenaTipos = new ArrayList<>();
	    for (String mascara : cadenaMascara) {
	        // Defino el patrón para encontrar el primer segmento entre corchetes
	        Pattern pattern = Pattern.compile(Constantes.REGEX_EXTRAER_MASCARA);
	        Matcher matcher = pattern.matcher(mascara);

	        // Encuentro el primer segmento entre corchetes
	        if (matcher.find()) {
	            String primerSegmento = matcher.group(1);
	            cadenaTipos.add(primerSegmento);
	        }
	    }
	    return cadenaTipos;
	}

	private List<ArchivosLiquidacionDTO> procesarNombreArchivos(List<ArchivosLiquidacionDTO> dtoResponseList, String[][] estructuraMascara, List<String> cadenaTrasportadoras, List<String> cadenaEntidades, String requiredFileExtension, List<String> cadenaTipos, List<BancoSimpleInfoEntity> bancos) {
	    List<ArchivosLiquidacionDTO> responseList = new ArrayList<>();
	   
	    for (ArchivosLiquidacionDTO dto : dtoResponseList) {
	        if (procesarNombreArchivo(dto, cadenaTrasportadoras, cadenaEntidades, requiredFileExtension, estructuraMascara, cadenaTipos, bancos)) {
	        	dto.setEstado(Constantes.ESTADO_PROCESO_PENDIENTE);
	        	responseList.add(dto);
	        }
	    }
	    return responseList;
	}

	private boolean procesarNombreArchivo(ArchivosLiquidacionDTO dto, List<String> cadenaTrasportadoras, List<String> cadenaEntidades, String requiredFileExtension, String[][] estructuraMascara, List<String> cadenaTipos, List<BancoSimpleInfoEntity> bancos) {
	    String cadena = dto.getNombreArchivo();
	    try {
	        dto.setNombreArchivo(cadena.substring(0, cadena.lastIndexOf('.')));
	    } catch (Exception e) {
	        return false;
	    }

	    for (String[] keyValue : estructuraMascara) {
	    	
	    	ProcesarCampoDTO procesar = ProcesarCampoDTO.builder()
	    		    .archivosLiquidacionDTO(dto)
	    		    .cadenaTrasportadoras(cadenaTrasportadoras)
	    		    .cadenaEntidades(cadenaEntidades)
	    		    .cadena(cadena)
	    		    .requiredFileExtension(requiredFileExtension)
	    		    .cadenaTipos(cadenaTipos)
	    		    .bancos(bancos)
	    		    .keyValue(keyValue)
	    		    .build();
	    	
	    	cadena = procesarCampo(procesar);
	    	
	        if (cadena == null || cadena.isEmpty()) {
	            return false;
	        }
	    }

	    return true;
	}

	private String procesarCampo(ProcesarCampoDTO dto) {
		
		ArchivosLiquidacionDTO archivosLiquidacionDTO = dto.getArchivosLiquidacionDTO();
	    List<String> cadenaTrasportadoras = dto.getCadenaTrasportadoras();
	    List<String> cadenaEntidades = dto.getCadenaEntidades();
	    String cadena = dto.getCadena();
	    String requiredFileExtension = dto.getRequiredFileExtension();
	    List<String> cadenaTipos = dto.getCadenaTipos();
	    List<BancoSimpleInfoEntity> bancos = dto.getBancos();
	    String[] keyValue = dto.getKeyValue();
		
	    String nombreCampo = keyValue[0];
	    String valorCampo = keyValue[1];
	    switch (nombreCampo) {
	        case "TIPO":
	            return procesarTipo(archivosLiquidacionDTO, cadena, requiredFileExtension, cadenaTipos);
	        case "TRANSPORTADORA":
	            return procesarTransportadora(archivosLiquidacionDTO, cadenaTrasportadoras, cadena);
	        case "BANCO":
	            return procesarBanco(archivosLiquidacionDTO, cadenaEntidades, cadena, bancos);
	        case "FECHA":
	            return procesarFecha(archivosLiquidacionDTO, valorCampo, cadena) ? cadena : null;
	        default:
	            return cadena;
	    }
	}

	private String procesarTipo(ArchivosLiquidacionDTO dto, String cadena, String requiredFileExtension, List<String> cadenaTipos) {
	    
	    try {
	    	
	    	String[] nameAndExtension = getNameAndExtension(cadena, requiredFileExtension);
		    cadena = nameAndExtension[0];
	        String existeTipo = validaCadena(cadenaTipos, cadena);
	        String idMaestroArchivo = (existeTipo.equals("LIQ_TRANSPORTE")) ? Constantes.MAESTRO_ARCHIVO_TRANSPORTE : Constantes.MAESTRO_ARCHIVO_PROCESAMIENTO;
	        int indiceCaracter = existeTipo.indexOf('_');
	        if (indiceCaracter != -1) {
	            dto.setTipoArchivo(existeTipo.substring(indiceCaracter + 1));
	            dto.setIdMaestroArchivo(idMaestroArchivo);
	            cadena = cadena.replace(existeTipo, "");
	        }
	    } catch (Exception e) {
	        return null;
	    }
	    return cadena;
	}

	private String procesarTransportadora(ArchivosLiquidacionDTO dto, List<String> cadenaTrasportadoras, String cadena) {
	    String existeTransportadora = validaCadena(cadenaTrasportadoras, cadena);
	    if (existeTransportadora != null) {
	        dto.setTdv(existeTransportadora);
	        cadena = cadena.replace(existeTransportadora, "");
	    } else {
	        return null;
	    }
	    return cadena;
	}

	private String procesarBanco(ArchivosLiquidacionDTO dto, List<String> cadenaEntidades, String cadena, List<BancoSimpleInfoEntity> bancos) {
	    String existeEntidad = validaCadena(cadenaEntidades, cadena);
	    if (existeEntidad != null) {
	        Optional<BancoSimpleInfoEntity> result = bancos.stream()
	                .filter(banco -> existeEntidad.equals(banco.getAbreviatura()))
	                .findFirst();
	        if (result.isPresent()) {
	            dto.setBanco(result.get().getNombreBanco());
	            cadena = cadena.replace(existeEntidad, "");
	        } else {
	        	return null;
	        }
	    } else {
	        return null;
	    }
	    return cadena;
	}

	private boolean procesarFecha(ArchivosLiquidacionDTO dto, String valorCampo, String cadena) {
	    Date fecha = validaFecha(valorCampo, cadena);
	    if (fecha == null) {
	        return false;
	    } else {
	        dto.setFechaArchivo(fecha);
	        return true;
	    }
	}

	private void ordenarYAsignarIds(List<ArchivosLiquidacionDTO> responseList) {
	    responseList.sort(Comparator.comparing(ArchivosLiquidacionDTO::getFechaArchivo)
	            .thenComparing(ArchivosLiquidacionDTO::getNombreArchivo));

	    AtomicLong counter = new AtomicLong(1);
	    responseList.forEach(dto -> dto.setIdArchivo(counter.getAndIncrement()));
	}

}

