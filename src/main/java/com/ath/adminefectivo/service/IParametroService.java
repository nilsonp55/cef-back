package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.ParametroDTO;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de los servicios de la tabla Parametro
 *
 * @author CamiloBenavides
 */
public interface IParametroService {

	/**
	 * Servicio encargado de consultar la lista de todos los Parametros filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<ParametroDTO>
	 * @author CamiloBenavides
	 */
	List<ParametroDTO> getParametros(Predicate predicate);

	/**
	 * Servicio encargado de consultar el valor de un Parametro por codigo Este
	 * servicio lanza un error en caso de que el parametro no exista
	 * 
	 * @param codigo
	 * @return String
	 * @author CamiloBenavides
	 */
	String valorParametro(String codigo);

	/**
	 * Servicio encargado de consultar el valor entero de un Parametro por codigo.
	 * Este servicio lanza un error en caso de que el parametro no exista o no sea
	 * un entero
	 * 
	 * @param codigo
	 * @return int
	 * @author CamiloBenavides
	 */
	int valorParametroEntero(String codigo);

	/**
	 * Servicio encargado de consultar el valor Date de un Parametro por codigo.
	 * Este servicio lanza un error en caso de que el parámetro no exista o no sea
	 * un date
	 * 
	 * @param codigo
	 * @return Date
	 * @author CamiloBenavides
	 */
	Date valorParametroDate(String codigo);


	/**
	 *  Actualiza el valor de un parámetro
	 *  
	 * @param codigo
	 * @param valor
	 * @return boolean
	 * @author CamiloBenavides
	 */
	boolean actualizarValorParametro(String codigo, String valor);

}
