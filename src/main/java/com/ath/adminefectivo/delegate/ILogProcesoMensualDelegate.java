package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.LogProcesoMensualDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a los bancos
 * @author cesar.castano
 */
public interface ILogProcesoMensualDelegate {

	/**
	 * Delegate encargado de retornar la lista de todas los procesos mensuales
	 * 
	 * @return List<LogProcesoMensualDTO>
	 * @author duvan.naranjo
	 */
	List<LogProcesoMensualDTO> getLogsProcesosMensuales(Predicate predicate);

	/**
	 * Delegate encargado de realizar el cierre mensual del proceso
	 * recibido como parametro
	 * 
	 * @param proceso
	 * @return String
	 * @author prv_dnaranjo
	 */
	String cerrarLogProcesoMensual(String proceso);

}
