package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.FallasRegistro;
import com.ath.adminefectivo.entities.id.FallasRegistroPK;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de la entidad FallasRegistroDTO
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FallasRegistroDTO {

	private FallasRegistroPK id;

	private String estado;

	private String descripcionError;

	private String usuarioCreacion;

	private Date fechaCreacion;

	private String usuarioModificacion;

	private Date fechaModificacion;

	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<FallasRegistroDTO, FallasRegistro> CONVERTER_ENTITY = (FallasRegistroDTO t) -> {
		var fallaRegistro = new FallasRegistro();
		UtilsObjects.copiarPropiedades(t, fallaRegistro);
		return fallaRegistro;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<FallasRegistro, FallasRegistroDTO> CONVERTER_DTO = (FallasRegistro t) -> {
		var dominioDTO = new FallasRegistroDTO();
		UtilsObjects.copiarPropiedades(t, dominioDTO);
		return dominioDTO;
	};
}
