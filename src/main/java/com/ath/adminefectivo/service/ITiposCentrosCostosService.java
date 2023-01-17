package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.TiposCentrosCostosDTO;
import com.querydsl.core.types.Predicate;

/**
 * Servicios para gestionar los TiposCentrosCostosService
 * @author Bayron Perez
 */

public interface ITiposCentrosCostosService {

	/**
	 * Servicio para obtener los tiposCentrosCostos totales
	 * @return: TiposCentrosCostos
	 */
	List<TiposCentrosCostosDTO> getAllTiposCentrosCostos(Predicate predicate);
	
	/**
	 * Servicio para obtner el tiposCentrosCostos por su identificador
	 * @param idTiposCentrosCostos
	 * @return
	 */
	TiposCentrosCostosDTO getTiposCentrosCostosById(String idTiposCentrosCostos);
	
	/**
	 * Servicio para persistir un tiposCentrosCostos
	 * @param tiposCentrosCostos
	 * @return
	 */
	TiposCentrosCostosDTO saveTiposCentrosCostos(TiposCentrosCostosDTO tiposCentrosCostos);
	
	/**
	 * Servicio para actualizar un tiposCentrosCostos
	 * @param tiposCentrosCostos
	 * @return
	 */
	TiposCentrosCostosDTO putTiposCentrosCostos(TiposCentrosCostosDTO tiposCentrosCostos);

	/**
	 * Servicio para eliminar el tiposCentrosCostos por su id
	 * @param idTiposCentrosCostos
	 */
	void deleteTiposCentrosCostosById(TiposCentrosCostosDTO tiposCentrosCostos);
	
}
