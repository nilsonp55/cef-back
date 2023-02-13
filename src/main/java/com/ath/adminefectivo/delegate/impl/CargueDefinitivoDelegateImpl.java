package com.ath.adminefectivo.delegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.ICargueDefinitivoDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;
import com.ath.adminefectivo.service.IFestivosNacionalesService;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IValidacionArchivoService;

/**
 * Delegate responsable del manejo, consulta y persistencia de archivos
 *
 * @author CamiloBenavides
 */
@Service
public class CargueDefinitivoDelegateImpl implements ICargueDefinitivoDelegate {

	@Autowired
	IFilesService filesService;

	@Autowired
	IArchivosCargadosService archivosCargadosService;

	@Autowired
	IParametroService parametrosService;

	@Autowired
	IValidacionArchivoService validacionArchivoService;

	@Autowired
	IMaestroDefinicionArchivoService maestroDefinicionArchivoService;

	@Autowired
	ILecturaArchivoService lecturaArchivoService;
	
	@Autowired
	ILogProcesoDiarioService logProcesoDiarioService;
	
	@Autowired
	IFestivosNacionalesService festivosNacionalesService;
	
	@Autowired
	IAuditoriaProcesosService auditoriaProcesosService;

	private ValidacionArchivoDTO validacionArchivo;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean persistirArchvoCargado(MultipartFile file) {
		var url = filesService.persistirArchvo(file);
		ArchivosCargadosDTO archivo = ArchivosCargadosDTO.builder().nombreArchivo(file.getOriginalFilename())
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
	@Transactional
	public ValidacionArchivoDTO procesarArchivo(String idMaestroDefinicion, String nombreArchivo) {
		validarLogProcesoDiario();
		Date fechaProceso = parametrosService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		
		auditoriaProcesosService.ActualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_DEFINITIVO, 
				fechaProceso, Constantes.ESTADO_PROCESO_PROCESO, Constantes.ESTADO_PROCESO_PROCESO);
		
		this.validacionesAchivoCargado(idMaestroDefinicion, nombreArchivo);
		Long idArchivo = archivosCargadosService.persistirDetalleArchivoCargado(validacionArchivo, false);

		String urlDestino = (Objects.equals(this.validacionArchivo.getEstadoValidacion(),
				Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO))
						? parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_ERRADOS)
						: parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_PROCESADOS);

		this.filesService.moverArchivos(this.validacionArchivo.getUrl(),
				this.validacionArchivo.getMaestroDefinicion().getUbicacion().concat(urlDestino),
				this.validacionArchivo.getNombreArchivo(),idArchivo.toString());

		ValidacionArchivoDTO resultado = ValidacionArchivoDTO.conversionRespuesta(this.validacionArchivo);
		auditoriaProcesosService.ActualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_DEFINITIVO, 
				fechaProceso, Constantes.ESTADO_PROCESO_PROCESO, Constantes.ESTRUCTURA_OK);
		return resultado;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ValidacionArchivoDTO validarArchivo(String idMaestroDefinicion, String nombreArchivo) {
		validarLogProcesoDiario();
		this.validacionesAchivoCargado(idMaestroDefinicion, nombreArchivo);
		return ValidacionArchivoDTO.conversionRespuesta(this.validacionArchivo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArchivosCargadosDTO> consultarArchivos(String estado, String agrupador) {
		List<ArchivosCargadosDTO> listArchivosCargados = new ArrayList<>();

		var maestrosDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoByAgrupador(estado, agrupador);
		var urlPendinetes = filesService.consultarPathArchivos(estado);
		var maestro = maestrosDefinicion.get(0);
		var url = maestro.getUbicacion().concat(urlPendinetes);
		var archivos = filesService.obtenerContenidoCarpeta(url);
		archivos.forEach(x -> {
			String nombreArchivo;
			nombreArchivo = x.split("_")[0];
			maestrosDefinicion.forEach(y -> {
				String nombreMaestro;
				nombreMaestro = y.getMascaraArch().split("_")[0];
				if (nombreArchivo.equals(nombreMaestro)) {
					listArchivosCargados.add(
							organizarDatosArchivo(x, estado, y.getIdMaestroDefinicionArchivo(), y.getMascaraArch()));
				}
			});
		});
		listArchivosCargados.sort(Comparator.comparing(ArchivosCargadosDTO::getFechaArchivo,
				Comparator.nullsLast(Comparator.naturalOrder())));

		return listArchivosCargados;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidacionArchivoDTO consultarDetalleArchivo(Long idArchivoCargado) {
		return archivosCargadosService.consultarDetalleArchivo(idArchivoCargado);
	}
	
	/**
	 * 
	 * @param archivo
	 * @param estado
	 * @param idModeloArchivo
	 * @param mascaraArchivo
	 * @return
	 */
	private ArchivosCargadosDTO organizarDatosArchivo(String archivo, String estado,
			String idModeloArchivo, String mascaraArchivo) {
		ArchivosCargadosDTO archivosCargadosDTO = new ArchivosCargadosDTO();

		Date fechaDatos = validacionArchivoService.obtenerFechaArchivo(archivo, mascaraArchivo);
//        fechaDatos = festivosNacionalesService.consultarAnteriorHabil(fechaDatos);
        archivosCargadosDTO = ArchivosCargadosDTO.builder().estadoCargue(estado).nombreArchivo(archivo)
                .idModeloArchivo(idModeloArchivo).fechaArchivo(fechaDatos).build();

		return archivosCargadosDTO;

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
	 * Obtiene la cantidad de registros, verificando si tiene cabecera y control
	 * final
	 * 
	 * @param maestroDefinicion
	 * @param contenido
	 * @return
	 * @return int
	 * @author duvan.naranjo
	 */
	private int obtenerBumeroRegistros(MaestrosDefinicionArchivoDTO maestroDefinicion, int cantidad) {
		if (maestroDefinicion.isCabecera())
			cantidad--;
		if (maestroDefinicion.isControlFinal())
			cantidad--;

		return cantidad;
	}

	/**
	 * Metodo encargado de realizar la validaciones de un archivo cargado
	 * 
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return void
	 * @author CamiloBenavides
	 */
	private void validacionesAchivoCargado(String idMaestroDefinicion, String nombreArchivo) {
		this.validacionArchivo = new ValidacionArchivoDTO();
		// Validaciones del archivo
		var maestroDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoById(idMaestroDefinicion);
		var urlPendientes = parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_PENDIENTES);
		var url = maestroDefinicion.getUbicacion().concat(urlPendientes).concat(nombreArchivo);
		validacionArchivoService.validarNombreArchivo(maestroDefinicion, nombreArchivo);
		var dowloadFile = filesService.downloadFile(DownloadDTO.builder().url(url).build());

		// Validaciones de arcihvo
		String delimitador = lecturaArchivoService.obtenerDelimitadorArchivo(maestroDefinicion);
		List<String[]> contenido = lecturaArchivoService.leerArchivo(dowloadFile.getFile(), delimitador, maestroDefinicion);
		Date fechaActual = parametrosService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);
		Date fechaAnteriorHabil = festivosNacionalesService.consultarAnteriorHabil(fechaActual);
		var fechaArchivo = validacionArchivoService.validarFechaArchivo(nombreArchivo,
				maestroDefinicion.getMascaraArch(), fechaAnteriorHabil);

		this.validacionArchivo = ValidacionArchivoDTO.builder().nombreArchivo(nombreArchivo)
				.descripcion(maestroDefinicion.getDescripcionArch()).fechaArchivo(fechaArchivo)
				.maestroDefinicion(maestroDefinicion).url(url)
				.numeroRegistros(obtenerBumeroRegistros(maestroDefinicion, contenido.size())).build();
		if (this.validarCantidadRegistros(maestroDefinicion, this.validacionArchivo.getNumeroRegistros())) {
			this.validacionArchivo = validacionArchivoService.validar(maestroDefinicion, contenido, 
					validacionArchivo);
		}
	}
	
	/**
	 * Metodo encargado de validar el log de proceso diario
	 * @author cesar.castano
	 */
	private void validarLogProcesoDiario() {
		var log = logProcesoDiarioService.obtenerEntidadLogProcesoDiario(
										Dominios.CODIGO_PROCESO_LOG_DEFINITIVO);
		if (Objects.isNull(log)) {
			throw new NegocioException(ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getCode(),
					ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getDescription(),
					ApiResponseCode.ERROR_CODIGO_PROCESO_NO_EXISTE.getHttpStatus());
		}else {
			if (log.getEstadoProceso().equals(Dominios.ESTADO_PROCESO_DIA_COMPLETO)) {
				throw new NegocioException(ApiResponseCode.ERROR_PROCESO_YA_CERRADO.getCode(),
						ApiResponseCode.ERROR_PROCESO_YA_CERRADO.getDescription(),
						ApiResponseCode.ERROR_PROCESO_YA_CERRADO.getHttpStatus());
			}
		}
	}

}
