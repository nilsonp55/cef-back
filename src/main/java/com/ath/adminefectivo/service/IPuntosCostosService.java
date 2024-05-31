package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.PuntosCostosDTO;
import com.querydsl.core.types.Predicate;

public interface IPuntosCostosService {

	/**
	 * Servicio encargado de contener la logica para listar los
	 * los puntos costos
	 * 
	 * @param predicate
	 * @return List<PuntosCostosDTO>
	 * @author duvan.naranjo
	 */
	List<PuntosCostosDTO> getPuntosCostos(Predicate predicate);

	/**
	 * Servicio encargado de contener la logica para consultar
	 * un punto costo
	 * 
	 * @param idTarifaOperacion
	 * @return PuntosCostosDTO
	 * @author duvan.naranjo
	 */
	PuntosCostosDTO getPuntosCostosById(Integer idPuntoCosto);

	/**
	 * Servicio encargado de contener la logica guardar o persistir
	 * un punto costo
	 * 
	 * @param PuntosCostosDTO
	 * @return PuntosCostosDTO
	 * @author duvan.naranjo
	 */
	PuntosCostosDTO guardarPuntosCostos(PuntosCostosDTO puntosCostosDTO);

	/**
	 * Servicio encargado de contener la logica para actualizar un punto costo
	 * 
	 * @param PuntosCostosDTO
	 * @return PuntosCostosDTO
	 * @author duvan.naranjo
	 */
	PuntosCostosDTO actualizarPuntosCostos(PuntosCostosDTO puntosCostosDTO);

	/**
	 * Servicio encargado de contener la logica eliminar un punto costo
	 * 
	 * @param idPuntoCosto
	 * @return boolean
	 * @author duvan.naranjo
	 */
	boolean eliminarPuntosCostos(Integer idPuntoCosto);

	

}
