package com.ath.adminefectivo.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.sql.Timestamp;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.ArchivosTarifasEspecialesDTO;
import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.TarifasEspecialesClienteDTO;
import com.ath.adminefectivo.dto.compuestos.ErroresCamposDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionLineasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.BancoSimpleInfoEntity;
import com.ath.adminefectivo.entities.ClientesCorporativos;
import com.ath.adminefectivo.entities.CostosTransporte;
import com.ath.adminefectivo.entities.TarifasEspecialesCliente;
import com.ath.adminefectivo.entities.Transportadoras;
import com.ath.adminefectivo.entities.VTarifasEspecialesClienteEntity;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ArchivosCargadosRepository;
import com.ath.adminefectivo.repositories.IBancoSimpleInfoRepository;
import com.ath.adminefectivo.repositories.IClientesCorporativosRepository;
import com.ath.adminefectivo.repositories.ITarifasEspecialesRepository;
import com.ath.adminefectivo.repositories.ITransportadorasRepository;
import com.ath.adminefectivo.repositories.IVTarifasEspecialesClienteRepository;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IArchivosLiquidacionService;
import com.ath.adminefectivo.service.IArchivosTarifasEspecialesService;
import com.ath.adminefectivo.service.IDetalleDefinicionArchivoService;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IRegistrosCargadosService;
import com.ath.adminefectivo.service.IValidacionArchivoService;
import com.ath.adminefectivo.utils.UtilsString;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ArchivosTarifasEspecialesServiceImpl implements IArchivosTarifasEspecialesService {
	
	@Autowired
    IFilesService filesService;
	
	@Autowired
	IParametroService parametrosService;
    
	@Autowired
	IValidacionArchivoService validacionArchivoService;

    @Autowired
    IMaestroDefinicionArchivoService maestroDefinicionArchivoService;
	
	@Autowired
	IArchivosCargadosService archivosCargadosService;
	
	@Autowired
	ILecturaArchivoService lecturaArchivoService;
	
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
    
    @Autowired
    IClientesCorporativosRepository clientesCorporativos;
    
    @Autowired
    IVTarifasEspecialesClienteRepository tarifasEspecialesCliente;
    
    @Autowired
    ITarifasEspecialesRepository tarifasEspeciales;
    
    @Autowired
	ArchivosCargadosRepository archivosCargadosRepository;
       
    private ValidacionArchivoDTO validacionArchivo;
    
    @Value("${aws.s3.active}")
    Boolean s3aws;
	
	@Override
	public List<ArchivosTarifasEspecialesDTO> filtrarPorMascara(List<ArchivosTarifasEspecialesDTO> archivos,
			String mascara, String extensionEsperada, String idMaestroDefinicion, boolean generarIndices) {

		String[] partesMascara = mascara.replace("[", "").split("]");
		String prefijoEsperado = partesMascara[0];
		DateTimeFormatter formatoFechaValida = DateTimeFormatter.ofPattern(Constantes.FECHA_PATTERN_YYYYMMDD);

		List<ArchivosTarifasEspecialesDTO> filtrados = archivos.stream()
				.filter(dto -> esArchivoValido(dto, prefijoEsperado, extensionEsperada, formatoFechaValida))
				.peek(dto -> asignarAtributosArchivo(dto, formatoFechaValida, idMaestroDefinicion))
				.collect(Collectors.toList());

		ordenarYAsignarIds(filtrados, generarIndices);
		return filtrados;
	}

	private boolean esArchivoValido(ArchivosTarifasEspecialesDTO dto, String prefijoEsperado, String extensionEsperada,
			DateTimeFormatter formatoFechaValida) {

		String nombreCompleto = dto.getNombreArchivoCompleto();
		if (nombreCompleto == null)
			return false;
		if (!esExtensionValida(nombreCompleto, extensionEsperada))
			return false;

		String nombreSinExt = quitarExtension(nombreCompleto);
		String[] partesNombre = nombreSinExt.split(Constantes.SEPARADOR_COSTOS_CLASIFICACION_MES_ANIO);

		if (partesNombre.length != 2)
			return false;
		if (!partesNombre[0].equals(prefijoEsperado))
			return false;

		return esFechaValida(partesNombre[1], formatoFechaValida);
	}

	private boolean esExtensionValida(String nombreCompleto, String extensionEsperada) {
		return nombreCompleto.toLowerCase()
				.endsWith(Constantes.SEPARADOR_EXTENSION_ARCHIVO + extensionEsperada.toLowerCase());
	}

	private String quitarExtension(String nombreCompleto) {
		return nombreCompleto.substring(0, nombreCompleto.lastIndexOf(Constantes.SEPARADOR_EXTENSION_ARCHIVO));
	}

	private boolean esFechaValida(String fechaStr, DateTimeFormatter formatoFechaValida) {
		try {
			LocalDate.parse(fechaStr, formatoFechaValida);
			return true;
		} catch (DateTimeParseException e) {
			return false;
		}
	}

	private void asignarAtributosArchivo(ArchivosTarifasEspecialesDTO dto, DateTimeFormatter formatoFechaValida,
			String idMaestroDefinicion) {
		String nombreCompleto = dto.getNombreArchivoCompleto();
		String nombreSinExt = quitarExtension(nombreCompleto);
		String fechaStr = nombreSinExt.split(Constantes.SEPARADOR_COSTOS_CLASIFICACION_MES_ANIO)[1];

		if (dto.getEstado() == null || dto.getEstado().isEmpty()) {
			dto.setEstado(Constantes.ESTADO_PROCESO_PENDIENTE);
		}

		LocalDate fechaLocal = LocalDate.parse(fechaStr, formatoFechaValida);
		dto.setFechaArchivo(java.sql.Date.valueOf(fechaLocal));
		dto.setNombreArchivo(nombreSinExt);
		dto.setIdMaestroArchivo(idMaestroDefinicion);
	}

	private void ordenarYAsignarIds(List<ArchivosTarifasEspecialesDTO> responseList, boolean generaIndice) {
		responseList.sort(Comparator
				.comparing((ArchivosTarifasEspecialesDTO dto) -> dto.getFechaArchivo() != null ? dto.getFechaArchivo()
						: dto.getFechaTransferencia(), Comparator.nullsLast(Comparator.naturalOrder()))
				.thenComparing(ArchivosTarifasEspecialesDTO::getNombreArchivo,
						Comparator.nullsLast(Comparator.naturalOrder())));

		if (generaIndice) {
			AtomicLong counter = new AtomicLong(1);
			responseList.forEach(dto -> dto.setIdArchivo(counter.getAndIncrement()));
		}
	}
	
	//------------------------------- PROCESAMIENTO ARCHIVOS ------------------------------------
	
	/**
	 * Metodo encargado de realizar la validaciones y procesamiento de un archivo cargado
	 * 
	 * @param ProcesarAchivoCargado
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return void
	 * @author johan.chaparro
	 */
	@Override
	public ValidacionArchivoDTO procesarAchivoCargadoTarifaEspecial(ArchivosLiquidacionDTO archivoProcesar) {
		
		this.validacionArchivo = new ValidacionArchivoDTO();

		String usuarioSesion = archivoProcesar.getUsuarioSesion();
		boolean permiteReemplazo = archivoProcesar.getPermitirReemplazoRegistros();
		String idMaestroDefinicion = archivoProcesar.getIdMaestroArchivo();
		String nombreArchivo = archivoProcesar.getNombreArchivoCompleto();
		Date fechaArchivo = archivoProcesar.getFechaArchivo();
		List<TarifasEspecialesClienteDTO> registrosArchivo = null;
		
		List<BancoSimpleInfoEntity> bancos;
	    List<Transportadoras> transportadoras;
	    	    	    
	    log.info("Inicia Proceso Archivos Pendientes de carga : Acceso AWS:{}", s3aws);
	    
	    try {
	        bancos = bancosRepository.findByEsAvalEqualsOne();
	        transportadoras = transportadorasRepository.findAll();
	    } catch (Exception e) {
	        throw new NegocioException(ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getCode(),
	                ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getDescription(),
	                ApiResponseCode.ERROR_LECTURA_DOCUMENTO.getHttpStatus());
	    }
	    
		// 1. Obtener el tipo de archivo
		var maestroDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoById(idMaestroDefinicion);
		var urlPendinetes = parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_PENDIENTES);
		var url = maestroDefinicion.getUbicacion().concat(urlPendinetes).concat(nombreArchivo);

		// 3. descargar temporal
		var dowloadFile = filesService.downloadFile(DownloadDTO.builder().url(url).build());
		
		// 4. obtener el contenido
		String delimitador = lecturaArchivoService.obtenerDelimitadorArchivo(maestroDefinicion);
		List<String[]> contenido = lecturaArchivoService.leerArchivo(dowloadFile.getFile(), delimitador,
				maestroDefinicion);

		this.validacionArchivo = ValidacionArchivoDTO.builder().nombreArchivo(nombreArchivo)
				.descripcion(maestroDefinicion.getDescripcionArch()).fechaArchivo(fechaArchivo)
				.maestroDefinicion(maestroDefinicion).url(url)
				.numeroRegistros(obtenerNumeroRegistrosTarifasEspeciales(maestroDefinicion, contenido.size())).build();

				this.validacionArchivo = validacionArchivoService.validar(maestroDefinicion, contenido,
						this.validacionArchivo);
				
			// SI validacion del contenido del archivo fue exitosa
			if (Objects.equals(this.validacionArchivo.getEstadoValidacion(), Dominios.ESTADO_VALIDACION_CORRECTO)) {
				
				registrosArchivo = parseCsv(this.validacionArchivo.getValidacionLineas(), bancos, transportadoras);
				
				// 3. Validar duplicados y traslapes dentro del archivo
	            validarDuplicadosYTraslapesArchivo(registrosArchivo);
	            
	            List<Integer> codigosCliente = registrosArchivo.stream()
	            	    .map(TarifasEspecialesClienteDTO::getCodigoCliente)
	            	    .distinct()
	            	    .toList();
				
				// 3. Consultar clientes en la vista de tarifas especales
		        List<TarifasEspecialesClienteDTO> registrosBD = obtenerTarifasPorClienteDTO(codigosCliente);
		        
		        validarDuplicadosYTraslapesDataBase(registrosArchivo, registrosBD, usuarioSesion, permiteReemplazo);
			}

			/* Persistir el estado de cargue del archivo validado */
			var alcance = false;
			Long idArchivo = archivosCargadosService.persistirDetalleArchivoCargado(validacionArchivo, false, alcance);
			this.validacionArchivo.setIdArchivo(idArchivo);

			// Si estado de validacion es OK persistir la informacion a tablas de proceso
			if (Objects.equals(this.validacionArchivo.getEstadoValidacion(), Dominios.ESTADO_VALIDACION_CORRECTO)) {

				// Persistir datos correctos en tarifas especiales
				persistirTarifasEspeciales(registrosArchivo, idArchivo);

				// Actualizar el estado de archivo a estado OK
				var actualizarArchivo = archivosCargadosService.consultarArchivoById(idArchivo);
				actualizarArchivo.setEstadoCargue(Dominios.ESTADO_VALIDACION_CORRECTO);
				archivosCargadosService.actualizarArchivosCargados(actualizarArchivo);
				this.validacionArchivo.setEstadoValidacion(Dominios.ESTADO_VALIDACION_CORRECTO);

				/* Mover archivo a carpeta de procesados */
				String urlDestino = parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_PROCESADOS);

				this.filesService.moverArchivosS3(this.validacionArchivo.getUrl(),
						this.validacionArchivo.getMaestroDefinicion().getUbicacion().concat(urlDestino),
						this.validacionArchivo.getNombreArchivo(), idArchivo.toString());
			}
			
			if (Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO.equals(this.validacionArchivo.getEstadoValidacion())) {
				/* Mover archivo a carpeta de errados */
				String urlDestino = parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_ERRADOS);

				this.filesService.moverArchivosS3(this.validacionArchivo.getUrl(),
						this.validacionArchivo.getMaestroDefinicion().getUbicacion().concat(urlDestino),
						this.validacionArchivo.getNombreArchivo(), idArchivo.toString());
			}
			
			return this.validacionArchivo;
	}
	
	//----------------------------------------------------------------------------------------
	
	private int obtenerNumeroRegistrosTarifasEspeciales(MaestrosDefinicionArchivoDTO maestroArchivo, int cantidad) {
		if (maestroArchivo.isControlFinal())
			cantidad--;
		if (maestroArchivo.isCabecera())
			cantidad--;

		return cantidad;
	}
	
	
	//-------------------- CARGUE Y TRANSFORMACION DE LOS CAMPOS CSV AL DTO  -------------------
	
	/**
	 * Construye la lista a partir del contenido del CSV y se transforma la información
	 * de bancos, cliente y transportadora proporcionada.
	 *
	 * La estructura resultante se utilizada en la validación de duplicidad y solapamiento de vigencias,
	 * considerando las reglas de negocio sobre los campos clave y el cruce de fechas de inicio/fin.
	 */
	private List<TarifasEspecialesClienteDTO> parseCsv(List<ValidacionLineasDTO> validacionLineas, List<BancoSimpleInfoEntity> bancos,
			List<Transportadoras> transportadoras) {
		
		List<TarifasEspecialesClienteDTO> lista = new ArrayList<>();
		Integer codigoCliente = 0;
		
		

		for (ValidacionLineasDTO linea : validacionLineas) {

			 String[] campos = linea.getContenidoTxt().split("\\s*,\\s*");

			// Buscar codigoBanco usando la abreviatura en campos[0]
			Integer codigoBanco = bancos.stream().filter(b -> campos[0].equalsIgnoreCase(b.getAbreviatura()))
					.map(BancoSimpleInfoEntity::getCodigoPunto).findFirst().orElse(0);
			
			String codigoTransportadora = transportadoras.stream().filter(b -> campos[1].equalsIgnoreCase(b.getNombreTransportadora()))
					.map(Transportadoras::getCodigo).findFirst().orElse(null);

			Optional<ClientesCorporativos> clienteOpt = clientesCorporativos
					.findByIdentificacionAndCodigoBancoAvalAndAmparadoTrueAndAplicaTarifaEspecialTrue(campos[3],
							codigoBanco);

			if (clienteOpt.isPresent()) {
				codigoCliente = clienteOpt.get().getCodigoCliente();
			}

			TarifasEspecialesClienteDTO dto = TarifasEspecialesClienteDTO.builder()
					.idRegistro(linea.getNumeroLinea())
				    .codigoBanco(codigoBanco)
				    .codigoTdv(codigoTransportadora)
				    .codigoCliente(codigoCliente)
				    .codigoDane(campos[4])
				    .codigoPunto(nullIfEmptyInt(campos[5]))
				    .tipoOperacion(campos[6])
				    .tipoServicio(campos[7])
				    .tipoComision(campos[8])
				    .unidadCobro(campos[9])
				    .escala(campos[10])
				    .billetes(campos[11])
				    .monedas(campos[12])
				    .fajado(campos[13])
				    .valorTarifa(new BigDecimal(campos[14]))
				    .fechaInicioVigencia(parseDate(campos[15]))
				    .fechaFinVigencia(parseDate(campos[16]))
				    .limiteComisionAplicar(UtilsString.toInteger(campos[17]))
				    .valorComisionAdicional(new BigDecimal(campos[18]))
				    .estado("1".equals(campos[19]))
				    .build();

			lista.add(dto);
		}

		// Ordenar por codigoCliente antes de retornar
	    return lista.stream()
	            .sorted(Comparator.comparing(TarifasEspecialesClienteDTO::getCodigoCliente))
	            .toList();
	}

    private Integer nullIfEmptyInt(String val) {
        return (val == null || val.isBlank()) ? null : Integer.valueOf(val);
    }

    private Date parseDate(String fecha) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
        } catch (Exception e) {
            throw new RuntimeException("Fecha inválida: " + fecha);
        }
    }
    
  //-------------------- VALIDAR REGISTRO DUPLICADOS Y TRASLAPADOS EN ARCHIVO ------------------
    
    /**
     * Valida una lista de registros del archivo de tarifas especiales detectando:
     * 
     * 1. **Duplicados exactos** en los campos clave:
     *    BANCO, CLIENTE, TDV, CIUDAD, PUNTO, ESCALA,
     *    TIPO OPERACIÓN, TIPO SERVICIO, TIPO DE COMISIÓN,
     *    FECHA INICIO VIGENCIA y FECHA FIN VIGENCIA.
     * 
     * 2. **Traslapes de fechas** en registros con la misma clave (sin considerar fechas) bajo las siguientes reglas:
     *    - Si la *Fecha Inicio Vigencia* de un registro se encuentra dentro del rango
     *      [Fecha Inicio Vigencia, Fecha Fin Vigencia] de otro registro ya existente en el archivo.
     *      
     *    - Si la *Fecha Fin Vigencia* de un registro se encuentra dentro del rango
     *      [Fecha Inicio Vigencia, Fecha Fin Vigencia] de otro registro ya existente en el archivo.
     * 
     * Si se encuentran estas inconsistencias, lanza una excepcion con el detalle.
     */
    private void validarDuplicadosYTraslapesArchivo(List<TarifasEspecialesClienteDTO> registros) {
        StringBuilder errores = new StringBuilder();
        Map<String, Set<Integer>> mapaDuplicados = new HashMap<>();
        Set<String> traslapesDetectados = new HashSet<>();

        // Detectar duplicados exactos
        for (int i = 0; i < registros.size(); i++) {
            TarifasEspecialesClienteDTO r1 = registros.get(i);
            String claveUnica = generarClaveUnica(r1);
            mapaDuplicados.computeIfAbsent(claveUnica, k -> new TreeSet<>()).add(i + 1);
        }

        // Construir mensajes para duplicados exactos
        for (Set<Integer> filas : mapaDuplicados.values()) {
            if (filas.size() > 1) {
                String filasStr = filas.stream()
                                       .sorted()
                                       .map(String::valueOf)
                                       .collect(Collectors.joining(", "));
                errores.append("Duplicado exacto encontrado en filas ")
                       .append(filasStr)
                       .append(" | ");
            }
        }

        // Validar traslapes de fechas
        for (int i = 0; i < registros.size(); i++) {
            TarifasEspecialesClienteDTO r1 = registros.get(i);
            for (int j = i + 1; j < registros.size(); j++) {
                TarifasEspecialesClienteDTO r2 = registros.get(j);
                if (claveUnicaSinFechas(r1, r2) && fechasTraslapadas(r1, r2)) {
                    String key = i + 1 + "-" + (j + 1);
                    if (traslapesDetectados.add(key)) { // evita repetición de mensaje
                        errores.append("Traslape de fechas entre filas ")
                               .append(i + 1)
                               .append(" y ")
                               .append(j + 1)
                               .append(" | ");
                    }
                }
            }
        }

		if (errores.length() > 0) {
			throw new NegocioException(ApiResponseCode.ERROR_DUPLICIDAD_TRASLAPE_ARCHIVO_TARIFAS_ESPECIALES.getCode(),
					ApiResponseCode.ERROR_DUPLICIDAD_TRASLAPE_ARCHIVO_TARIFAS_ESPECIALES.getDescription() + ": || " + errores,
					ApiResponseCode.ERROR_DUPLICIDAD_TRASLAPE_ARCHIVO_TARIFAS_ESPECIALES.getHttpStatus());
		}
    }

    //------------------------- TRASLAPE Y DUPLICADOS EN BASE DE DATOS ------------------
    
    /**
     * Valida duplicados y traslapes entre dos listas distintas (archivo vs BD).
     */
	private void validarDuplicadosYTraslapesDataBase(List<TarifasEspecialesClienteDTO> archivo,
			List<TarifasEspecialesClienteDTO> bd, String usuarioSesion, boolean permiteReemplazo) {

		StringBuilder errores = new StringBuilder();
		Set<String> duplicadosExactos = new HashSet<>();

		for (int i = 0; i < archivo.size(); i++) {
		    TarifasEspecialesClienteDTO registroArchivo = archivo.get(i);
		    boolean esCandidatoReemplazo = false;
		    
			if (bd == null || bd.isEmpty()) {
				registroArchivo.setUsuarioCreacion(usuarioSesion);
				registroArchivo.setFechaCreacion(new Date());
			}

		    for (TarifasEspecialesClienteDTO registroBD : bd) {

		        // 1️ Candidato a reemplazo
		    	LocalDate inicioArchivo = toLocalDate(registroArchivo.getFechaInicioVigencia());
		    	LocalDate finArchivo    = toLocalDate(registroArchivo.getFechaFinVigencia());
		    	LocalDate inicioBD      = toLocalDate(registroBD.getFechaInicioVigencia());
		    	LocalDate finBD         = toLocalDate(registroBD.getFechaFinVigencia());

		    	// 1️ Candidato a reemplazo
		    	if (inicioArchivo.isBefore(inicioBD) && finArchivo.isAfter(finBD)) {

		            boolean hayOtrosTraslapes = bd.stream()
		                .anyMatch(otroBD ->
		                    claveUnicaSinFechas(registroArchivo, otroBD) &&
		                    fechasTraslapadas(registroArchivo, otroBD) &&
		                    !otroBD.equals(registroBD)
		                );

		            if (!hayOtrosTraslapes) {
		                registroArchivo.setIdTarifaEspecial(registroBD.getIdTarifaEspecial());
		                registroArchivo.setUsuarioCreacion(registroBD.getUsuarioCreacion());
		                registroArchivo.setFechaCreacion(registroBD.getFechaCreacion());
		                registroArchivo.setUsuarioModificacion(usuarioSesion);
		                registroArchivo.setFechaModificacion(new Date());
		                esCandidatoReemplazo = true;
		                break;
		                
		            } else {
		            	registroArchivo.setUsuarioCreacion(usuarioSesion);
			        	registroArchivo.setFechaCreacion(new Date());
		            }
		        }else {
		        	registroArchivo.setUsuarioCreacion(usuarioSesion);
		        	registroArchivo.setFechaCreacion(new Date());
		        }
		    }

		    // 2️ Validaciones normales
		    if (!esCandidatoReemplazo) {
		        for (TarifasEspecialesClienteDTO registroBD : bd) {
		        	
		            // Duplicado exacto
		            if (generarClaveUnica(registroArchivo).equals(generarClaveUnica(registroBD))) {
		                
		            	duplicadosExactos.add(registroArchivo.getIdRegistro() + "-" + registroBD.getIdTarifaEspecial());
		            	
		            	// Si el usuario autorizó el reemplazo de registros, 
		            	// se asigna el ID al registro para que sea actualizado en la base de datos.		            	
		            	if(permiteReemplazo) {
		            		
		            		registroArchivo.setIdTarifaEspecial(registroBD.getIdTarifaEspecial());
			                registroArchivo.setUsuarioCreacion(registroBD.getUsuarioCreacion());
			                registroArchivo.setFechaCreacion(registroBD.getFechaCreacion());
			                registroArchivo.setUsuarioModificacion(usuarioSesion);
			                registroArchivo.setFechaModificacion(new Date());
		            		
		            	} else {
		            		
		            		errores.append("Duplicado entre la fila ")
		                       .append(i + 1)
		                       .append(" del archivo y el registro en BD con idTarifaEspecial ")
		                       .append(registroBD.getIdTarifaEspecial())
		                       .append(" | ");
		            	}		 
		            }

					// Traslape de fechas (solo si no está en duplicadosExactos)
					String clavePar = registroArchivo.getIdRegistro() + "-" + registroBD.getIdTarifaEspecial();

					if (!duplicadosExactos.contains(clavePar) && claveUnicaSinFechas(registroArchivo, registroBD)
							&& fechasTraslapadas(registroArchivo, registroBD)) {

						int numeroLinea = i + 1;
					    String mensajeErroresTxt = "Se encontró traslape de fechas de vigencia con un registro de la base de datos (idTarifaEspecial: "
					            + registroBD.getIdTarifaEspecial() +")";					 
					    
					    //ValidacionLineasDTO
					    ErroresCamposDTO error = ErroresCamposDTO.builder()
					            .numeroCampo(0)
					            .estado(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO)
					            .contenido("Traslape de fechas")
					            .mensajeErrorTxt(mensajeErroresTxt)
					            .mensajeError(List.of(mensajeErroresTxt))
					            .build();

					        // Buscas la línea correspondiente en validacionArchivo
					        ValidacionLineasDTO linea = validacionArchivo.getValidacionLineas()
					            .stream()
					            .filter(vl -> vl.getNumeroLinea() == numeroLinea)
					            .findFirst()
					            .orElseGet(() -> {
					                // si no existe la línea, la creas
					                ValidacionLineasDTO nueva = ValidacionLineasDTO.builder()
					                    .numeroLinea(numeroLinea)
					                    .estado(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO)
					                    .campos(new ArrayList<>())
					                    .build();
					                validacionArchivo.getValidacionLineas().add(nueva);
					                return nueva;
					            });

					        // Agregas el error a esa línea
					        if (linea.getCampos() == null) {
					            linea.setCampos(new ArrayList<>());
					        }
					        linea.getCampos().add(error);

					        // Actualizas estado global del archivo
					        validacionArchivo.setNumeroErrores(validacionArchivo.getNumeroErrores() + 1);
					        validacionArchivo.setEstadoValidacion(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO);
					}
				}
			}
		}
		

		if (errores.length() > 0) {
			
			String mensajeErrores = errores.toString();
		    boolean contieneDuplicados = mensajeErrores.contains("Duplicado exacto");
		    if (contieneDuplicados) {
		        errores.append("| ¿Desea reemplazar los registros duplicados?");
		    }
		    
			throw new NegocioException(ApiResponseCode.ERROR_DUPLICIDAD_TRASLAPE_DATABASE_TARIFAS_ESPECIALES.getCode(),
					ApiResponseCode.ERROR_DUPLICIDAD_TRASLAPE_DATABASE_TARIFAS_ESPECIALES.getDescription() + ": || " + errores,
					ApiResponseCode.ERROR_DUPLICIDAD_TRASLAPE_DATABASE_TARIFAS_ESPECIALES.getHttpStatus());
		}
	}
    
    //----------------------------- METODOS UTILITARIOS -----------------------------------

    private String generarClaveUnica(TarifasEspecialesClienteDTO r) {
        return String.join("|",
                String.valueOf(r.getCodigoBanco()),
                String.valueOf(r.getCodigoTdv()),
                String.valueOf(r.getCodigoCliente()),
                String.valueOf(r.getCodigoDane()),
                String.valueOf(r.getCodigoPunto()),
                String.valueOf(r.getEscala()),
                String.valueOf(r.getTipoOperacion()),
                String.valueOf(r.getTipoServicio()),
                String.valueOf(r.getTipoComision()),
                String.valueOf(toLocalDate(r.getFechaInicioVigencia())),
                String.valueOf(toLocalDate(r.getFechaFinVigencia()))
        );
    }
    
    /** r1 y r2 comparten la misma clave de unicidad (sin fechas). */
    private boolean claveUnicaSinFechas(TarifasEspecialesClienteDTO r1, TarifasEspecialesClienteDTO r2) {
        return Objects.equals(r1.getCodigoBanco(), r2.getCodigoBanco()) &&
               Objects.equals(r1.getCodigoTdv(), r2.getCodigoTdv()) &&
               Objects.equals(r1.getCodigoCliente(), r2.getCodigoCliente()) &&
               Objects.equals(r1.getCodigoDane(), r2.getCodigoDane()) &&
               Objects.equals(r1.getCodigoPunto(), r2.getCodigoPunto()) &&
               Objects.equals(r1.getEscala(), r2.getEscala()) &&
               Objects.equals(r1.getTipoOperacion(), r2.getTipoOperacion()) &&
               Objects.equals(r1.getTipoServicio(), r2.getTipoServicio()) &&
               Objects.equals(r1.getTipoComision(), r2.getTipoComision());
    }

	private boolean fechasTraslapadas(TarifasEspecialesClienteDTO r1, TarifasEspecialesClienteDTO r2) {
		LocalDate inicio1 = toLocalDate(r1.getFechaInicioVigencia());
		LocalDate fin1 = toLocalDate(r1.getFechaFinVigencia());
		LocalDate inicio2 = toLocalDate(r2.getFechaInicioVigencia());
		LocalDate fin2 = toLocalDate(r2.getFechaFinVigencia());

		// traslapan si los rangos se intersectan
		return !(fin1.isBefore(inicio2) || inicio1.isAfter(fin2));
	}
    
    /** Verdadero si fecha está dentro [inicio, fin], inclusivo. */
    private boolean fechaEntre(Date fecha, TarifasEspecialesClienteDTO rango) {
        return !fecha.before(rango.getFechaInicioVigencia()) &&
               !fecha.after(rango.getFechaFinVigencia());
    }
    
    /** r1 contiene completamente a r2 (condición estricta de tu regla: menor/ mayor). */
    private boolean contieneRango(TarifasEspecialesClienteDTO r1, TarifasEspecialesClienteDTO r2) {
        return r1.getFechaInicioVigencia().before(r2.getFechaInicioVigencia()) &&
               r1.getFechaFinVigencia().after(r2.getFechaFinVigencia());
    }
    
    /** Convierte java.util.Date a java.time.LocalDate (ignora horas y zona horaria). */
    private LocalDate toLocalDate(Object fecha) {
        if (fecha == null) {
            return null;
        }

        if (fecha instanceof LocalDate) {
            return (LocalDate) fecha;
        }

        if (fecha instanceof java.sql.Date) {
            return ((java.sql.Date) fecha).toLocalDate();
        }

        if (fecha instanceof java.util.Date) {
            return ((java.util.Date) fecha).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }

        if (fecha instanceof String) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse((String) fecha, formatter);
        }

        throw new IllegalArgumentException("Tipo de fecha no soportado: " + fecha.getClass());
    }
    
    //------------------------------ DATABASE ------------------------------------------
    
    private List<TarifasEspecialesClienteDTO> obtenerTarifasPorClienteDTO(List<Integer> codigoCliente) {
        List<VTarifasEspecialesClienteEntity> entidades = 
        		tarifasEspecialesCliente.findByCodigoClienteIn(codigoCliente, Pageable.unpaged()).getContent();

        return entidades.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }
    
    private <T> TarifasEspecialesClienteDTO mapEntityToDTO(T entity) {
    	
    	TarifasEspecialesClienteDTO dto = new TarifasEspecialesClienteDTO();

        if (entity instanceof VTarifasEspecialesClienteEntity) {
            VTarifasEspecialesClienteEntity e = (VTarifasEspecialesClienteEntity) entity;
            dto.setIdTarifaEspecial(e.getIdTarifaEspecial());
            dto.setCodigoBanco(e.getCodigoBanco());
            dto.setCodigoTdv(e.getCodigoTdv());
            dto.setCodigoCliente(e.getCodigoCliente());
            dto.setCodigoDane(e.getCodigoDane());
            dto.setCodigoPunto(e.getCodigoPunto());
            dto.setTipoOperacion(e.getTipoOperacion());
            dto.setTipoServicio(e.getTipoServicio());
            dto.setTipoComision(e.getTipoComision());
            dto.setUnidadCobro(e.getUnidadCobro());
            dto.setEscala(e.getEscala());
            dto.setBilletes(e.getBilletes());
            dto.setMonedas(e.getMonedas());
            dto.setFajado(e.getFajado());
            dto.setValorTarifa(e.getValorTarifa());
            dto.setFechaInicioVigencia(e.getFechaInicioVigencia());
            dto.setFechaFinVigencia(e.getFechaFinVigencia());
            dto.setLimiteComisionAplicar(e.getLimiteComisionAplicar());
            dto.setValorComisionAdicional(e.getValorComisionAdicional());
            dto.setUsuarioCreacion(e.getUsuarioCreacion());
            dto.setFechaCreacion(e.getFechaCreacion());
            dto.setUsuarioModificacion(e.getUsuarioModificacion());
            dto.setFechaModificacion(e.getFechaModificacion());
            dto.setEstado(e.isEstado());
        } else if (entity instanceof TarifasEspecialesCliente) {
            TarifasEspecialesCliente e = (TarifasEspecialesCliente) entity;
            dto.setIdTarifaEspecial(e.getIdTarifaEspecial());
            dto.setCodigoBanco(e.getCodigoBanco());
            dto.setCodigoTdv(e.getCodigoTdv());
            dto.setCodigoCliente(e.getCodigoCliente());
            dto.setCodigoDane(e.getCodigoDane());
            dto.setCodigoPunto(e.getCodigoPunto());
            dto.setTipoOperacion(e.getTipoOperacion());
            dto.setTipoServicio(e.getTipoServicio());
            dto.setTipoComision(e.getTipoComision());
            dto.setUnidadCobro(e.getUnidadCobro());
            dto.setEscala(e.getEscala());
            dto.setBilletes(e.getBilletes());
            dto.setMonedas(e.getMonedas());
            dto.setFajado(e.getFajado());
            dto.setValorTarifa(e.getValorTarifa());
            dto.setFechaInicioVigencia(e.getFechaInicioVigencia());
            dto.setFechaFinVigencia(e.getFechaFinVigencia());
            dto.setLimiteComisionAplicar(e.getLimiteComisionAplicar());
            dto.setValorComisionAdicional(e.getValorComisionAdicional());
            dto.setUsuarioCreacion(e.getUsuarioCreacion());
            dto.setFechaCreacion(e.getFechaCreacion());
            dto.setUsuarioModificacion(e.getUsuarioModificacion());
            dto.setFechaModificacion(e.getFechaModificacion());
            dto.setEstado(e.isEstado());
        }

        return dto;
    }
    
    private void persistirTarifasEspeciales(List<TarifasEspecialesClienteDTO> registroArchivo, Long idArchivoCargado) {
    	
    	List<TarifasEspecialesCliente> listTarifasEspeciales = registroArchivo.stream()
    		    .map(dto -> {
    		        TarifasEspecialesCliente entity = TarifasEspecialesClienteDTO.CONVERTER_ENTITY.apply(dto);
    		        entity.setIdArchivoCargado(idArchivoCargado.intValue());
    		        return entity;
    		    })
    		    .collect(Collectors.toList());
    	    	
        tarifasEspeciales.saveAll(listTarifasEspeciales);
    }
    
    @Getter 
    @AllArgsConstructor
    private static class ReemplazoCandidato {
        private final int filaArchivo;
        private final Long idRegistroBd;
        private final String motivo;
    }

    @Getter 
    @AllArgsConstructor
    private static class ValidacionContraBD {
        private final List<ReemplazoCandidato> reemplazos;
    }
}
