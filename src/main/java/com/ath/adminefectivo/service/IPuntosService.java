package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.entities.Puntos;
import com.querydsl.core.types.Predicate;

public interface IPuntosService {

	/**
	 * Servicio encargado de consultar la lista de todos los Puntos filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<PuntosDTO>
	 * @author cesar.castano
	 */
	List<PuntosDTO> getPuntos(Predicate predicate);

	/**
	 * Servicio encargado de consultar el objeto Punto por codigo y tipo. Este
	 * servicio lanza un error en caso de que la ciudad no exista
	 * 
	 * @param codigoPunto
	 * @param tipoPunto
	 * @return Puntos
	 * @author cesar.castano
	 */
	Puntos getEntidadPunto(String tipoPunto, Integer codigoPunto);
	
	/**
	 * Servicio encargado de consultar el nombre del Punto por codigo y tipo. Este
	 * servicio lanza un error en caso de que el punto no exista
	 * 
	 * @param codigoPunto
	 * @param tipoPunto
	 * @return String
	 * @author cesar.castano
	 */
	String getNombrePunto(String tipoPunto, Integer codigoPunto);
}
