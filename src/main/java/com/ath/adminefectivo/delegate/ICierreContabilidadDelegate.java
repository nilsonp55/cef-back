package com.ath.adminefectivo.delegate;

import java.sql.Date;
import java.util.List;

import com.ath.adminefectivo.dto.RespuestaContableDTO;

public interface ICierreContabilidadDelegate {

	/**
	 * Delegate encargado de generar la logica para generar el cierre de la  contabilidad AM / PM
	 * 
	 * @param fecha
	 * * @param tipoContabilidad
	 * * @param numeroBancos
	 * * @param codBanco
	 * @return String
	 * @author miller.caro
	 */
	List<RespuestaContableDTO> cerrarContabilidad(java.util.Date fechaSistema,String tipoContabilidad,String numeroBancos,String codBanco,String fase);
	
}
