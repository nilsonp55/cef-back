package com.ath.adminefectivo.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de errores contables
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErroresContablesConsultaDTO {

	private Integer idErroresContables;
	
	private Long idTransaccionInterna;
	
	private Date fecha;
	
	private String mensajeError;
	
	private int estado;
}
