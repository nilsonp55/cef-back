package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.Cajeros;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de Cajeros
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CajerosDTO {

private Integer codigoPunto;
	
	private String codigoATM;
	
	private Integer codigoBancoAval;
	
	private String nombreCajero;
	
	private Double tarifaRuteo;
	
	private Double tarifaVerificacion;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<CajerosDTO, Cajeros> CONVERTER_ENTITY = (CajerosDTO t) -> {
		var cajeros = new Cajeros();
		UtilsObjects.copiarPropiedades(t, cajeros);
		return cajeros;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<Cajeros, CajerosDTO> CONVERTER_DTO = (Cajeros t) -> {
		var cajerosDTO = new CajerosDTO();
		UtilsObjects.copiarPropiedades(t, cajerosDTO);
		return cajerosDTO;
	};
}
