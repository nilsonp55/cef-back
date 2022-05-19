package com.ath.adminefectivo.dto.compuestos;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la informacion de los errores de los campos de archivo validado
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultadoReglaDTO {

	private Integer idRegla;
	
	private String parametro;
	
	private boolean resultado;
}
