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
	List<LogProcesoDiarioDTO> getLogsProcesosDiarios(Predicate predicate);
	
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

	/**
	 * Metodo encargado de obtener un log proceso diario con la fecha de sistema y codigo 
	 * del proceso
	 * 
	 * @param codigoProceso
	 * @param fechaProceso
	 * @return LogProcesoDiarioDTO
	 * @author duvan.naranjo
	 */
	LogProcesoDiarioDTO obtenerEntidadLogProcesoDiarioByCodigoAndFecha(String codigoProceso, Date fechaProceso);
	
	/**
	 * Consulta LogProcesoDiario de la fecha proceso vigente y/o activa	
	 * @return Lista de Log Proceo Diario
	 * @author prv_nparra
	 */
	List<LogProcesoDiarioDTO> getFechaProcesoVigente();
	
	/**
	 * Consulta LogProcesoDiario agrupado por fechaCreacion, retornando lista de fechas generadas y procesadas 	
	 * @return Lista de Log Proceo Diario 
	 * @author prv_nparra
	 */
	List<Date> getLogProcesoDiarioFechasProcesadas();

}
