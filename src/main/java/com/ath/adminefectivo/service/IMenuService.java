package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.MenuDTO;
import com.querydsl.core.types.Predicate;

public interface IMenuService {
	
	/**
	 * Consultar la lista de todos los menu filtrando por condicion del parametro Predicate
	 * @param predicate condiciones para buscar registros
	 * @return List<MenuDTO> Lista de Menus coincidentes con el criterio de Predicate
	 * @author nilsonparra
	 */
	List<MenuDTO> getMenuByPredicate(Predicate predicate);

}
