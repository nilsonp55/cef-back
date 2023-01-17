package com.ath.adminefectivo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO del resumen de conciliaciones
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumenConciliacionesDTO {

	private Integer conciliadas;
	
	private Integer programadasNoConciliadas;
	
	private Integer certificadasNoConciliadas;
	
	private Integer certificadasNoConciliables;
	
	private Integer conciliacionesFallidas;
	
	private Integer conciliacionesCanceladas;
	
	private Integer conciliacionesPospuestas;
	
	private Integer conciliacionesFueraConciliacion;
	
}
