package com.ath.adminefectivo.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.DetallesDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.ListaDetalleDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ErroresCamposDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionLineasDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionMotorDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IDetalleDefinicionArchivoService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.ILecturaArchivoService;
import com.ath.adminefectivo.service.IMaestroDefinicionArchivoService;
import com.ath.adminefectivo.service.IMotorReglasService;
import com.ath.adminefectivo.service.IValidacionArchivoService;
import com.ath.adminefectivo.utils.UtilsString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
@Log4j2
public class ValidacionArchivoServiceImpl implements IValidacionArchivoService {

	@Autowired
	IDetalleDefinicionArchivoService detalleDefinicionArchivoService;

	@Autowired
	IDominioService dominioService;

	@Autowired
	IMotorReglasService motorReglasService;
	
	@Autowired
	ILecturaArchivoService lecturaArchivoService;
	
	@Autowired
    IMaestroDefinicionArchivoService maestroDefinicionArchivoService;

	private List<DetallesDefinicionArchivoDTO> listaDetalleDefinicion;

	private List<String> listaDominioFecha;

	private List<String> listaDominioFechaHora;

	private List<String> listaDominioCaracteres;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidacionArchivoDTO validar(MaestrosDefinicionArchivoDTO maestroDefinicion, List<String[]> contenido,
			ValidacionArchivoDTO validacionArchivo) {
	  log.debug("Validar inicio archivo: {}", validacionArchivo.getNombreArchivo());
		validacionArchivo.setEstadoValidacion(Dominios.ESTADO_VALIDACION_CORRECTO);
		List<ValidacionLineasDTO> respuesta = cargueDataInicial(maestroDefinicion, contenido);		
		validacionArchivo = validarEstructura(maestroDefinicion, contenido, validacionArchivo, respuesta);
		if (validacionArchivo.getEstadoValidacion().equals(Dominios.ESTADO_VALIDACION_CORRECTO)) {
			validarContenido(maestroDefinicion, validacionArchivo);
		}
		log.debug("Validar fin");
		return validacionArchivo;
	}

	/**
	 * Metodo encargado de realizar las validaciones correspondientes a las
	 * validaciones de contenido
	 *
	 * @param maestroDefinicion
	 * @param validacionArchivo
	 * @return
	 */
	private ValidacionArchivoDTO validarContenido(MaestrosDefinicionArchivoDTO maestroDefinicion,
			ValidacionArchivoDTO validacionArchivo) {
	  log.debug("validarContenido inicio");
		if (maestroDefinicion.isValidaContenido()) {

			for (int i = 0; i < validacionArchivo.getValidacionLineas().size(); i++) {
				ValidacionLineasDTO lineaDTO = validacionArchivo.getValidacionLineas().get(i);
				List<ErroresCamposDTO> erroresCampos = this.validarLineaReglas(lineaDTO,
						maestroDefinicion.getIdMaestroDefinicionArchivo(), validacionArchivo);
				if (!erroresCampos.isEmpty()) {
					lineaDTO.setCampos(erroresCampos);
					lineaDTO.setEstado(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO);
					validacionArchivo.setNumeroErrores(validacionArchivo.getNumeroErrores() + (erroresCampos.size()));
					validacionArchivo.setEstadoValidacion(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO);
				} else {
					lineaDTO.setEstado(Dominios.ESTADO_VALIDACION_CORRECTO);
				}
			}
		}
		log.debug("validarContenido fin");
		return validacionArchivo;
	}



	/**
	 * Metodo encargado de realizar la validacion de cada columna de la linea
	 * cargada llamando al motor de reglas para ejecutar la regla en caso de que se
	 * valide
	 * 
	 * @param lineaDTO
	 * @param idMaestroDefinicionArchivo
	 * @return List<ErroresCamposDTO>
	 * @author duvan.naranjo
	 */
	private List<ErroresCamposDTO> validarLineaReglas(ValidacionLineasDTO lineaDTO, String idMaestroDefinicionArchivo,
			ValidacionArchivoDTO validacionArchivo) {
	    log.debug("validarLineaReglas inicio");
	    List<ErroresCamposDTO> erroresCamposDTO = new ArrayList<>();
		List<String> contenido = lineaDTO.getContenido();

		ValidacionArchivoDTO validacionArchivoCopy = validacionArchivo.toBuilder().validacionLineas(List.of(lineaDTO))
				.build();

		Map<String, ListaDetalleDTO> detalleDefinicionMap = listaDetalleMap(contenido, listaDetalleDefinicion);

		for (int i = 0; i < contenido.size(); i++) {
			DetallesDefinicionArchivoDTO detalle = obtenerListaDetalleFiltrada(idMaestroDefinicionArchivo, i + 1,
					lineaDTO.getTipo());

			ValidacionMotorDTO validacionMotorDTO = this.validarReglas(detalle, contenido.get(i), validacionArchivoCopy,
					i, detalleDefinicionMap);
			if (Objects.nonNull(validacionMotorDTO) && !validacionMotorDTO.isValida()) {

				var mensajeErrores = validacionMotorDTO.getMensajesError();
				var mensajeErroresTxt = String.join(Constantes.SEPARADOR_PUNTO_Y_COMA, mensajeErrores);
				erroresCamposDTO.add(ErroresCamposDTO.builder().numeroCampo(i + 1)
						.estado(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO).contenido(contenido.get(i))
						.mensajeErrorTxt(mensajeErroresTxt).mensajeError(mensajeErrores).build());
			}
		}
		log.debug("validarLineaReglas fin");
		return erroresCamposDTO;
	}

	/**
	 * Metodo encargado de evaluar de que metodo llamar del motor ya sea por ser
	 * regla multiple o regla simple
	 * 
	 * @param detalle
	 * @param valorCampo
	 * @return ValidacionMotorDTO
	 * @author duvan.naranjo
	 */
	private ValidacionMotorDTO validarReglas(DetallesDefinicionArchivoDTO detalle, String valorCampo,
			ValidacionArchivoDTO validacionArchivo, int index, Map<String, ListaDetalleDTO> detalleDefinicionMap) {
	    log.debug("validarReglas inicio");
		if (detalle.isValidarReglas() && Objects.nonNull(detalle.getExpresionRegla())) {
			if (detalle.isMultiplesReglas()) {
				if (!Objects.isNull(detalle.getExpresionRegla())) {
					return motorReglasService.evaluarReglaMultiple(detalle.getExpresionRegla(), valorCampo,
							validacionArchivo, index, detalleDefinicionMap);
				}
			} else {
				if (!Objects.isNull(detalle.getExpresionRegla())) {
					return motorReglasService.evaluarReglaSimple(detalle.getExpresionRegla(), valorCampo,
							validacionArchivo, index, detalleDefinicionMap);
				}

			}
		}
		log.debug("validarReglas fin");
		return null;
	}

	/**
	 * metodo encargado de realizar las acciones previas a validar estructura y
	 * contenido
	 * 
	 * @param maestroDefinicion
	 * @param contenido
	 * @return List<ValidacionLineasDTO>
	 * @author duvan.naranjo
	 */
	private List<ValidacionLineasDTO> cargueDataInicial(MaestrosDefinicionArchivoDTO maestroDefinicion,
			List<String[]> contenido) {
	  log.debug("cargueDataInicial inicio");
		var listaDetalle = detalleDefinicionArchivoService
				.consultarDetalleDefinicionArchivoByIdMaestro(maestroDefinicion.getIdMaestroDefinicionArchivo());
		
		this.setListaDetalleDefinicion(listaDetalle);
		this.cargarListaDominios();

		List<String[]> contenidoValidado = validacionInicial(maestroDefinicion, contenido);
		log.debug("cargueDataInicial fin");
		return obtenerLineas(maestroDefinicion, contenidoValidado);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValidacionArchivoDTO validarEstructura(MaestrosDefinicionArchivoDTO maestroDefinicion,
			List<String[]> contenido, ValidacionArchivoDTO validacionArchivo, List<ValidacionLineasDTO> respuesta) {
	  log.debug("validarEstructura inicio");
		if (maestroDefinicion.isValidaEstructura()) {
			validacionArchivo.setValidacionLineas(validarEstructuraCampos(maestroDefinicion, respuesta));
			int erroresTotales = validacionDeErrores(validacionArchivo.getValidacionLineas());
			if (erroresTotales > 0) {
				validacionArchivo.setNumeroErrores(erroresTotales);
				validacionArchivo.setEstadoValidacion(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO);
			} else {
				validacionArchivo.setEstadoValidacion(Dominios.ESTADO_VALIDACION_CORRECTO);
			}
			return validacionArchivo;
		} else {
			validacionArchivo.setValidacionLineas(respuesta);
			validacionArchivo.setEstadoValidacion(Dominios.ESTADO_VALIDACION_CORRECTO);
			validacionArchivo.setDescripcionErrorEstructura(Dominios.ESTADO_VALIDACION_CORRECTO);
		}
		log.debug("validarEstructura fin");
		return validacionArchivo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validarNombreArchivo(MaestrosDefinicionArchivoDTO maestroDefinicion, String nombreArchivo) {
	  log.debug("validarNombreArchivo inicio - maestroDefinicion: {} - nombreArchivo: {}", maestroDefinicion, nombreArchivo);
		if (maestroDefinicion.getAgrupador().equals("CERTI")) {
			String[] arregloNombre = nombreArchivo.split(Constantes.EXPRESION_REGULAR_PUNTO);
			String inicioNombre = arregloNombre[0].substring(0, 2);
			String fecha;
			String mascaraFecha;
			List<String> formatoFecha;
			
			if (!StringUtils.equalsIgnoreCase(arregloNombre[1], maestroDefinicion.getExtension())) {
              log.debug("arregloNombre[1]: {} - maestroDefinicion.getExtension()", arregloNombre[1],
                  maestroDefinicion.getExtension());
				throw new NegocioException(ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getCode(),
						ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getDescription(),
						ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getHttpStatus());
			}
			switch (inicioNombre) {
			// fragmento que valida las archivos de certificaciones tipo atlas

			case "AC": {
				fecha = nombreArchivo.substring(5, 11);
				mascaraFecha = maestroDefinicion.getMascaraArch().substring(8, 14);
				formatoFecha = new ArrayList<>();
				formatoFecha.add(mascaraFecha);
				if (!UtilsString.isFecha(fecha, formatoFecha)) {
				  log.debug("Exception AC isFecha fecha: {} - formatoFecha: {}", fecha, formatoFecha);
					throw new NegocioException(ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getCode(),
							ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getDescription(),
							ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getHttpStatus());
				}
				break;
			}
			case "BS", "BI": {
				fecha = nombreArchivo.substring(8, 14);
				mascaraFecha = maestroDefinicion.getMascaraArch().substring(13, 19);
				formatoFecha = new ArrayList<>();
				formatoFecha.add(mascaraFecha);
				if (!UtilsString.isFecha(fecha, formatoFecha)) {
				  log.debug("Exception BS, BI isFecha fecha: {} - formatoFecha: {}", fecha, formatoFecha);
					throw new NegocioException(ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getCode(),
							ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getDescription(),
							ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getHttpStatus());
				}
				break;
			}
			case "TH", "VG": {
				fecha = getString(nombreArchivo);
				mascaraFecha = maestroDefinicion.getMascaraArch().substring(19, 27);
				formatoFecha = new ArrayList<>();
				formatoFecha.add(mascaraFecha);
				if (!UtilsString.isFecha(fecha, formatoFecha)) {
				  log.debug("Exception TH, VG isFecha fecha: {} - formatoFecha: {}", fecha, formatoFecha);
					throw new NegocioException(ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getCode(),
							ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getDescription(),
							ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getHttpStatus());
				}
				break;
			}
			case "SC": {
				if (nombreArchivo.contains("VILLAS")) {
					fecha = nombreArchivo.substring(4, 14);
					mascaraFecha = maestroDefinicion.getMascaraArch().substring(5, 13) + "yy";
				} else {
					fecha = nombreArchivo.substring(4, 12);
					mascaraFecha = maestroDefinicion.getMascaraArch().substring(5, 13);
				}

				formatoFecha = new ArrayList<>();
				formatoFecha.add(mascaraFecha);
				if (!UtilsString.isFecha(fecha, formatoFecha)) {
				  log.debug("Exception SC isFecha fecha: {} - formatoFecha: {}", fecha, formatoFecha);
					throw new NegocioException(ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getCode(),
							ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getDescription(),
							ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getHttpStatus());
				}
				break;
			}
			default: {
			  log.debug("Exception default: {}", ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getDescription());
				throw new NegocioException(ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getCode(),
						ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getDescription(),
						ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getHttpStatus());
			}
			}
            log.debug("arregloNombre: {} - inicioNombre:{} - fecha:{} - mascaraFecha{}",
                arregloNombre, inicioNombre, fecha, mascaraFecha);
		} else {
			String[] arregloNombre = nombreArchivo
					.replace(Constantes.SEPARADOR_FECHA_ARCHIVO, Constantes.SEPARADOR_EXTENSION_ARCHIVO)
					.split(Constantes.EXPRESION_REGULAR_PUNTO);
			maestroDefinicion.getMascaraArch().split(Constantes.SEPARADOR_FECHA_ARCHIVO);
			log.debug("else: nombreArchivo: {}", nombreArchivo);
			if (!StringUtils.equalsIgnoreCase(arregloNombre[2], maestroDefinicion.getExtension())) {
              log.debug("arregloNombre[2]: {} - maestroDefinicion.getExtension()", arregloNombre[2],
                  maestroDefinicion.getExtension());
				throw new NegocioException(ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getCode(),
						ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getDescription(),
						ApiResponseCode.ERROR_FORMATO_NO_VALIDO.getHttpStatus());
			}
		}
		log.debug("validarNombreArchivo fin");
		return true;
	}

	private String getString(String nombreArchivo) {
		String fecha;
		if(nombreArchivo.length() == 26) {
			fecha = nombreArchivo.substring(14, 22);
		}
		else {
			if(nombreArchivo.length() == 27) {
				fecha = nombreArchivo.substring(15, 23);
			}
			else {
				if(nombreArchivo.length() == 29) {
					fecha = nombreArchivo.substring(17, 25);
				} else {
					fecha = null;
				}
			}
		}
		log.debug("nombreArchivo: {} - longitud: {} - fechaString: {}", nombreArchivo, nombreArchivo.length(), fecha);
		return fecha;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date validarFechaArchivo(String nombreArchivo, String mascaraArchivo, Date fechaComparacion) {

		Date fechaArchivo = this.obtenerFechaArchivo(nombreArchivo, mascaraArchivo);

		if (Objects.isNull(fechaArchivo)) {

			throw new NegocioException(ApiResponseCode.ERROR_FECHA_ARCHIVO_DIA.getCode(),
					ApiResponseCode.ERROR_FECHA_ARCHIVO_DIA.getDescription(),
					ApiResponseCode.ERROR_FECHA_ARCHIVO_DIA.getHttpStatus());
		} else {
			if (Objects.nonNull(fechaComparacion) && !DateUtils.isSameDay(fechaComparacion, fechaArchivo)) {
				throw new NegocioException(ApiResponseCode.ERROR_FECHA_ARCHIVO_DIA.getCode(),
						ApiResponseCode.ERROR_FECHA_ARCHIVO_DIA.getDescription(),
						ApiResponseCode.ERROR_FECHA_ARCHIVO_DIA.getHttpStatus());
			}
		}
		return fechaArchivo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date validarFechaArchivoBetween(String nombreArchivo, String mascaraArchivo, Date fechaComparacion,
			Date fechaAnteriorHabil) {
      log.debug(
          "nombreArchivo: {} - mascaraArchivo: {} - fechaComparacion: {} - fechaAnteriorHabil: {}",
          nombreArchivo, mascaraArchivo, fechaComparacion, fechaAnteriorHabil);
		Date fechaArchivo = this.obtenerFechaArchivo(nombreArchivo, mascaraArchivo);

		if (Objects.isNull(fechaArchivo)) {
		  log.debug("Fecha archivo no valida: {}", ApiResponseCode.ERROR_FECHA_ARCHIVO_DIA.getDescription());
			throw new NegocioException(ApiResponseCode.ERROR_FECHA_ARCHIVO_DIA.getCode(),
					ApiResponseCode.ERROR_FECHA_ARCHIVO_DIA.getDescription(),
					ApiResponseCode.ERROR_FECHA_ARCHIVO_DIA.getHttpStatus());
		}
		log.debug("validarFechaArchivoBetween fin - fechaArchivo: {}", fechaArchivo.toString());
		return fechaArchivo;
	}

	/**
	 * n {@inheritDoc}
	 */
	@Override
	public Date obtenerFechaArchivo(String nombreArchivo, String mascaraArchivo) {
	  log.debug("nombreArchivo: {}, mascaraArchivo: {}", nombreArchivo, mascaraArchivo);
		Date fechaArchivo = null;
		String fecha = null;
		String mascaraFecha = null;
		try {
			String inicioNombre = mascaraArchivo.substring(0, 2);											
			switch (inicioNombre) {

				case "AC": {
					fecha = nombreArchivo.substring(5, 11);
					mascaraFecha = mascaraArchivo.substring(8, 14);
					fechaArchivo = new SimpleDateFormat(mascaraFecha).parse(fecha);
					break;
				}
				case "BS", "BI": {
					fecha = nombreArchivo.substring(8, 14);
					mascaraFecha = mascaraArchivo.substring(13, 19);
					fechaArchivo = new SimpleDateFormat(mascaraFecha).parse(fecha);
					break;
				}
				case "TH", "VG": {
					fecha = getString(nombreArchivo);
					if(Objects.nonNull(fecha)) {
						mascaraFecha = mascaraArchivo.substring(19, 27);
						fechaArchivo = new SimpleDateFormat(mascaraFecha).parse(fecha);
					} else {
					  log.debug("fechaArchivo new Date()");
						fechaArchivo = new Date();
					}
					break;
				}
				case "SC": {
					if (nombreArchivo.contains("VILLAS")) {
						fecha = nombreArchivo.substring(4, 14);
					} else {
						fecha = nombreArchivo.substring(4, 12);
					}
					mascaraFecha = mascaraArchivo.substring(5, 13);
					fechaArchivo = new SimpleDateFormat(mascaraFecha).parse(fecha);
					break;
				}
				case "PR": {
					String[] arregloMascara = mascaraArchivo.split(Constantes.SEPARADOR_FECHA_ARCHIVO);
					var arregloNombre = nombreArchivo
							.replace(Constantes.SEPARADOR_FECHA_ARCHIVO, Constantes.SEPARADOR_EXTENSION_ARCHIVO)
							.split(Constantes.REGEX_PUNTO);
					if (arregloMascara[1].length() == arregloNombre[1].length()) {
						fechaArchivo = new SimpleDateFormat(arregloMascara[1]).parse(arregloNombre[1]);
					}
					else {
						if (arregloMascara[1].length() - arregloNombre[1].length() == 1) {
							fechaArchivo = new SimpleDateFormat(arregloMascara[1]).parse("0".concat(arregloNombre[1]));
						}
					}
					break;
				}
				default: {
				  log.debug("default new Date()");
					fechaArchivo = new Date();
					
				}
			}
            log.info("Obtener fecha archivo: {} - mascaraFecha: {} - fechaString: {}",
                nombreArchivo, mascaraFecha, fecha);
            Optional.of(fechaArchivo).ifPresent(t -> log.info("fechaArchivo: {}", t.toString()));
		} catch (Exception e) {
			log.error("Exception: {}", e);
			fechaArchivo = null;
		}
		
		return fechaArchivo;
	}
	

	/**
	 * Metodo encargado de validar si el maestro tiene cabecera y control final
	 * 
	 * @param maestro
	 * @param contenido
	 * @return List<String[]>
	 * @author duvan.naranjo
	 */
	private List<String[]> validacionInicial(MaestrosDefinicionArchivoDTO maestro, List<String[]> contenido) {
	  log.debug("validacionInicial inicio");
		if (maestro.isCabecera() && !contenido.isEmpty()) {
			contenido.remove(0);
		}
		if (maestro.isControlFinal() && !contenido.isEmpty()) {
			contenido.remove(contenido.size() - 1);
		}
		log.debug("validacionInicial inicio");
		return contenido;
	}

	/**
	 * Metodo encargado de recorrer cada registro del archivo y realizar la
	 * insercion de cada registro en el DTO
	 * 
	 * @param maestroDefinicion
	 * @param contenido
	 * @return List<ValidacionLineasDTO>
	 * @author duvan.naranjo
	 */
	private List<ValidacionLineasDTO> obtenerLineas(MaestrosDefinicionArchivoDTO maestroDefinicion,
			List<String[]> contenido) {
	  log.debug("obtenerLineas inicio");
		List<ValidacionLineasDTO> listadoValidacionLineasDTO = new ArrayList<>();
		for (int i = 0; i < contenido.size(); i++) {
			if (!Objects.isNull(contenido.get(i)) && contenido.get(i).length != 0) {
				var validacionArchivoDTO = obtenerCampos(contenido.get(i), i);
				this.obtenerTipoRegistro(maestroDefinicion, validacionArchivoDTO);
				listadoValidacionLineasDTO.add(validacionArchivoDTO);
			}
		}
		log.debug("obtenerLineas fin");
		return listadoValidacionLineasDTO;
	}

	/**
	 * Metodo encargado de recorrer cada registro de las filas y devolverla en DTO
	 * 
	 * @param fila
	 * @param numeroDeLinea
	 * @return ValidacionLineasDTO
	 * @author duvan.naranjo
	 */
	private ValidacionLineasDTO obtenerCampos(String[] fila, int numeroDeLinea) {
	  log.debug("obtenerCampos inicio");
		List<String> listaCampos = Arrays.asList(fila);
		var contenidoTxt = String.join(", ", fila);
		log.debug("obtenerCampos fin");
		return ValidacionLineasDTO.builder().numeroLinea(numeroDeLinea + 1).contenidoTxt(contenidoTxt)
				.estado(Constantes.ESTRUCTURA_OK).contenido(listaCampos).build();

	}

	/**
	 * Metodo encargado de recorrer cada registro del contenido y devolverla en un
	 * listado de DTO con validaciones
	 * 
	 * @param maestro
	 * @param contenido
	 * @return List<ErroresCamposDTO>
	 * @author duvan.naranjo
	 */
	private List<ValidacionLineasDTO> validarEstructuraCampos(MaestrosDefinicionArchivoDTO maestro,
			List<ValidacionLineasDTO> contenido) {

		List<ValidacionLineasDTO> respuesta = new ArrayList<>();

		for (int i = 0; i < contenido.size(); i++) {
			respuesta.add(obtenerEstructuraCampos(maestro.getIdMaestroDefinicionArchivo(), contenido.get(i)));
		}
		return respuesta;
	}

	/**
	 * Metodo encargado de obtener las validaciones de estructura en caso de ser
	 * validadas
	 * 
	 * @param idMaestro
	 * @param validacionLineasDTO
	 * @return ValidacionLineasDTO
	 * @author duvan.naranjo
	 */
	private ValidacionLineasDTO obtenerEstructuraCampos(String idMaestro, ValidacionLineasDTO validacionLineasDTO) {
		List<ErroresCamposDTO> erroresCampos = new ArrayList<>();
		List<String> contenido = validacionLineasDTO.getContenido();
		boolean errorCampo = false;
		int minimo = 0;

		for (int i = 0; i < contenido.size() - minimo; i++) {
		    ErroresCamposDTO validacionEstructuraCampo = validarEstructuraCampo(contenido.get(i), idMaestro, i + 1,
					validacionLineasDTO.getTipo());
			if (!Objects.isNull(validacionEstructuraCampo)) {
				erroresCampos.add(validacionEstructuraCampo);
				errorCampo = true;
			}
		}
		if (errorCampo) {
			validacionLineasDTO.setEstado(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO);
		} else {
			validacionLineasDTO.setEstado(Dominios.ESTADO_VALIDACION_CORRECTO);
		}

		validacionLineasDTO.setCampos(erroresCampos);

		return validacionLineasDTO;
	}

	/**
	 * Metodo encargado de realizar las validaciones de estructura campo por campo
	 * ademas de filtrar el listado de detalles definicion archivo
	 * 
	 * @param valorCampo
	 * @param idMaestro
	 * @param numeroCampo
	 * @param tipoRegistro
	 * @return ErroresCamposDTO
	 * @author duvan.naranjo
	 */
	private ErroresCamposDTO validarEstructuraCampo(String valorCampo, String idMaestro, int numeroCampo,
			Integer tipoRegistro) {
		ErroresCamposDTO erroresCampo = null;
		List<String> mensajeErrores = new ArrayList<>();

		DetallesDefinicionArchivoDTO detalle = obtenerListaDetalleFiltrada(idMaestro, numeroCampo, tipoRegistro);

		if (!Objects.isNull(detalle)) {
			erroresCampo = validarDetalleDefinicion(detalle, valorCampo);
		} else {
			mensajeErrores.add(Constantes.DETALLE_NO_VALIDO);
			var mensajeErroresTxt = String.join(Constantes.SEPARADOR_PUNTO_Y_COMA, mensajeErrores);
			erroresCampo = ErroresCamposDTO.builder().numeroCampo(numeroCampo).contenido(valorCampo)
					.estado(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO).mensajeError(mensajeErrores)
					.mensajeErrorTxt(mensajeErroresTxt).build();
		}

		return erroresCampo;
	}

	/**
	 * Método encargado de validar el detalle de un campo
	 * 
	 * @param detalle
	 * @param valorCampo
	 * @return ErroresCamposDTO
	 * @author duvan.naranjo
	 */
	private ErroresCamposDTO validarDetalleDefinicion(DetallesDefinicionArchivoDTO detalle, String valorCampo) {
		List<String> mensajesErrores = new ArrayList<>();

		if (!validaRequerido(valorCampo, detalle)) {
			mensajesErrores.add(Constantes.CAMPO_REQUERIDO);
		}

		if (!valorCampo.isEmpty() && !validaTipoDato(valorCampo, detalle)) {
			mensajesErrores.add(Constantes.CAMPO_NO_VALIDO);
		}

		if (!mensajesErrores.isEmpty()) {
			var mensajeErroresTxt = String.join(Constantes.SEPARADOR_PUNTO_Y_COMA, mensajesErrores);
			return ErroresCamposDTO.builder().numeroCampo(detalle.getId().getNumeroCampo())
					.nombreCampo(detalle.getNombreCampo()).estado(Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO)
					.contenido(valorCampo).mensajeError(mensajesErrores).mensajeErrorTxt(mensajeErroresTxt).build();
		}

		return null;
	}

	/**
	 * Metodo encargado de realizar las validaciones de tipo dato de un campo
	 * 
	 * @param valorCampo
	 * @param detalle
	 * @return boolean
	 * @author duvan.naranjo
	 */
	private boolean validaTipoDato(String valorCampo, DetallesDefinicionArchivoDTO detalle) {
		String tipoDato = detalle.getTipoDato();
		boolean resultadoValidacion = false;

		switch (tipoDato) {
		case Dominios.TIPO_DATO_ENTERO:
			resultadoValidacion = UtilsString.isNumeroEntero(valorCampo);
			break;
		case Dominios.TIPO_DATO_DECIMAL:
			resultadoValidacion = UtilsString.validarNumeroDecimal(valorCampo, detalle.getDecimales());
			break;
		case Dominios.TIPO_DATO_TEXTO:
			resultadoValidacion = UtilsString.isTexto(valorCampo, listaDominioCaracteres);
			break;
		case Dominios.TIPO_DATO_CARACTER:
			resultadoValidacion = UtilsString.isChar(valorCampo);
			break;
		case Dominios.TIPO_DATO_FECHA:
			resultadoValidacion = UtilsString.isFecha(valorCampo, listaDominioFecha);
			break;
		case Dominios.TIPO_DATO_HORA:
			resultadoValidacion = UtilsString.isHoras(valorCampo);
			break;
		case Dominios.TIPO_FECHA_HORA:
			resultadoValidacion = UtilsString.isDateWithHours(valorCampo, listaDominioFechaHora);
			break;

		default:
			break;
		}

		return resultadoValidacion;
	}

	/**
	 * Metodo encargado de realizar las validaciones del campo si es requerido
	 * 
	 * @param valorCampo
	 * @param detalle
	 * @return boolean
	 * @author duvan.naranjo
	 */
	private boolean validaRequerido(String valorCampo, DetallesDefinicionArchivoDTO detalle) {
		return !detalle.isRequeridos() || !valorCampo.isEmpty();

	}

	/**
	 * Metodo encargado de setear en el dto ValidacionLineasDTO el tipo de formato
	 * de cada una de las lineas
	 * 
	 * @param maestroDefinicion
	 * @param validacionLineasDTO
	 * @return void
	 * @author CamiloBenavides
	 */
	private void obtenerTipoRegistro(MaestrosDefinicionArchivoDTO maestroDefinicion,
			ValidacionLineasDTO validacionLineasDTO) {
		log.debug("obtenerTipoRegistro inicio");
		if (maestroDefinicion.isMultiformato()) {
			try {
				Integer tipo = Integer
						.valueOf(validacionLineasDTO.getContenido().get(maestroDefinicion.getCampoMultiformato()));
				validacionLineasDTO.setTipo(tipo);
			} catch (ArrayIndexOutOfBoundsException e) {
			  log.error("Validacion archivo: {}", ApiResponseCode.ERROR_MULTIFORMATO_POSICION.getDescription());
			} catch (NumberFormatException e) {
			  log.error("Validacion archivo: {}", ApiResponseCode.ERROR_MULTIFORMATO_INVALIDO.getDescription());
			}
		} else {
			validacionLineasDTO.setTipo(Constantes.ID_MULTIFORMATO_STD);
		}
		log.debug("obtenerTipoRegistro fin");

	}

	/**
	 * Metodo encargado contar la cantidad de errores existentes en las lineas y
	 * campos de los DTO
	 * 
	 * @param contenido
	 * @return int
	 * @author duvan.naranjo
	 */
	private int validacionDeErrores(List<ValidacionLineasDTO> contenido) {
		int totalErrores = 0;

		for (int i = 0; i < contenido.size(); i++) {
			var linea = contenido.get(i);
			if (!linea.getCampos().isEmpty()) {
				totalErrores += linea.getCampos().size();
			}
		}

		return totalErrores;
	}

	/**
	 * Metodo encargado de cargar el listado de dominios para las validaciones de
	 * los tipos
	 * 
	 * @author duvan.naranjo
	 */
	private void cargarListaDominios() {
		this.setListaDominioFecha(dominioService.consultaListValoresPorDominio("FORMATO_FECHA"));
		this.setListaDominioFechaHora(dominioService.consultaListValoresPorDominio("FORMATO_FECHA_HORA"));
		this.setListaDominioCaracteres(dominioService.consultaListValoresPorDominio("CARACTER_INVALIDO_TX"));
	}

	/**
	 * 
	 * @param idMaestro
	 * @param numeroCampo
	 * @param tipoRegistro
	 * @return
	 */
	private DetallesDefinicionArchivoDTO obtenerListaDetalleFiltrada(String idMaestro, int numeroCampo,
			Integer tipoRegistro) {
	  log.debug("obtenerListaDetalleFiltrada inicio y fin");
		return listaDetalleDefinicion.stream()
				.filter(detalleDefinicion -> detalleDefinicion.getId().getTipoRegistro().equals(tipoRegistro)
						&& detalleDefinicion.getId().getIdArchivo().equals(idMaestro)
						&& numeroCampo == detalleDefinicion.getId().getNumeroCampo())
				.findFirst().orElse(null);
	}
	
	
	/**
	 * 
	 * @param contenido
	 * @param listaDetalleDefinicion
	 * @return Map<String, ListaDetalleDTO> 
	 */
	private Map<String, ListaDetalleDTO> listaDetalleMap(List<String> contenido,
			List<DetallesDefinicionArchivoDTO> listaDetalleDefinicion) {

		// Ordenar ascendentemente por numeroCampo
		listaDetalleDefinicion.sort(Comparator.comparing(d -> d.getId().getNumeroCampo()));

		Map<String, ListaDetalleDTO> mapa = new LinkedHashMap<>();
		for (int i = 0; i < listaDetalleDefinicion.size(); i++) {
			DetallesDefinicionArchivoDTO detalle = listaDetalleDefinicion.get(i);

			ListaDetalleDTO campo = ListaDetalleDTO.builder().nombreCampo(detalle.getNombreCampo())
					.tipoDato(detalle.getTipoDato()).valor(contenido.get(i)).build();

			mapa.put(detalle.getNombreCampo(), campo);
		}

		return mapa;
	}

}
