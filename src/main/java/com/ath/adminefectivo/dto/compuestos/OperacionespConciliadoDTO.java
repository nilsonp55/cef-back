package com.ath.adminefectivo.dto.compuestos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del DetalleOperacionesDTO
 * @author duvan.naranjo
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperacionespConciliadoDTO {

	private Integer codigoFondoTDV;

	private Integer codigoPuntoOrigen;

	private Integer codigoPuntoDestino;

	private Date fechaOrigen;

	private Date fechaDestino;

	private String tipoOperacion;

	private Double valorTotal;

	private String estadoConciliacion;
	
	private String entradaSalida;

}