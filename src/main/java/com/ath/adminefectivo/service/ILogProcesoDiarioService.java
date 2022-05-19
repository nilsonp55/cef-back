package com.ath.adminefectivo.service;

import java.util.Date;

/**
 * Interfaz de los servicios referentes a los LogProcesoDiario
 *
 * @author CamiloBenavides
 */
public interface ILogProcesoDiarioService {

	/**
	 * Servicio encargado de consultar si se completaron todos los procesos del día
	 * 
	 * @param diaCierre
	 * @return boolean
	 * @author CamiloBenavides
	 */
	boolean esDiaCompleto(Date diaCierre);

}
