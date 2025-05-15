package com.ath.adminefectivo.repositories;

import java.util.List;
import java.util.Map;

import com.ath.adminefectivo.dto.ListaDetalleDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;

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
	
	/**
	 * Método que construye una consulta dinamica con varios parametro de entrada
	 * 
	 * @param consulta
	 * @param contenido
	 * @return string
	 * @author jchaparro
	 */
	public String queryBuilder(String consulta, Map<String, ListaDetalleDTO> detalleDefinicionMap);
	
	/**
	 * Método que retorna el valor del parámetro correspondiente según la descripción de la regla
	 * 
	 * @param descripcionRegla
	 * @param validacionArchivo
	 * @return string
	 * @author jchaparro
	 */
	public String getvalorCampoRegla(String descripcionRegla, ValidacionArchivoDTO validacionArchivo, Map<String, ListaDetalleDTO> detalleDefinicionMap);
}
