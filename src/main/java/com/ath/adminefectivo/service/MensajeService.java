package com.ath.adminefectivo.service;


/**
 * Servicio de formato de Strings para mensajeria.
 * 
 * @author BayronPerez
 */
public interface MensajeService {

	/**
	 * Obtener mensaje por llave y locale por defecto.
	 * 
	 * @param llave
	 * @return
	 * @return String
	 * @author BayronPerez
	 */
	String obtenerMensaje(String llave);


	/**
	 * Obtener mensaje por llave, argumento y locale por defecto.
	 * 
	 * @param llave
	 * @param args
	 * @return String
	 * @author BayronPerez
	 */
	String obtenerMensaje(String llave, Object[] args);
}
