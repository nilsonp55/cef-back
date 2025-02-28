package com.ath.adminefectivo.service;

import java.util.List;
import com.ath.adminefectivo.dto.MenuDTO;
import com.querydsl.core.types.Predicate;

public interface IMenuService {

  /**
   * Consultar la lista de todos los menu filtrando por condicion del parametro Predicate
   * 
   * @param predicate condiciones para buscar registros
   * @return List<MenuDTO> Lista de Menus coincidentes con el criterio de Predicate
   * @author nilsonparra
   */
  List<MenuDTO> getMenuByPredicate(Predicate predicate);

  /**
   * Crear un nuevo registro en tambla Menu
   * 
   * @param menu objeto con propiedades de la tabla Menu
   * @return DTO con el Id del registro creado en tabla Menu
   * @author nilsonparra
   */
  MenuDTO createMenu(MenuDTO menu);

  /**
   * Actualizar un registro en la tabla Menu
   * 
   * @param menu objeto con propiedades de la tabla Menu
   * @return DTO con el registro modificado en tabla Menu
   * @author nilsonparra
   */
  MenuDTO updateMenu(MenuDTO menu);

  /**
   * Eliminar un registro de la tabla Menu
   * 
   * @param idMenu
   * @author nilsonparra
   */
  void deleteMenu(String idMenu);

}
