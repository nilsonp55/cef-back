package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.PuntosCostosDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a las funciones dinamicas
 * @author duvan.naranjo
 */
public interface IPuntosCostosDelegate {

	/**
	 * Delegate encargado de contener la union con el servicio para listar 
	 * las tarifas operacion
	 * 
	 * @param idTarifaOperacion
	 * @return PuntosCostosDTO
	 * @author duvan.naranjo
	 */
	List<PuntosCostosDTO> getPuntosCostos(Predicate predicate);
	
	/**
	 * Delegate encargado de contener la union con el servicio para obtener 
	 * una tarifa operacion por id
	 * 
	 * @param idTarifaOperacion
	 * @return PuntosCostosDTO
	 * @author duvan.naranjo
	 */
	PuntosCostosDTO getPuntosCostosById(Integer idTarifaOperacion);
	

	/**
	 * Delegate encargado de contener la union con el servicio de guardar 
	 * una tarifa operacion
	 * 
	 * @param idPuntosCostos
	 * @return PuntosCostosDTO
	 * @author duvan.naranjo
	 */
	PuntosCostosDTO guardarPuntosCostos(PuntosCostosDTO puntosCostosDTO);

	/**
	 * Delegate encargado de contener la union con el servicio para actualizar 
	 * una tarifa operacion
	 * 
	 * @param idPuntosCostos
	 * @return PuntosCostosDTO
	 * @author duvan.naranjo
	 */
	PuntosCostosDTO actualizarPuntosCostos(PuntosCostosDTO puntosCostosDTO);

	/**
	 * Delegate encargado de contener la union con el servicio para eliminar 
	 * una tarifa operacion
	 * 
	 * @param idPuntosCostos
	 * @return PuntosCostosDTO
	 * @author duvan.naranjo
	 */
	boolean eliminarPuntosCostos(Integer idPuntosCostos);



}
