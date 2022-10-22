package com.ath.adminefectivo.dto.compuestos;

import java.util.ArrayList;
import java.util.List;

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
public class RequestFuncionesDinamicasDTO {

	private Integer idFuncion;
	
	private String parametros;
	
	
}
