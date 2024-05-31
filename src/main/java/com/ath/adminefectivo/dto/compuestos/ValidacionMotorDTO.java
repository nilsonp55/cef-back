package com.ath.adminefectivo.dto.compuestos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO con el contenido del resultao de la validacion del motor
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidacionMotorDTO {

	private boolean isValida;	

	private List<Integer> codigosError;
	
	private List<String> mensajesError;	
	

}
