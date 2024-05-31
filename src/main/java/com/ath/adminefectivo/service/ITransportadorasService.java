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
	 * servicio lanza un error en caso de que la transportadora no exista
	 * 
	 * @param codigo
	 * @return String
	 * @author cesar.castano
	 */
	String getNombreTransportadora(String codigo);
	
	/**
	 * Servicio encargado de consultar el codigo de la Transportadora por nombre Este
	 * servicio lanza un error en caso de que la transportadora no exista
	 * 
	 * @param nombre
	 * @return String
	 * @author cesar.castano
	 */
	String getcodigoTransportadora(String nombre);
	
	/**
	 * Servicio encargado de consultar la Transportadora por codigo Este
	 * servicio retorna un exepcion en caso no de existir
	 * 
	 * @param codigo 
	 * @return TransportadorasDTO
	 * @author duvan.naranjo
	 */
	TransportadorasDTO getTransportadoraPorCodigo(String codigo);
}
