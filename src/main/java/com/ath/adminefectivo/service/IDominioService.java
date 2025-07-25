package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.DominioDTO;
import com.ath.adminefectivo.entities.id.DominioPK;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de los servicios a la tabla de dominio
 *
 * @author CamiloBenavides
 */
public interface IDominioService {

	/**
	 * Servicio encargado de consultar la lista de todos los Dominios con el
	 * predicado
	 * 
	 * @return List<DominioDTO>
	 * @author CamiloBenavides
	 */
	List<DominioDTO> getDominios(Predicate predicate);

	/**
	 * Servicio encargado de consultar el valor texto de un dominio por codigo y
	 * dominio
	 * 
	 * @param dominio
	 * @param codigo
	 * @return String
	 * @author CamiloBenavides
	 */
	String valorTextoDominio(String dominio, String codigo);

	/**
	 * Servicio encargado de consultar el valor numérico de un dominio por codigo y
	 * dominio
	 * 
	 * @param dominio
	 * @param codigo
	 * @return String
	 * @author CamiloBenavides
	 */
	Double valorNumericoDominio(String dominio, String codigo);

	/**
	 * Retorna la lista de valores texto consultados por un dominio, en caso de no
	 * encontrar ninguno retonar una lista vacia
	 * 
	 * @param dominio
	 * @return List<String>
	 * @author CamiloBenavides
	 */
	List<String> consultaListValoresPorDominio(String dominio);

	
	/**
	 * Retorna la lista de valores numericos consultados por un dominio, en caso de no
	 * encontrar ninguno retonar una lista vacia
	 * 
	 * @param dominio
	 * @return List<String>
	 * @author CamiloBenavides
	 */
	List<Double> consultaListValoresNumPorDominio(String dominio);

	/**
	 * Crear un nuevo registro en tabla dominio
	 * 
	 * @param dominioDto
	 * @return dominioDto
	 * @author duvan.naranjo
	 * @author prv_nparra
	 */
	DominioDTO crearDominio(DominioDTO dominioDto);
	
	/**
     * Actualizar un registro en la tabla dominio
     * 
     * @param dominioDto
     * @return dominioDto
     * @author prv_nparra
     */
	DominioDTO actualizarDominio(DominioDTO dominioDto);
	
	/**
	 * Metodo encargado de realizar la eliminacion 
	 * @param dominioPK
	 * @return
	 */
	Boolean eliminarDominio(DominioPK dominioPK);
	

}
