package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.Cajeros;
import com.ath.adminefectivo.entities.CajerosATM;
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
public class CajerosATMDTO {

private Integer codigoPunto;
	
	private String codigoATM;
	
	private Integer codigoBancoAval;
	
	private String nombreCajero;
	
	private Double tarifaRuteo;
	
	private Double tarifaVerificacion;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<CajerosATMDTO, CajerosATM> CONVERTER_ENTITY = (CajerosATMDTO t) -> {
		var cajeros = new CajerosATM();
		UtilsObjects.copiarPropiedades(t, cajeros);
		return cajeros;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<CajerosATM, CajerosATMDTO> CONVERTER_DTO = (CajerosATM t) -> {
		var cajerosDTO = new CajerosATMDTO();
		UtilsObjects.copiarPropiedades(t, cajerosDTO);
		return cajerosDTO;
	};
}
