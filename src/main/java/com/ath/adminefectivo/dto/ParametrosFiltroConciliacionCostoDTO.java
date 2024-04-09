package com.ath.adminefectivo.dto;

import java.util.Date;

import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de de los filtros pasados por parametros para la 
 * consulta de las conciliacion de costos
 *
 * @author hector.mercado
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParametrosFiltroConciliacionCostoDTO {

	private String entidad; 
	private Date fechaServicioTransporte;
	private Date fechaServicioTransporteFinal;
	private String identificacionCliente; 
	private String razonSocial; 
	private Integer codigoPuntoCargo; 
	private String nombrePuntoCargo;
	private String ciudadFondo; 
	private String nombreTipoServicio; 
	private String monedaDivisa; 
	private String estado; 
	private Pageable page;
	
}
