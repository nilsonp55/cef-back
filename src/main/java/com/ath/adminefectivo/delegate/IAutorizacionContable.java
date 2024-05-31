package com.ath.adminefectivo.delegate;

public interface IAutorizacionContable {
	
	/**
	 * Delegate encargado de generar la autorizacion contable AM / PM
	 * 
	 * @param fecha
	 * * @param tipoContabilidad
	 * * @param estado
	 * @author miller.caro
	 */
	
	String autorizacionContable(String tipoContabilidad,String estado);
}
