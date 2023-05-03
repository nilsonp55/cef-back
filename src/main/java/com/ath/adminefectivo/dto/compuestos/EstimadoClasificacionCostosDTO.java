package com.ath.adminefectivo.dto.compuestos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la informacion de los errores de los campos de archivo validado
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstimadoClasificacionCostosDTO implements Serializable {

	private Integer bancoAval;
	
	private String tdv;
	
	private Long estimadaFajos;

	private Long estimadaBolsas;
		
}
