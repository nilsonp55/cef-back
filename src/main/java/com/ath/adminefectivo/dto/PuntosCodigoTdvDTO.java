package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.PuntosCodigoTDV;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO  de la tabla PuntosCodigoTDV
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuntosCodigoTdvDTO {

	private String codigoTDV;
	
	private Integer codigoPunto;
	
	private String codigoPropioTDV;
	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<PuntosCodigoTDV, PuntosCodigoTdvDTO> CONVERTER_DTO = (PuntosCodigoTDV t) -> {
		var puntosCodigoTdvDTO = new PuntosCodigoTdvDTO();
		UtilsObjects.copiarPropiedades(t, puntosCodigoTdvDTO);
		return puntosCodigoTdvDTO;
	};
}
