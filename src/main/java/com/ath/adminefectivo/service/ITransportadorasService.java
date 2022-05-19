package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.TransportadorasDTO;
import com.querydsl.core.types.Predicate;

public interface ITransportadorasService {

	/**
	 * Servicio encargado de consultar la lista de todos las Transportadoras filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<TransportadorasDTO>
	 * @author cesar.castano
	 */
	List<TransportadorasDTO> getTransportadoras(Predicate predicate);

	/**
	 * Servicio encargado de consultar el nombre de la Transportadora por codigo Este
	 * servicio lanza un error en caso de que la ciudad no exista
	 * 
	 * @param codigo
	 * @return String
	 * @author cesar.castano
	 */
	String getNombreTransportadora(String codigo);
}
