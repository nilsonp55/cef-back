package com.ath.adminefectivo.dto.compuestos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la informacion del archivo de certificacion registro tipo 1
 *
 * @author cesar.castno
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistroTipo1ArchivosFondosDTO {

	private String tdv;
	
	private Integer codigoPunto;
	
	private Date fechaEjecucion;
	
	private Integer banco_aval;
	
	private String codigoDane;

}
