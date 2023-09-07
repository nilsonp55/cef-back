package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de Bancos
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BancosDTO {

	private Integer codigoCompensacion;
	
	private String numeroNit;
	
	private String abreviatura;
	
	private Boolean esAVAL;
	
	private Integer codigoPunto;
	
	private String nombreBanco;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<BancosDTO, Bancos> CONVERTER_ENTITY = (BancosDTO t) -> {
		var bancos = new Bancos();
		UtilsObjects.copiarPropiedades(t, bancos);
		return bancos;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<Bancos, BancosDTO> CONVERTER_DTO = (Bancos t) -> {
		var bancosDTO = new BancosDTO();
		UtilsObjects.copiarPropiedades(t, bancosDTO);
		return bancosDTO;
	};
}
