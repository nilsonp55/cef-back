package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de los servicios referentes a los LogProcesoDiario
 *
 * @author CamiloBenavides
 */
public interface ILogProcesoDiarioService {

	/**
	 * Servicio encargado de traer todos los procesos diarios
	 * @param predicate
	 * @return
	 */
	List<LogProcesoDiarioDTO> getLogsProcesosDierios(Predicate predicate);
	
	/**
	 * Servicio encargado de consultar si se completaron todos los procesos del día
	 * 
	 * @param diaCierre
	 * @return boolean
	 * @author CamiloBenavides
	 */
	boolean esDiaCompleto(Date diaCierre);

}
