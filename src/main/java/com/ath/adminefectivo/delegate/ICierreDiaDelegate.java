package com.ath.adminefectivo.delegate;

import java.util.Date;

/**
 * Delegate encargado de la logica del proceso de cierre de dia
 *
 * @author CamiloBenavides
 */
public interface ICierreDiaDelegate {


	/**
	 * Proceso encargado de realizar el cierre del dia, calcular el siguiente dia habil.
	 * 
	 * @return Date
	 * @author CamiloBenavides
	 */
	Date cerrarDia();
}
