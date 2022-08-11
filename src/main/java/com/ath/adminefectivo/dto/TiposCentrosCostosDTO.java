package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.TiposCentrosCostos;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO para la entidad TiposCentrosCostosDTO
 * @author BayronPerez
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TiposCentrosCostosDTO {

private String tipoCentro;
	
	private String nombreCentro;
	
	private String codigoCentro;
	
	private String tablaCentros;
	
	
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<TiposCentrosCostosDTO, TiposCentrosCostos> CONVERTER_ENTITY = (TiposCentrosCostosDTO t) -> {
		TiposCentrosCostos tiposCentrosCostos = new TiposCentrosCostos();
		UtilsObjects.copiarPropiedades(t, tiposCentrosCostos);
		return tiposCentrosCostos;
	};

	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<TiposCentrosCostos, TiposCentrosCostosDTO> CONVERTER_DTO = (TiposCentrosCostos t) -> {
		TiposCentrosCostosDTO tiposCentrosCostosDTO = new TiposCentrosCostosDTO();
		UtilsObjects.copiarPropiedades(t, tiposCentrosCostosDTO);
		return tiposCentrosCostosDTO;
	};
	
}
