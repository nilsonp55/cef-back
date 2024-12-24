package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.CajerosDTO;
import com.querydsl.core.types.Predicate;

public interface ICajerosService {

	/**
	 * Servicio encargado de consultar la lista de todos los Cajeros filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<CajerosDTO>
	 * @author cesar.castano
	 */
	List<CajerosDTO> getCajeros(Predicate predicate);

	/**
	 * Servicio encargado de consultar el codigo punto del cajero
	 * 
	 * @param codigoCajero
	 * @return Integer
	 * @author cesar.castano
	 */
	Integer getCodigoPunto(String codigoCajero);

	/**
	 * Servicio encargado de obtener el objeto Cajero con base en el codigoPunto
	 * 
	 * @param codigoPunto
	 * @return Integer
	 * @author cesar.castano
	 */
	Boolean getCodigoPuntoCajero(Integer codigoPunto);

	/**
	 * Servicio encargado de obtener el objeto Cajero con base en el codigoPunto
	 * Este servicio consume una optimizacion mediante JDBC
	 * 
	 * @param codigoPunto
	 * @return Integer
	 */
	Boolean getCodigoPuntoCajeroJdbc(Integer codigoPunto);
}
