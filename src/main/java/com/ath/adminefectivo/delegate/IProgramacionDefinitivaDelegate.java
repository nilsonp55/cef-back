package com.ath.adminefectivo.delegate;

/**
 * Delegate que expone los servicios referente al procesamiento de archivos
 * de fondos, oficinas y cajeros
 * @author cesar.castano
 */
public interface IProgramacionDefinitivaDelegate {

	/**
	 * Metodo encargado de procesar los archivos de fondos, oficinas y cajeros
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean procesarProgramacionDefinitiva(String modeloArchivo, Long idArchivo);

}
