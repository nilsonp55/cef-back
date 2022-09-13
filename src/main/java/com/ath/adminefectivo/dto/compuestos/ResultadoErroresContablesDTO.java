package com.ath.adminefectivo.dto.compuestos;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.ErroresContablesDTO;
import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO encargado de retornar al front de contabilidad la informacion de los errores
 * contables actuales
 * 
 * @author prv_dnaranjo
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoErroresContablesDTO {
	
	private String abreviaturaBancoAval;
	
	private String abreviaturaTransportadora;
	
	private Date fecha;
	
	private String tipoTransaccion;
	
	private String tipoOperacion;
	
	private String tipoProceso;
	
	private String nombrePuntoOrigen;
	
	private String nombrePuntoDestino;
	
	private String codigoComision;
	
	private String codigoImpuesto;
	
	private int valor;
	
	private String mensajeError;
	
}
