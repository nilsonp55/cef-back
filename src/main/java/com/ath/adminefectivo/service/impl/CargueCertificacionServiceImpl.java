package com.ath.adminefectivo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.ICargueCertificacionService;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IParametroService;
import com.ath.adminefectivo.service.IValidacionArchivoService;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CargueCertificacionServiceImpl implements ICargueCertificacionService {

	@Autowired
	IArchivosCargadosService archivosCargadosService;

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

	private ValidacionArchivoDTO validacionArchivo;

	@Override
	@Transactional
	public ValidacionArchivoDTO procesarArchivo2(String idMaestroDefinicion, String nombreArchivo, boolean alcance,
			Date fechaActual, Date fechaAnteriorHabil, Date fechaAnteriorHabil2) {
	  log.debug(
          "procesarArchivo2 inicio -  idMaestroDefinicion: {} - nombreArchivo: {} - alcance:{} - fechaActual: {} - fechaAnteriorHabil: {} fechaAnteriorHabil2: {}",
          idMaestroDefinicion, nombreArchivo, alcance, fechaActual, fechaAnteriorHabil,
          fechaAnteriorHabil2);
		this.validacionesAchivoCargado(idMaestroDefinicion, nombreArchivo, alcance, fechaActual, fechaAnteriorHabil,
				fechaAnteriorHabil2);
		if (!Objects.equals(this.validacionArchivo.getEstadoValidacion(), Dominios.ESTADO_VALIDACION_FUTURO)) {
			Long idArchivo = archivosCargadosService.persistirDetalleArchivoCargado(validacionArchivo, false, alcance);
			log.debug("Detalles archivo: {} persistido en tabla", nombreArchivo);
			this.validacionArchivo.setIdArchivo(idArchivo);
			String urlDestino = (Objects.equals(this.validacionArchivo.getEstadoValidacion(),
					Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO))
							? parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_ERRADOS)
							: parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_PROCESADOS);

			this.filesService.moverArchivos(this.validacionArchivo.getUrl(),
					this.validacionArchivo.getMaestroDefinicion().getUbicacion().concat(urlDestino),
					this.validacionArchivo.getNombreArchivo(), idArchivo.toString());
		} else {
		  log.debug("Archivo: {} en estado futuro", nombreArchivo);
			this.validacionArchivo.setIdArchivo((long) 0);
		}
		log.debug("procesarArchivo2 fin");
		return ValidacionArchivoDTO.conversionRespuesta(this.validacionArchivo);

	}

	/**
	 * Metodo encargado de realizar la validaciones de un archivo cargado
	 *
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return void
	 * @author cesar.castano
	 * @author prv_nparra
	 */
	@Override
	public ValidacionArchivoDTO validacionesAchivoCargado(String idMaestroDefinicion, String nombreArchivo, boolean alcance,
			Date fechaActual, Date fechaAnteriorHabil, Date fechaAnteriorHabil2) {
      log.debug(
          "validacionesAchivoCargado inicio - idMaestroDefinicion: {} - nombreArchivo: {} - alcance:{} - fechaActual: {} - fechaAnteriorHabil: {} fechaAnteriorHabil2: {}",
          idMaestroDefinicion, nombreArchivo, alcance, fechaActual, fechaAnteriorHabil,
          fechaAnteriorHabil2);
		this.validacionArchivo = new ValidacionArchivoDTO();
		// Validaciones del archivo
		var maestroDefinicion = maestroDefinicionArchivoService.consultarDefinicionArchivoById(idMaestroDefinicion);
		var urlPendientes = parametrosService.valorParametro(Parametros.RUTA_ARCHIVOS_PENDIENTES);
		var url = maestroDefinicion.getUbicacion().concat(urlPendientes).concat(nombreArchivo);
		log.debug("maestroDefinicion: {} - urlPendientes: {} - url: {}", maestroDefinicion, urlPendientes, url);
		validacionArchivoService.validarNombreArchivo(maestroDefinicion, nombreArchivo);
		var dowloadFile = filesService.downloadFile(DownloadDTO.builder().url(url).build());

		// Validaciones de arcihvo	
		String delimitador = lecturaArchivoService.obtenerDelimitadorArchivo(maestroDefinicion);
		List<String[]> contenido = lecturaArchivoService.leerArchivo(dowloadFile.getFile(), delimitador,
				maestroDefinicion);

		this.validacionArchivo = ValidacionArchivoDTO.builder().nombreArchivo(nombreArchivo)
				.descripcion(maestroDefinicion.getDescripcionArch()).maestroDefinicion(maestroDefinicion).url(url)
				.numeroRegistros(obtenerNumeroRegistros(maestroDefinicion, contenido.size())).build();

		var fechaArchivo = validacionArchivoService.validarFechaArchivoBetween(nombreArchivo,
				maestroDefinicion.getMascaraArch(), fechaActual, fechaAnteriorHabil);
		this.validacionArchivo.setFechaArchivo(fechaArchivo);

		if (fechaArchivo.compareTo(fechaAnteriorHabil) <= 0) {
		  log.debug("if fechaAnteriorHabil");
			if (this.validarCantidadRegistros(maestroDefinicion, this.validacionArchivo.getNumeroRegistros())) {
			  log.debug("if validarCantidadRegistros");
			  this.validacionArchivo = validacionArchivoService.validar(maestroDefinicion, contenido,
						validacionArchivo);
			}
			if (fechaArchivo.compareTo(fechaAnteriorHabil2) <= 0 || alcance) {
			  log.debug("if fechaAnteriorHabil2");
				this.validacionArchivo.setEstadoValidacion(Dominios.ESTADO_VALIDACION_REPROCESO);
			}
		} else {
		  log.debug("if else fechaAnteriorHabil");
			this.validacionArchivo.setEstadoValidacion(Dominios.ESTADO_VALIDACION_FUTURO);
			this.validacionArchivo.setDescripcionErrorEstructura("Archivo con fecha futura, no se procesa");
		}
		log.debug("validacionesAchivoCargado fin");
		return this.validacionArchivo;
	}

	/**
	 * Obtiene la cantidad de registros, verificando si tiene cabecera y control
	 * final
	 *
	 * @param maestroDefinicion
	 * @param cantidad
	 * @return
	 * @return int
	 * @author cesar.castano
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

}
