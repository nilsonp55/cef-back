package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.MenuRol;
import com.querydsl.core.types.Predicate;

public interface IMenuRolService {

	/**
	 * Servicio encargado de consultar la lista de todos los menuRol 
	 * @param predicate
	 * @return List<ParametroDTO>
	 * @author bayron.perez
	 */
	List<MenuRol> getMenuRol(Predicate predicate);
	
	/**
	 * Metodo encargado de obtener un menuRol por su ID
	 * @param idMenuRol
	 * @return
	 * @author bayron.perez
	 */
	MenuRol getMenuRolById(Integer idMenuRol);
	
	/**
	 * Metodo encargado de guardar un menuRol 
	 * @param idMenuRol
	 * @return
	 * @author bayron.perez
	 */
	MenuRol postMenuRol(MenuRol menuRol);
	
	/**
	 * Metodo encargado de actualizar un menuRol 
	 * @param idMenuRol
	 * @return
	 * @author bayron.perez
	 */
	MenuRol putMenuRol(MenuRol menuRol);
	
	/**
	 * Metodo encargado de eliminar un menuRol 
	 * @param idMenuRol
	 * @return
	 * @author bayron.perez
	 */
	void deleteMenuRol(Integer idMenuRol);

}
