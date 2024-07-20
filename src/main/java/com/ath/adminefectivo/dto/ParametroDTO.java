package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import javax.validation.constraints.NotBlank;

import com.ath.adminefectivo.entities.Parametro;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de la entidad Parametro
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParametroDTO {

	@NotBlank
	private String codigo;

	private String descripcion;

	private String areaAplicativo;

	@NotBlank
	private String valor;

	private String estado;

	private String usuarioCreacion;

	private Date fechaCreacion;

	private String usuarioModificacion;

	private Date fechaModificacion;

	/**
	 * Funcion que retorna la entidad recibiendo un DTO 
	 */
	public static final Function<ParametroDTO, Parametro> CONVERTER_ENTITY = (ParametroDTO t) -> {
		var parametro = new Parametro();
		UtilsObjects.copiarPropiedades(t, parametro);
		return parametro;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<Parametro, ParametroDTO> CONVERTER_DTO = (Parametro t) -> {
		var parametroDTO = new ParametroDTO();
		UtilsObjects.copiarPropiedades(t, parametroDTO);
		return parametroDTO;
	};
}
