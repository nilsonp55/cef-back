package com.ath.adminefectivo.service;

import java.util.List;

/**
 * 
 * @author cesar.castano
 *
 */
public interface IDesconciliacionOperacionesService {

	/**
	 * Servicio encargado de consultar todas las operaciones Conciliadas
	 * @return Boolean
	 * @param List<Integer>
	 * @author cesar.castano
	 */
	Boolean procesoDesconciliacion(List<Integer> operacionesADesconciliar);
}
