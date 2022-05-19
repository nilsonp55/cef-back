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
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
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
	public Boolean eliminarArchivo(Long idArchivo) {
		var archivoCargadoDTO = archivosCargadosService.eliminarArchivo(idArchivo);
		if (Objects.nonNull(archivoCargadoDTO.getUrl())) {
			return filesService.eliminarArchivo(archivoCargadoDTO.getUrl());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public ValidacionArchivoDTO procesarArchivo(String idMaestroDefinicion, String nombreArchivo) {
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
	@Transactional
	public ValidacionArchivoDTO validarArchivo(String idMaestroDefinicion, String nombreArchivo) {

		this.validacionesAchivoCargado(idMaestroDefinicion, nombreArchivo);
		if (Objects.equals(this.validacionArchivo.getEstadoValidacion(), Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO)) {
			archivosCargadosService.persistirDetalleArchivoCargado(validacionArchivo, true);
		}
		return ValidacionArchivoDTO.conversionRespuesta(this.validacionArchivo);
	}

	@Override
	public List<ArchivosCargadosDTO> consultarArchivos(String estado) {

		List<ArchivosCargadosDTO> listArchivosCargados = new ArrayList<>();

		var maestrosDefinicion = maestroDefinicionArchivoService
				.consultarDefinicionArchivoByAgrupador(Dominios.AGRUPADOR_DEFINICION_ARCHIVOS_DEFINITIVO);
		var urlPendinetes = filesService.consultarPathArchivos(estado);

		maestrosDefinicion.forEach(x -> {
			var url = x.getUbicacion().concat(urlPendinetes);
			var archivos = filesService.obtenerContenidoCarpeta(url);
			listArchivosCargados.addAll(
					organizarDataArchivos(archivos, estado, x.getIdMaestroDefinicionArchivo(), x.getMascaraArch()));

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
		var urlPendinetes = parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_PENDIENTES);
		var url = maestroDefinicion.getUbicacion().concat(urlPendinetes).concat(nombreArchivo);
		validacionArchivoService.validarNombreArchivo(maestroDefinicion, nombreArchivo);
		var dowloadFile = filesService.downloadFile(DownloadDTO.builder().url(url).build());

		// Validaciones de arcihvo
		String delimitador = lecturaArchivoService.obtenerDelimitadorArchivo(maestroDefinicion);
		List<String[]> contenido = lecturaArchivoService.leerArchivo(dowloadFile.getFile(), delimitador);
		var fechaArchivo = validacionArchivoService.validarFechaArchivo(nombreArchivo,
				maestroDefinicion.getMascaraArch(), new Date());

		this.validacionArchivo = ValidacionArchivoDTO.builder().nombreArchivo(nombreArchivo)
				.descripcion(maestroDefinicion.getDescripcionArch()).fechaArchivo(fechaArchivo)
				.maestroDefinicion(maestroDefinicion).url(url)
				.numeroRegistros(obtenerBumeroRegistros(maestroDefinicion, contenido.size())).build();

		if (this.validarCantidadRegistros(maestroDefinicion, this.validacionArchivo.getNumeroRegistros())) {
			this.validacionArchivo = validacionArchivoService.validar(maestroDefinicion, contenido, validacionArchivo);
		}

	}

	/**
	 * Método encargado de organizar la lista de archivos y armar el objeto de
	 * archivos cargados
	 * 
	 * @param archivos
	 * @return
	 * @return List<ArchivosCargadosDTO>
	 * @author CamiloBenavides
	 */
	private List<ArchivosCargadosDTO> organizarDataArchivos(List<String> archivos, String estado,
			String idModeloArchivo, String mascaraArchivo) {
		List<ArchivosCargadosDTO> archivosCargados = new ArrayList<>();
		archivos.forEach(fecha -> archivosCargados.add(
				ArchivosCargadosDTO.builder().estadoCargue(estado).nombreArchivo(fecha).idModeloArchivo(idModeloArchivo)
						.fechaArchivo(validacionArchivoService.obtenerFechaArchivo(fecha, mascaraArchivo)).build()));

		return archivosCargados;

	}

}
