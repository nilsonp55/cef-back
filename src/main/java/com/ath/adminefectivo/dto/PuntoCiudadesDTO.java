package com.ath.adminefectivo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de las tablas Ciudades y Puntos
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuntoCiudadesDTO {

	private String codigoDane;
	
	private String nombreCiudad;
	
	private String codigoNombreCiudad;
	
	private Integer codigoPunto;
	
	private String nombrePunto;
	
	private String puntosCliente;

}
