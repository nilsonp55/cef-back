package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.FestivosNacionales;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la estructura de la entidad FestivosNacionales
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FestivosNacionalesDTO {

	private Date fecha;

	private String descripcion;

	/**
	 * Funcion de conversion de DTO a Entidad
	 * 
	 * @author CamiloBenavides
	 */
	public static final Function<FestivosNacionalesDTO, FestivosNacionales> CONVERTER_ENTITY = (
			FestivosNacionalesDTO t) -> {
		var festivosNacionales = new FestivosNacionales();
		UtilsObjects.copiarPropiedades(t, festivosNacionales);
		return festivosNacionales;
	};

	/**
	 * Funcion de conversion de Entidad a DTO
	 * 
	 * @author CamiloBenavides
	 */
	public static final Function<FestivosNacionales, FestivosNacionalesDTO> CONVERTER_DTO = (FestivosNacionales t) -> {
		var festivosNacionalesDTO = new FestivosNacionalesDTO();
		UtilsObjects.copiarPropiedades(t, festivosNacionalesDTO);
		return festivosNacionalesDTO;
	};

}
