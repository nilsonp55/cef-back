package com.ath.adminefectivo.delegate;

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
	List<RespuestaContableDTO> cerrarContabilidad(String tipoContabilidad,int codBanco,String fase);
	
}
