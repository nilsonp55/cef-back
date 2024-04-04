package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.TasasCambio;
import com.ath.adminefectivo.entities.TasasCambioPK;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TasasCambioDTO {

	private TasasCambioPK tasasCambioPK;

	private Double tasaCambio;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO 
	 */
	public static final Function<TasasCambioDTO, TasasCambio> CONVERTER_ENTITY = (TasasCambioDTO t) -> {
		var tasasCambio = new TasasCambio();
		UtilsObjects.copiarPropiedades(t, tasasCambio);
		return tasasCambio;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<TasasCambio, TasasCambioDTO> CONVERTER_DTO = (TasasCambio t) -> {
		var tasasCambioDTO = new TasasCambioDTO();
		UtilsObjects.copiarPropiedades(t, tasasCambioDTO);
		return tasasCambioDTO;
	};
}
