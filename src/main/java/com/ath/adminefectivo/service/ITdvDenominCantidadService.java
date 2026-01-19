package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.TdvDenominCantidadDTO;
import com.querydsl.core.types.Predicate;

public interface ITdvDenominCantidadService {

	/**
	 * Servicio encargado de contener la logica para listar los
	 * las TdvDenominCantidad
	 * 
	 * @param predicate
	 * @return List<TdvDenominCantidadDTO>
	 * @author duvan.naranjo
	 */
	List<TdvDenominCantidadDTO> getTdvDenominCantidad(Predicate predicate);

	/**
	 * Servicio encargado de contener la logica para consultar
	 * una TdvDenominCantidad
	 * 
	 * @param idTdvDenominCantidad
	 * @return TdvDenominCantidadDTO
	 * @author duvan.naranjo
	 */
	TdvDenominCantidadDTO getTdvDenominCantidadById(Long idTdvDenominCantidad);

	/**
	 * Servicio encargado de contener la logica guardar o persistir
	 * una TdvDenominCantidad
	 * 
	 * @param TdvDenominCantidadDTO
	 * @return TdvDenominCantidadDTO
	 * @author duvan.naranjo
	 */
	TdvDenominCantidadDTO guardarTdvDenominCantidad(TdvDenominCantidadDTO tdvDenominCantidadDTO);

	/**
	 * Servicio encargado de contener la logica para actualizar una TdvDenominCantidad
	 * 
	 * @param TdvDenominCantidadDTO
	 * @return TdvDenominCantidadDTO
	 * @author duvan.naranjo
	 */
	TdvDenominCantidadDTO actualizarTdvDenominCantidad(TdvDenominCantidadDTO tdvDenominCantidadDTO);

	/**
	 * Servicio encargado de contener la logica eliminar una TdvDenominCantidad
	 * 
	 * @param idTdvDenominCantidad
	 * @return boolean
	 * @author duvan.naranjo
	 */
	boolean eliminarTdvDenominCantidad(Long idTdvDenominCantidad);

	

}
