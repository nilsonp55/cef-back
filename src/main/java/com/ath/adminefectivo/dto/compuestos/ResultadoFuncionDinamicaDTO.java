package com.ath.adminefectivo.dto.compuestos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO encargado de contener la informacion del del retorno de la funcion dinamica
 * 
 * @author prv_dnaranjo
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoFuncionDinamicaDTO {
		
	private Integer idFuncion;
	
	private Integer consecutivo;
	
	private String resultado;
	

}
