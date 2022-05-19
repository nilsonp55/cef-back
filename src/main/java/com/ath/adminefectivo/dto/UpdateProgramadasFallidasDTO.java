package com.ath.adminefectivo.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del DTO request para la actualizacion de Operaciones Programadas Fallidas
 * @author cesar.castano
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProgramadasFallidasDTO {

	@NotNull(message = "(Conciliacion.idOperacion)")
	private Integer idOperacion;
	
	private String estado;
	
	private Double valor;
}
