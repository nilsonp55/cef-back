package com.ath.adminefectivo.service;

import com.ath.adminefectivo.dto.FechasConciliacionDTO;

/**
 * @author cesar.castano
 */
public interface IOperacionesCertificadasService {

	/**
	 * Servicio encargado de actualizar el estado en la tabla de Operaciones
	 * Certificadas por id
	 * 
	 * @param idCertificacion
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean actualizarEstadoEnCertificadas(Integer idCertificacion, String estado);

	/**
	 * Servicio encargado de generar el numero de operaciones certificadas no
	 * conciliadas por rango de fechas
	 * @return Integer
	 * @author cesar.castano
	 * @param fechaConciliacion
	 * @param estado
	 */
	Integer numeroOperacionesPorEstadoyFecha(FechasConciliacionDTO fechaConciliacion, String estado);
}
