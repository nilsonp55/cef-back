package com.ath.adminefectivo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO la creacion de un punto
 *
 * @author Bayron Andres Perez M.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePuntosDTO {

	private Integer codigoPunto;
	private Boolean fajado;
	private Boolean refagillado;
	private Double tarifaRuteo;
	private Double tarifaVerificacion;
	private Integer bancoAVAL;


	//Punto
	private String tipoPunto;
	private String nombrePunto;
	private String codigoCiudad;

	//Bancos
	private Integer codigoCompensacion;
	private String numeroNit;
	private String abreviatura;
	private Boolean esAVAL;

	//Oficinas
	private Integer codigoOficina;

	//Ciudades
	private String codigoDANE;
	private String nombreCiudad;
	
	//Sitios clientes	
	private Integer codigoCliente;
	
	//Puntos Codigo
	private String codigoTDV;
	private String codigoPropioTDV;
	
	//Fondos
	private String tdv;
	private String nombreFondo;
	
	//Cajeros
	private Integer codigoATM;
	
	
}
