package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.TiposCuentas;

/**
 * Servicios para gestionar los tipos de cuentas
 * @author Bayron Perez
 */

public interface ITiposCuentasService {
	
	/**
	 * Servicio para obtener los tiposCuentas totales
	 * @return: TiposCuentas
	 */
	List<TiposCuentas> getAllTiposCuentas();
	
	/**
	 * Servicio para obtner el tiposCuentas por su identificador
	 * @param idTipoCuentas
	 * @return
	 */
	TiposCuentas getTiposCuentasById(String idTipoCuentas);
	
	/**
	 * Servicio para persistir un tiposCuentas
	 * @param tiposCuentas
	 * @return
	 */
	TiposCuentas saveTiposCuentasById(TiposCuentas tiposCuentas);

	/**
	 * Servicio para eliminar el tiposCuentas por su id
	 * @param idTipoCuentas
	 */
	void deleteTiposCuentasById(String idTipoCuentas);

}
