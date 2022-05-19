package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.FondosDTO;
import com.ath.adminefectivo.entities.Fondos;
import com.querydsl.core.types.Predicate;

public interface IFondosService {

	/**
	 * Servicio encargado de consultar la lista de todos los Fondos filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<FondosDTO>
	 * @author cesar.castano
	 */
	List<FondosDTO> getFondos(Predicate predicate);

	/**
	 * Servicio encargado de consultar los datos del Fondo por codigo Este
	 * servicio lanza un error en caso de que la ciudad no exista
	 * 
	 * @param codigo
	 * @return Fondos
	 * @author cesar.castano
	 */
	Fondos getDatosFondos(String codigo);
}
