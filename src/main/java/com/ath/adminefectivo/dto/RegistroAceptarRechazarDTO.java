package com.ath.adminefectivo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura de los archivos Liquidación con 
 * el campo idLiquidacion para aceptar o rechazar
 * @author jose.pabon
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistroAceptarRechazarDTO {

	    private Long idRegistro;

	    private String operacionEstado;

	    private String observacion;

		private Long idLiquidacion;

}