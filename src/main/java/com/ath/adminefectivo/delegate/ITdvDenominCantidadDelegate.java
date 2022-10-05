package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.TdvDenominCantidadDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a las funciones dinamicas
 * @author duvan.naranjo
 */
public interface ITdvDenominCantidadDelegate {

	/**
	 * Delegate encargado de contener la union con el servicio para listar 
	 * las TdvDenominCantidad
	 * 
	 * @param idTarifaOperacion
	 * @return PuntosCostosDTO
	 * @author duvan.naranjo
	 */
	List<TdvDenominCantidadDTO> getTdvDenominCantidad(Predicate predicate);
	
	/**
	 * Delegate encargado de contener la union con el servicio para obtener 
	 * una escala por id
	 * 
	 * @param idTarifaOperacion
	 * @return TdvDenominCantidadDTO
	 * @author duvan.naranjo
	 */
	TdvDenominCantidadDTO getTdvDenominCantidadById(Integer idTarifaOperacion);
	

	/**
	 * Delegate encargado de contener la union con el servicio de guardar 
	 * una escala
	 * 
	 * @param idTdvDenominCantidad
	 * @return TdvDenominCantidadDTO
	 * @author duvan.naranjo
	 */
	TdvDenominCantidadDTO guardarTdvDenominCantidad(TdvDenominCantidadDTO tdvDenominCantidadDTO);

	/**
	 * Delegate encargado de contener la union con el servicio para actualizar 
	 * una escala
	 * 
	 * @param idTdvDenominCantidad
	 * @return TdvDenominCantidadDTO
	 * @author duvan.naranjo
	 */
	TdvDenominCantidadDTO actualizarTdvDenominCantidad(TdvDenominCantidadDTO tdvDenominCantidadDTO);

	/**
	 * Delegate encargado de contener la union con el servicio para eliminar 
	 * una escala
	 * 
	 * @param idTdvDenominCantidad
	 * @return TdvDenominCantidadDTO
	 * @author duvan.naranjo
	 */
	boolean eliminarTdvDenominCantidad(Integer idTdvDenominCantidad);



}
