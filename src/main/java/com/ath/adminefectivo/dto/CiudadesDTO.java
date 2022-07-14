package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de ciudades
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CiudadesDTO {

	private String codigoDANE;
	
	private String nombreCiudad;
	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<Ciudades, CiudadesDTO> CONVERTER_DTO = (Ciudades t) -> {
		var ciudadesDTO = new CiudadesDTO();
		UtilsObjects.copiarPropiedades(t, ciudadesDTO);
		return ciudadesDTO;
	};
	
	public static final Function<CiudadesDTO, Ciudades> CONVERTER_ENTITY = (CiudadesDTO t) -> {
		var ciudades = new Ciudades();
		UtilsObjects.copiarPropiedades(t, ciudades);
		return ciudades;
	};
}
