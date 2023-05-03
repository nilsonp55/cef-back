package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.ParametrosFuncionesDinamicas;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de los parametros de las FuncionesDinamicas
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParametrosFuncionesDinamicasDTO {

	private Integer idParametro;
	
	private Integer numeroParametro;
	
	private String nombreParametro;
	
	private String tipoDatoParametro;	
	
	private String valorDefecto;
	
	private String posiblesValores;

	private FuncionesDinamicasDTO funcionesDinamicasDTO;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<ParametrosFuncionesDinamicasDTO, ParametrosFuncionesDinamicas> CONVERTER_ENTITY = (ParametrosFuncionesDinamicasDTO t) -> {
		var parametrosFuncionesDinamicas = new ParametrosFuncionesDinamicas();
		UtilsObjects.copiarPropiedades(t, parametrosFuncionesDinamicas);
		return parametrosFuncionesDinamicas;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<ParametrosFuncionesDinamicas, ParametrosFuncionesDinamicasDTO> CONVERTER_DTO = (ParametrosFuncionesDinamicas t) -> {
		var parametrosFuncionesDinamicasDTO = new ParametrosFuncionesDinamicasDTO();
		UtilsObjects.copiarPropiedades(t, parametrosFuncionesDinamicasDTO);
		return parametrosFuncionesDinamicasDTO;
	};
}
