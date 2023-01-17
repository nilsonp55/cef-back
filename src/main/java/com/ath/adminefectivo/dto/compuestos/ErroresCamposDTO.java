package com.ath.adminefectivo.dto.compuestos;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroresCamposDTO {

	private int numeroLinea;
	
	private int numeroCampo;
	
	private String nombreCampo;

	private String contenido;
	
	private String estado;	
	
	private String mensajeErrorTxt;

	private List<String> mensajeError;
	
	
}
