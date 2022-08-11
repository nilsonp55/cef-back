package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.TiposCentrosCostos;

/**
 * Servicios para gestionar los TiposCentrosCostosService
 * @author Bayron Perez
 */

public interface ITiposCentrosCostosService {

	/**
	 * Servicio para obtener los tiposCentrosCostos totales
	 * @return: TiposCentrosCostos
	 */
	List<TiposCentrosCostos> getAllTiposCentrosCostos();
	
	/**
	 * Servicio para obtner el tiposCentrosCostos por su identificador
	 * @param idTiposCentrosCostos
	 * @return
	 */
	TiposCentrosCostos getTiposCentrosCostosById(String idTiposCentrosCostos);
	
	/**
	 * Servicio para persistir un tiposCentrosCostos
	 * @param tiposCentrosCostos
	 * @return
	 */
	TiposCentrosCostos saveTiposCentrosCostosById(TiposCentrosCostos tiposCentrosCostos);

	/**
	 * Servicio para eliminar el tiposCentrosCostos por su id
	 * @param idTiposCentrosCostos
	 */
	void deleteTiposCentrosCostosById(String idTiposCentrosCostos);
	
}
