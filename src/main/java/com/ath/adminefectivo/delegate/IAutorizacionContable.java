package com.ath.adminefectivo.delegate;

import java.util.Date;

import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;

public interface IAutorizacionContable {
	
	/**
	 * Delegate encargado de generar la autorizacion contable AM / PM
	 * 
	 * @param fecha
	 * * @param tipoContabilidad
	 * * @param estado
	 * @author miller.caro
	 */
	
	LogProcesoDiarioDTO autorizacionContable(Date fecha,String tipoContabilidad,String estado);
}
