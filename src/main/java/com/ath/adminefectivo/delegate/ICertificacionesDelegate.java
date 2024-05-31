package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.entities.ArchivosCargados;

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

    Boolean procesarArchivosCertificaciones(List<Long> idsArchivosCargados);

	String  procesarArchivosAlcance(List<ArchivosCargados> archivosCargados);
}
