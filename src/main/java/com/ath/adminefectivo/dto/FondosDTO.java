package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de Fondos
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FondosDTO {

	private Integer codigoPunto;
	
	private String tdv;
	
	private Integer bancoAVAL;
	
	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<Fondos, FondosDTO> CONVERTER_DTO = (Fondos t) -> {
		var fondosDTO = new FondosDTO();
		UtilsObjects.copiarPropiedades(t, fondosDTO);
		return fondosDTO;
	};
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO 
	 */
	public static final Function<FondosDTO, Fondos> CONVERTER_ENTITY = (FondosDTO t) -> {
		var fondos = new Fondos();
		UtilsObjects.copiarPropiedades(t, fondos);
		return fondos;
	};
}
