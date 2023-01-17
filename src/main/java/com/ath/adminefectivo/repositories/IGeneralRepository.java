package com.ath.adminefectivo.repositories;

/**
 * Repository encargado de manejar la lógica de las consultas dinámicas
 *
 * @author CamiloBenavides
 */

public interface IGeneralRepository{
	
	/**
	 * Método de ejecución de una consulta nativa dinamica, sin parametros
	 * 
	 * @param query
	 * @return boolean
	 * @author CamiloBenavides
	 */
	public boolean ejecutarQueryNativa(String consulta);
	
	/**
	 * Método de ejecución de una consulta nativa dinamica con un solo parametro de entrada
	 * 
	 * @param consulta
	 * @param parametro
	 * @return boolean
	 * @author CamiloBenavides
	 */
	public boolean ejecutarQueryNativa(String consulta, String parametro);
}
