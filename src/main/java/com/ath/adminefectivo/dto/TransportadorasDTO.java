package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.Transportadoras;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de la tabla de transportadoras
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransportadorasDTO {

	private String codigo;
	
	private String nombreTransportadora;
	
	private String abreviatura;
	
	private String nombre2;
	
	private String abreviatura2;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<TransportadorasDTO, Transportadoras> CONVERTER_ENTITY = (TransportadorasDTO t) -> {
		var transportadoras = new Transportadoras();
		UtilsObjects.copiarPropiedades(t, transportadoras);
		return transportadoras;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<Transportadoras, TransportadorasDTO> CONVERTER_DTO = (Transportadoras t) -> {
		var transportadorasDTO = new TransportadorasDTO();
		UtilsObjects.copiarPropiedades(t, transportadorasDTO);
		return transportadorasDTO;
	};
}
