package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.CiudadesDTO;
import com.querydsl.core.types.Predicate;

public interface ICiudadesService {

	/**
	 * Servicio encargado de consultar la lista de todos las Ciudades filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<CiudadesDTO>
	 * @author cesar.castano
	 */
	List<CiudadesDTO> getCiudades(Predicate predicate);

	/**
	 * Servicio encargado de consultar el nombre de una Ciudad por codigo Este
	 * servicio lanza un error en caso de que la ciudad no exista
	 * 
	 * @param codigo
	 * @return String
	 * @author cesar.castano
	 */
	String getNombreCiudad(String codigo);
}
