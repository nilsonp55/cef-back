package com.ath.adminefectivo.dto.compuestos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO con el contenido de las lineas del archivo validado
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidacionLineasDTO {

	private int numeroLinea;	

	private String contenidoTxt;
	
	private String estado;
	
	private Integer tipo;
	
	private List<String> contenido;	
	
	private List<ErroresCamposDTO> campos;

}
