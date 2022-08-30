package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.entities.LogProcesoDiario;
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
	 * Servicio encargado de traer todos los procesos diarios
	 * @param predicate
	 * @return
	 */
	List<LogProcesoDiarioDTO> getLogsProcesosDiariosByFechaProceso(Date fechaProceso);
	
	/**
	 * Servicio encargado de consultar si se completaron todos los procesos del día
	 * 
	 * @param diaCierre
	 * @return boolean
	 * @author CamiloBenavides
	 */
	boolean esDiaCompleto(Date diaCierre);
	
	/**
	 * Servicio encargado de consultar log proceso diario por id
	 * 
	 * @param id
	 * @return LogProcesoDiarioDTO
	 * @author cesar.castano
	 */
	LogProcesoDiarioDTO obtenerLogProcesoDiarioById(Long id);

	/**
	 * Servicio encargado de guardar log proceso diario
	 * 
	 * @param logProcesoDiarioDTO
	 * @return LogProcesoDiarioDTO
	 * @author cesar.castano
	 */
	LogProcesoDiarioDTO persistirLogProcesoDiario(LogProcesoDiarioDTO logProcesoDiarioDTO);

	/**
	 * Servicio encargado de actualizar log proceso diario
	 * 
	 * @param logProcesoDiarioDTO
	 * @return LogProcesoDiarioDTO
	 * @author cesar.castano
	 */
	LogProcesoDiarioDTO actualizarLogProcesoDiario(LogProcesoDiarioDTO logProcesoDiarioDTO);

	/**
	 * Servicio encargado de eliminar log proceso diario
	 * 
	 * @param id
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean eliminarLogProcesoDiario(Long id);

	/**
	 * Servicio encargado de obtener el id con base en el codigo de proceso
	 * 
	 * @param codigoProceso
	 * @return LogProcesoDiario
	 * @author cesar.castano
	 */
	LogProcesoDiario obtenerEntidadLogProcesoDiario(String codigoProceso);

}
