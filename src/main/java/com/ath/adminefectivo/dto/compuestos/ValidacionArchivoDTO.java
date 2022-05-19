package com.ath.adminefectivo.dto.compuestos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.utils.UtilsObjects;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la información del archivo validado
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidacionArchivoDTO {

	private String nombreArchivo;
	private String descripcion;
	private int numeroRegistros;
	private int numeroErrores;
	private String estadoValidacion;
	private String descripcionErrorEstructura;
	private Date fechaArchivo;
	private Date fechaInicioCargue;
	private String usuarioCreacion;
	private String url;
	private MaestrosDefinicionArchivoDTO maestroDefinicion;
	private List<ValidacionLineasDTO> validacionLineas;


	/**
	 *  Funcion que retorna la copia de un DTO con la información de respuesta
	 * 
	 * @param t
	 * @return ValidacionArchivoDTO
	 * @author CamiloBenavides
	 */
	public static final ValidacionArchivoDTO conversionRespuesta(ValidacionArchivoDTO t) {
		var validacionRespuesta = new ValidacionArchivoDTO();
		validacionRespuesta.setNombreArchivo(t.nombreArchivo);
		validacionRespuesta.setDescripcion(t.descripcion);
		validacionRespuesta.setNumeroRegistros(t.numeroRegistros);
		validacionRespuesta.setDescripcionErrorEstructura(t.descripcionErrorEstructura);
		validacionRespuesta.setFechaArchivo(t.fechaArchivo);
		validacionRespuesta.setNumeroErrores(t.numeroErrores);
		validacionRespuesta.setEstadoValidacion(t.estadoValidacion);
		
		if (Objects.nonNull(t.validacionLineas)) {
			validacionRespuesta.setValidacionLineas(new ArrayList<>());
			t.validacionLineas.stream().filter(x -> Objects.equals(x.getEstado(), Dominios.ESTADO_VALIDACION_REGISTRO_ERRADO))
					.forEach(x -> validacionRespuesta.getValidacionLineas()
							.add(ValidacionLineasDTO.builder()
									.campos(organizarErrores(x.getCampos(), x.getNumeroLinea()))
									.numeroLinea(x.getNumeroLinea()).estado(x.getEstado()).tipo(x.getTipo()).build()));
		}

		return validacionRespuesta;
	}

	/**
	 * Metodo de conversion de errores
	 * 
	 * @param errores
	 * @param numeroLinea
	 * @return List<ErroresCamposDTO>
	 * @author CamiloBenavides
	 */
	private static final List<ErroresCamposDTO> organizarErrores(List<ErroresCamposDTO> errores, int numeroLinea) {
		List<ErroresCamposDTO> erroresCampos = new ArrayList<>();
		if (Objects.nonNull(errores)) {
			errores.forEach(x -> {
				ErroresCamposDTO erroresDto = new ErroresCamposDTO();
				UtilsObjects.copiarPropiedades(x, erroresDto);
				erroresDto.setNumeroLinea(numeroLinea);
				erroresCampos.add(erroresDto);
			});
		}

		return erroresCampos;
	}

}
