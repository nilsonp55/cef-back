package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.ConfContableEntidadesDTO;
import com.querydsl.core.types.Predicate;

/**
 * Servicios para gestionar los tipos de cuentas
 * @author Bayron Perez
 */

public interface IConfContableEntidadesService {

	/**
	 * Servicio para obtener los confContableEntidades totales
	 * @return: ConfContableEntidades
	 */
	List<ConfContableEntidadesDTO> getAllConfContableEntidades(Predicate predicate);
	
	/**
	 * Servicio para obtner el confContableEntidades por su identificador
	 * @param idContableEntidadesService
	 * @return
	 */
	ConfContableEntidadesDTO getConfContableEntidadesById(Long idConfContableEntidades);
	
	/**
	 * Servicio para persistir un confContableEntidades
	 * @param confContableEntidades
	 * @return
	 */
	ConfContableEntidadesDTO saveConfContableEntidades(ConfContableEntidadesDTO confContableEntidadesDTO);

	/**
	 * Servicio para actualizar un confContableEntidades
	 * @param confContableEntidades
	 * @return
	 */
	ConfContableEntidadesDTO putConfContableEntidades(ConfContableEntidadesDTO confContableEntidadesDTO);

	
	/**
	 * Servicio para eliminar el confContableEntidades por su id
	 * @param idContableEntidadesService
	 */
	void deleteConfContableEntidadesById(Long idContableEntidadesService);
	
}
