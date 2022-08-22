package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.TiposCuentasDTO;
import com.querydsl.core.types.Predicate;

/**
 * Servicios para gestionar los tipos de cuentas
 * @author Bayron Perez
 */

public interface ITiposCuentasService {
	
	/**
	 * Servicio para obtener los tiposCuentas totales
	 * @return: TiposCuentas
	 */
	List<TiposCuentasDTO> getAllTiposCuentas(Predicate predicate);
	
	/**
	 * Servicio para obtner el tiposCuentas por su identificador
	 * @param idTipoCuentas
	 * @return
	 */
	TiposCuentasDTO getTiposCuentasById(String idTipoCuentas);
	
	/**
	 * Servicio para persistir un tiposCuentas
	 * @param tiposCuentas
	 * @return
	 */
	TiposCuentasDTO saveTiposCuentas(TiposCuentasDTO tiposCuentasDTO);

	/**
	 * Servicio para actualizar un tiposCuentas
	 * @param tiposCuentas
	 * @return
	 */
	TiposCuentasDTO putTiposCuentas(TiposCuentasDTO tiposCuentasDTO);

	/**
	 * Servicio para eliminar el tiposCuentas por su id
	 * @param idTipoCuentas
	 */
	void deleteTiposCuentasById(TiposCuentasDTO tiposCuentasDTO);

}
