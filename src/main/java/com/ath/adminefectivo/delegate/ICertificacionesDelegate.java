package com.ath.adminefectivo.delegate;

public interface ICertificacionesDelegate {

	/**
	 * Metodo encargado de procesar los archivos de certificaciones
	 * @param modeloArchivo
	 * @param idArchivo
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean procesarCertificaciones(String agrupador);
	
	/**
	 * Metodo encargado de procesar los archivos de alcance a certificaciones
	 * @param agrupador
	 * @return String
	 * @author rafael.parra
	 */
	String procesarAlcances();

}
