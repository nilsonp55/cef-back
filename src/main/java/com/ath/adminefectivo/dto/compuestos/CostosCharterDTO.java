package com.ath.adminefectivo.dto.compuestos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que contiene la estructura del input para actualizar el costo Charter en
 * la tabla de Valores Liquidados
 * @author cesar.castaño
 */

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CostosCharterDTO {

	private Long idLiquidacion;
	
	private Double costosCharter;
}
