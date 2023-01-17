package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.RolDTO;

public interface IRolDelegate {

	/**
	 * Delegate encargado de retornar la lista de todos los Roles
	 * 
	 * @return List<RolDTO>
	 * @author CamiloBenavides
	 */
	List<RolDTO> getRoles();
}
