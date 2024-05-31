package com.ath.adminefectivo.dto.compuestos;

import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la informacion del resultado de la consulta de operaciones
 * programadas para una transaccion interna intradia
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransaccionInternaIntradiaDTO {

	private Integer idBancoAval;
	
	private Integer codigoPunto;
	
	private TransaccionesInternasDTO transaccionesInternasDTO;
	
	
	
}
