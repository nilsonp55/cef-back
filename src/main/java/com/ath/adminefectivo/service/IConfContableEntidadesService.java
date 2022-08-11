package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.ConfContableEntidades;

/**
 * Servicios para gestionar los tipos de cuentas
 * @author Bayron Perez
 */

public interface IConfContableEntidadesService {

	/**
	 * Servicio para obtener los confContableEntidades totales
	 * @return: ConfContableEntidades
	 */
	List<ConfContableEntidades> getAllConfContableEntidades();
	
	/**
	 * Servicio para obtner el confContableEntidades por su identificador
	 * @param idContableEntidadesService
	 * @return
	 */
	ConfContableEntidades getConfContableEntidadesById(String idContableEntidadesService);
	
	/**
	 * Servicio para persistir un confContableEntidades
	 * @param confContableEntidades
	 * @return
	 */
	ConfContableEntidades saveConfContableEntidadesById(ConfContableEntidades confContableEntidades);

	/**
	 * Servicio para eliminar el confContableEntidades por su id
	 * @param idContableEntidadesService
	 */
	void deleteConfContableEntidadesById(String idContableEntidadesService);
	
}
