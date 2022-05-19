package com.ath.adminefectivo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de la captura de los datos para la conciliacion manual
 *
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParametrosConciliacionManualDTO {

	private Integer idOperacion;
	
	private Integer idCertificacion;
}
