package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.LogProcesoMensualDTO;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de los servicios referentes a los LogProcesoMensual
 *
 * @author duvan.naranjo
 */
public interface ILogProcesoMensualService {

	/**
	 * Servicio encargado de traer todos los procesos mensuales
	 * @param predicate
	 * @return List<LogProcesoMensualDTO>
	 * @author prv_dnaranjo
	 */
	List<LogProcesoMensualDTO> getLogsProcesosMensual(Predicate predicate);

	/**
	 * Servicio encargado de traer los log procesosmensuales por un codigo proceso 
	 * que se encuentre en estado pendiente
	 * 
	 * @param codigoProceso
	 * @return List<LogProcesoMensualDTO>
	 * @author prv_dnaranjo
	 */
	LogProcesoMensualDTO getLogsProcesosMensualByCodigoProcesoAndPendiente(String codigoProceso);

	/**
	 * Servicio encargado de realizar validaciones para permitir el cierre
	 * del proceso
	 * 
	 * @param logProcesoMensual
	 * @return boolean
	 * @author prv_dnaranjo
	 */
	boolean validarLogProceso(LogProcesoMensualDTO logProcesoMensual);
	

}
