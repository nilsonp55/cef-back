package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.DominioDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que contienen los metodos de la entidad dominio
 *
 * @author CamiloBenavides
 */
public interface IDominioDelegate {
	
	/**
	 * Metodo que retonar la lista de dominios, consultados por predicado
	 * 
	 * @param predicate
	 * @return List<DominioDTO>
	 * @author CamiloBenavides
	 */
	public List<DominioDTO> getDominios(Predicate predicate);


	/**
	 * Método encargado de consultar el valor texto de un dominio, consultado por dominio y código 
	 * 
	 * @param dominio
	 * @param codigo
	 * @return String
	 * @author CamiloBenavides
	 */
	public String valorTextoDominio(String dominio, String codigo);
	
	/**
	 * Método encargado de consultar el valor numerico de un dominio, consultado por dominio y código
	 * 
	 * @param dominio
	 * @param codigo
	 * @return Double
	 * @author CamiloBenavides
	 */
	public Double valorNumericoDominio(String dominio, String codigo);
}
