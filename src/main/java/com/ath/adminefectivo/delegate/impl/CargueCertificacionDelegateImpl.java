package com.ath.adminefectivo.delegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.delegate.ICargueCertificacionDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IValidacionArchivoService;

/**
 * Delegate responsable del manejo, consulta y persistencia de archivos
 * 
 * @author cesar.castano
 */
@Service
public class CargueCertificacionDelegateImpl implements ICargueCertificacionDelegate {

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
	public ValidacionArchivoDTO validarArchivo(String idMaestroDefinicion, String nombreArchivo) {
		validarLogProcesoDiario();
		this.validacionesAchivoCargado(idMaestroDefinicion, nombreArchivo);
		if (Objects.equals(this.validacionArchivo.getEstadoValidacion(), Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO)) {
			archivosCargadosService.persistirDetalleArchivoCargado(validacionArchivo, true);
		}
		return ValidacionArchivoDTO.conversionRespuesta(this.validacionArchivo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidacionArchivoDTO procesarArchivo(String idMaestroDefinicion, String nombreArchivo) {
		validarLogProcesoDiario();
		this.validacionesAchivoCargado(idMaestroDefinicion, nombreArchivo);
		archivosCargadosService.persistirDetalleArchivoCargado(validacionArchivo, false);

		String urlDestino = (Objects.equals(this.validacionArchivo.getEstadoValidacion(),
				Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO))
						? parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_ERRADOS)
						: parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_PROCESADOS);

		this.filesService.moverArchivos(this.validacionArchivo.getUrl(),
				this.validacionArchivo.getMaestroDefinicion().getUbicacion().concat(urlDestino),
				this.validacionArchivo.getNombreArchivo());

		return ValidacionArchivoDTO.conversionRespuesta(this.validacionArchivo);
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
	 * Valida si que el valor del contenido sea mayor al minimo paramtrizado
	 * 
	 * @param maestroDefinicion
	 * @param contenido
	 * @return
	 * @return boolean
	 * @author cesar.castano
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
	 * @author cesar.castano
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
	 * @author cesar.castano
	 */
	private void validacionesAchivoCargado(String idMaestroDefinicion, String nombreArchivo) {
		this.validacionArchivo = new ValidacionArchivoDTO();
		// Validaciones del archivo
		var maestroDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoById(idMaestroDefinicion);
		var urlPendinetes = parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_PENDIENTES);
		var url = maestroDefinicion.getUbicacion().concat(urlPendinetes).concat(nombreArchivo);
		validacionArchivoService.validarNombreArchivo(maestroDefinicion, nombreArchivo);
		var dowloadFile = filesService.downloadFile(DownloadDTO.builder().url(url).build());
		
		// Validaciones de arcihvo	
		String delimitador = lecturaArchivoService.obtenerDelimitadorArchivo(maestroDefinicion);
		List<String[]> contenido = lecturaArchivoService.leerArchivo(dowloadFile.getFile(), delimitador);
		Date fechaActual = parametrosService.valorParametroDate(Parametros.FECHA_DIA_ACTUAL_PROCESO);
		var fechaArchivo = validacionArchivoService.validarFechaArchivo(nombreArchivo,
				maestroDefinicion.getMascaraArch(), fechaActual);
		this.validacionArchivo = ValidacionArchivoDTO.builder().nombreArchivo(nombreArchivo)
				.descripcion(maestroDefinicion.getDescripcionArch()).fechaArchivo(fechaArchivo)
				.maestroDefinicion(maestroDefinicion).url(url)
				.numeroRegistros(obtenerBumeroRegistros(maestroDefinicion, contenido.size())).build();
		if (this.validarCantidadRegistros(maestroDefinicion, this.validacionArchivo.getNumeroRegistros())) {
			this.validacionArchivo = validacionArchivoService.validar(maestroDefinicion, contenido,
					validacionArchivo);
		}

	}

	private ArchivosCargadosDTO organizarDatosArchivo(String archivo, String estado,
			String idModeloArchivo, String mascaraArchivo) {
		ArchivosCargadosDTO archivosCargadosDTO = new ArchivosCargadosDTO();

		archivosCargadosDTO = ArchivosCargadosDTO.builder().estadoCargue(estado).nombreArchivo(archivo)
				.idModeloArchivo(idModeloArchivo)
				.fechaArchivo(validacionArchivoService.obtenerFechaArchivo(archivo, mascaraArchivo)).build();

		return archivosCargadosDTO;

	}

	/**
	 * Método encargado de organizar la lista de archivos y armar el objeto de
	 * archivos cargados
	 * 
	 * @param archivos
	 * @return
	 * @return List<ArchivosCargadosDTO>
	 * @author cesar.castano
	 */
	private List<ArchivosCargadosDTO> organizarDataArchivos(List<String> archivos, String estado,
			String idModeloArchivo, String mascaraArchivo) {
		List<ArchivosCargadosDTO> archivosCargados = new ArrayList<>();
		archivos.forEach(x -> archivosCargados.add(
				ArchivosCargadosDTO.builder().estadoCargue(estado).nombreArchivo(x).idModeloArchivo(idModeloArchivo)
						.fechaArchivo(validacionArchivoService.obtenerFechaArchivo(x, mascaraArchivo)).build()));

		return archivosCargados;

	}
	
	/**
	 * Metodo encargado de validar el log de proceso diario
	 * @author cesar.castano
	 */
	private void validarLogProcesoDiario() {
		var log = logProcesoDiarioService.obtenerEntidadLogProcesoDiario(
										Dominios.CODIGO_PROCESO_LOG_CERTIFICACION);
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
