package com.ath.adminefectivo.service;

import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.ParametrosConciliacionManualDTO;

/**
 * @author cesar.castano
 */
public interface IConciliacionServiciosService {

	/**
	 * Servicio encargado de eliminar conciliacion en la tabla de Conciliacion Servicios
	 * @return Boolean
	 * @param Integer
	 * @author cesar.castano
	 */
	Boolean eliminarRegistroConciliacion(Integer idConciliacion);
	
	/**
	 * Servicio encargado de crear el registro en la tabla de Conciliacion Servicios
	 * @param elemento
	 * @return Boolean
	 */
	Boolean crearRegistroConciliacion(ParametrosConciliacionManualDTO elemento);
	
	/**
	 * Servicio encargado de generar el numero de servicios concilliados por rango de fechas
	 * @param fechaConciliacion
	 * @return Integer
	 * @author cesar.castano
	 */
	Integer numeroOperacionesPorRangoFechas(FechasConciliacionDTO fechaConciliacion);
	
}
