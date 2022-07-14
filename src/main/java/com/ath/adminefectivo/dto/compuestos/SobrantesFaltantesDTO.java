package com.ath.adminefectivo.dto.compuestos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la informacion de los sobrantes y faltantes
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SobrantesFaltantesDTO {

	private String tipoAjuste;
	
	private String codigoServicio;
	
	private Double valor;
}
