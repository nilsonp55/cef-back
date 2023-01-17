package com.ath.adminefectivo.dto.compuestos;

import java.io.Serializable;

import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la informacion del resultado de la consulta de operaciones
 * programadas para una transaccion interna intradia
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperacionIntradiaDTO implements Serializable {

	private Integer bancoAVAL;
	
	private Integer codigoPunto;
	
	private String entradaSalida;
	
//	private BancosDTO banco;
//	
//	private OperacionesProgramadasDTO operacionProgramada;
	
	
	
}
