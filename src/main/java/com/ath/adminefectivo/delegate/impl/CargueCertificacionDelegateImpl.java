package com.ath.adminefectivo.delegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.ICargueCertificacionDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.BitacoraAutomaticosDTO;
import com.ath.adminefectivo.dto.DetallesProcesoAutomaticoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IBitacoraAutomaticosService;
import com.ath.adminefectivo.service.ICargueCertificacionService;
import com.ath.adminefectivo.service.IFestivosNacionalesService;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IValidacionArchivoService;
import lombok.extern.log4j.Log4j2;

/**
 * Delegate responsable del manejo, consulta y persistencia de archivos
 *
 * @author cesar.castano
 */
@Service
@Log4j2
public class CargueCertificacionDelegateImpl implements ICargueCertificacionDelegate {

	@Autowired
	IArchivosCargadosService archivosCargadosService;

	@Autowired
	IBitacoraAutomaticosService bitacoraAutomaicosService;

	@Autowired
	IFestivosNacionalesService festivosNacionalesService;

	@Autowired
	IFilesService filesService;

	@Autowired
	ILecturaArchivoService lecturaArchivoService;

	@Autowired
	ILogProcesoDiarioService logProcesoDiarioService;

	@Autowired
	IMaestroDefinicionArchivoService maestroDefinicionArchivoService;

	@Autowired
	IParametroService parametrosService;

	@Autowired
	IValidacionArchivoService validacionArchivoService;

	private ValidacionArchivoDTO validacionArchivo;
	
	@Autowired
	ICargueCertificacionService cargueCertificacionService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean persistirArchvoCargado(MultipartFile file) {
		var url = filesService.persistirArchvo(file);
		ArchivosCargadosDTO archivo = ArchivosCargadosDTO.builder().nombreArchivo(file.getOriginalFilename())
				.nombreArchivoUpper(file.getOriginalFilename().toUpperCase())
				.fechaInicioCargue(new Date()).estado(Constantes.REGISTRO_ACTIVO).contentType(file.getContentType())
				.estadoCargue(Constantes.ESTADO_CARGUE_PENDIENTE).url(url).build();

		archivosCargadosService.guardarArchivos(Arrays.asList(archivo));

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean eliminarArchivo(String nombreArchivo, String idMaestroArchivo) {
		var maestrosDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoById(idMaestroArchivo);
		String carpeta = parametrosService.valorParametro("RUTA_ARCHIVOS_PENDIENTES");
		String file = maestrosDefinicion.getUbicacion() + carpeta + nombreArchivo;
		return filesService.eliminarArchivo(file);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidacionArchivoDTO validarArchivo(String idMaestroDefinicion, String nombreArchivo) {
		log.info("archivo: {}", nombreArchivo);
		boolean alcance = esProcesoDiarioCerrado();
		Date fechaActual = parametrosService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);
		Date fechaAnteriorHabil = festivosNacionalesService.consultarAnteriorHabil(fechaActual);
		Date fechaAnteriorHabil2 = festivosNacionalesService.consultarAnteriorHabil(fechaAnteriorHabil);
		this.validacionArchivo = cargueCertificacionService.validacionesAchivoCargado(idMaestroDefinicion, nombreArchivo, alcance,fechaActual,fechaAnteriorHabil,fechaAnteriorHabil2);
		return ValidacionArchivoDTO.conversionRespuesta(this.validacionArchivo);
	}


	/**
	 * procesa archivo y captura posibles excepciones sin relanzarlas
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @param alcance
	 * @param fechaActual
	 * @param fechaAnteriorHabil
	 * @param fechaAnteriorHabil2
	 * @return
	 */
	private ValidacionArchivoDTO procesarArchivoSinExcep(String idMaestroDefinicion, String nombreArchivo, boolean alcance,
														 Date fechaActual, Date fechaAnteriorHabil, Date fechaAnteriorHabil2) {
      log.debug(
          "procesarArchivoSinExcep inicio - idMaestroDefinicion: {} - nombreArchivo: {} - alcance:{} - fechaActual: {} - fechaAnteriorHabil: {} fechaAnteriorHabil2: {}",
          idMaestroDefinicion, nombreArchivo, alcance, fechaActual, fechaAnteriorHabil,
          fechaAnteriorHabil2);
		try {
			return cargueCertificacionService.procesarArchivo2(idMaestroDefinicion, nombreArchivo, alcance, fechaActual, fechaAnteriorHabil, fechaAnteriorHabil2);
		} catch (Exception e) {
			log.error("Error validando nombre o fecha de archivo: {} -error: {}", this.validacionArchivo.getNombreArchivo(), e);
			this.validacionArchivo.setIdArchivo((long) 0);
			this.validacionArchivo.setEstadoValidacion("FALLIDO");
			this.validacionArchivo.setDescripcionErrorEstructura("Error validando nombre o fecha de archivo" + e.getMessage().substring(200));
		}
		log.debug("procesarArchivoSinExcep fin - validacionArchivo: {}", validacionArchivo.toString());
		return validacionArchivo;
	}


	/**
	 *
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return
	 */
	@Override
	public ValidacionArchivoDTO procesarArchivo(String idMaestroDefinicion, String nombreArchivo) {

		Date fechaActual = parametrosService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);
		Date fechaAnteriorHabil = festivosNacionalesService.consultarAnteriorHabil(fechaActual);
		Date fechaAnteriorHabil2 = festivosNacionalesService.consultarAnteriorHabil(fechaAnteriorHabil);
		boolean alcance = esProcesoDiarioCerrado();
		return cargueCertificacionService.procesarArchivo2(idMaestroDefinicion, nombreArchivo, alcance, fechaActual, fechaAnteriorHabil, fechaAnteriorHabil2);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidacionArchivoDTO consultarDetalleArchivo(Long idArchivoCargado) {

		return archivosCargadosService.consultarDetalleArchivo(idArchivoCargado);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArchivosCargadosDTO> consultarArchivos(String estado, String agrupador) {

		List<ArchivosCargadosDTO> listArchivosCargados = new ArrayList<>();
		var maestrosDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoByAgrupador(estado, agrupador);
		var maestro = maestrosDefinicion.get(0);
		var urlPendientes = filesService.consultarPathArchivos(estado);
		var url = maestro.getUbicacion().concat(urlPendientes);
		var archivos = filesService.obtenerContenidoCarpeta(url);
		log.info("Archivos, previo a procesar, encontrados en directorio Pendientes: url:{} - cantidad:{}",urlPendientes, archivos.size());
		archivos.forEach(x -> {
			log.debug("Validar archivo: {}", x);
			String nombreArchivo;
			nombreArchivo = x.split("_")[0];
			String inicialMascara = nombreArchivo.substring(0, 2);
			
			maestrosDefinicion.stream().filter( f -> f.getMascaraArch().startsWith(inicialMascara) )
			.findFirst().ifPresent(t -> {
              listArchivosCargados.add(organizarDatosArchivo(x, estado,
                  t.getIdMaestroDefinicionArchivo(), t.getMascaraArch()));
              log.debug("Archivo agregado a lista de procesar: {}", x);
            });
			
		});
		listArchivosCargados.sort(Comparator.comparing(ArchivosCargadosDTO::getFechaArchivo,
				Comparator.nullsLast(Comparator.naturalOrder())));
		log.info("Archivos agregados a lista de procesar: {}", listArchivosCargados.size());		
		return listArchivosCargados;
	}

	private ArchivosCargadosDTO organizarDatosArchivo(String archivo, String estado,
													  String idModeloArchivo, String mascaraArchivo) {

		Date fechaDatos = validacionArchivoService.obtenerFechaArchivo(archivo, mascaraArchivo);
		return ArchivosCargadosDTO.builder().estadoCargue(estado).nombreArchivo(archivo)
				.nombreArchivoUpper(archivo.toUpperCase())
				.idModeloArchivo(idModeloArchivo).fechaArchivo(fechaDatos).build();
	}

	/**
	 * Metodo encargado de validar si laactividad esta cerrada en el log de procesos diarios
	 * @author cesar.castano
	 */
	private boolean esProcesoDiarioCerrado() {
	  log.debug("Validar estado cerrado en el log de procesos diarios");
		var logProceso = logProcesoDiarioService.obtenerEntidadLogProcesoDiario(
				Dominios.CODIGO_PROCESO_LOG_CERTIFICACION);
		if (Objects.isNull(logProceso)) {
		  log.debug("Log de procesos diarios lanza Null");
			throw new NegocioException(ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getCode(),
					ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getDescription(),
					ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getHttpStatus());
		}
		else {
		    log.debug("Log de procesos diarios Cerrado");
			return logProceso.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_COMPLETO);
		}
	}

	/**
	 * Metodo o job que es ejecutado segun el cron configurado(de lunes a viernes de
	 * 7 a 12 cada 15 minutos) encargado de dejar registro de cada vez que se
	 * ejecuta el proceso automatico de archivos de certificaciones
	 *
	 * @author duvan.naranjo
	 */
	@Scheduled(cron = "0 5/1 7-17 * * *")
	public void certificacionesProgramadas() {
	  log.debug("Procesar certificacion inicio");
		// crea el registro en bitacora de automaticos
		Date fechaActual = parametrosService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);
		Date fechaAnteriorHabil = festivosNacionalesService.consultarAnteriorHabil(fechaActual);
		Date fechaAnteriorHabil2 = festivosNacionalesService.consultarAnteriorHabil(fechaAnteriorHabil);
		boolean alcance = esProcesoDiarioCerrado();
        log.info(
            "Procesar archivos de Certificacion, fechaActual: {} - fechaAnteriorHabil: {} - fechaAnteriorHabil2: {} - alcance: {}",
            fechaActual.toString(), fechaAnteriorHabil.toString(), fechaAnteriorHabil2.toString(),
            alcance);

		BitacoraAutomaticosDTO bitacoraDTO = BitacoraAutomaticosDTO.builder()
				.codigoProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION).fechaSistema(fechaActual)
				.fechaHoraInicio(new Date()).build();

		List<ValidacionArchivoDTO> validacionesArchivos = new ArrayList<>();
		this.consultarArchivos(Constantes.ESTADO_CARGUE_PENDIENTE, Dominios.AGRUPADOR_DEFINICION_ARCHIVOS_CERTIFICACION)
				.forEach(archivoCerti ->
						validacionesArchivos.add(
								this.procesarArchivoSinExcep(archivoCerti.getIdModeloArchivo(), archivoCerti.getNombreArchivo(),
										alcance, fechaActual,fechaAnteriorHabil,fechaAnteriorHabil2))
				);
		this.procesarValidacionRealizada(bitacoraDTO, validacionesArchivos);
		
		log.info("Finaliza procesar certificacion: {} - archivos validados: {}",  fechaActual.toString(), validacionesArchivos.size());
	}

	private BitacoraAutomaticosDTO procesarValidacionRealizada(BitacoraAutomaticosDTO bitacoraDTO,
															   List<ValidacionArchivoDTO> validacionesArchivos) {
	  log.debug("procesarValidacionRealizada inicio");
		List<DetallesProcesoAutomaticoDTO> listadoDetallesProcesos = new ArrayList<>();

		validacionesArchivos.forEach(validacion -> {
			DetallesProcesoAutomaticoDTO detalleProcesoAuto = new DetallesProcesoAutomaticoDTO();
			detalleProcesoAuto.setMensajeError(validacion.getDescripcion());
			detalleProcesoAuto.setNombreArchivo(validacion.getNombreArchivo());
			detalleProcesoAuto.setResultado(validacion.getEstadoValidacion());
			detalleProcesoAuto.setIdArchivo(validacion.getIdArchivo());
			listadoDetallesProcesos.add(detalleProcesoAuto);
		});
		bitacoraDTO.setDetallesProcesosAutomaticosDTO(listadoDetallesProcesos);
		log.debug("procesarValidacionRealizada fin");
		return bitacoraDTO;
	}
}