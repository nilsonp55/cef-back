package com.ath.adminefectivo.service;

import java.util.List;
import com.ath.adminefectivo.dto.RolDTO;

/**
 * Interfaz de los servicios referentes a los Roles
 *
 * @author CamiloBenavides
 */
public interface IRolService {

	/**
	 * Servicio encargado de consultar la lista de todos los roles
	 * 
	 * @return List<RolDTO>
	 * @author CamiloBenavides
	 */
	List<RolDTO> getRoles();
	
	/**
	 * Insertar nuevo registro en tabla Rol
	 * @param rol DTO con los atributos a insertar
	 * @return DTO de rol creado
	 * @author prv_nparra
	 */
	RolDTO createRoles(RolDTO rol);
	
	/**
	 * Actualiza un registro existen en la tabla Rol
	 * @param rol DTO con los atributos a actualizar
	 * @return DTO de rol actualizado
	 * @author prv_nparra
	 */
	RolDTO updateRoles(RolDTO rol);
	
	/**
	 * Eliminar un registro de la tabla DTO
	 * @param idRol identificador del registro en la tabla Rol
	 * @author prv_nparra
	 */
	void deleteRoles(String idRol);

}
