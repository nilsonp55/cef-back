package com.ath.adminefectivo.dto.compuestos;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la informacion del proceso de liquidacion mensual de costos 
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CostosMensualesClasificacionDTO {

	private int codigoBanco;
	
	private String nombreBanco;
	
	private int cantidadEstimadaFajos;
	
	private int cantidadAsignadaFajos;
	
	private long valorLiquidadoFajos;
	
	private int cantidadEstimadaRem;
	
	private int cantidadAsignadaRem;
	
	private long valorLiquidadoRem;

	private int cantidadEstimadaBolsas;
	
	private int cantidadAsignadaBolsas;
	
	private long valorLiquidadoBolsas;
	
	private long valorTotalLiquidacion;

	private String mesAnio;
	
	private Date fechaSistema;
	
	
}
