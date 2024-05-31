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

}
