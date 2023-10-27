package com.ath.adminefectivo.service;

import java.util.Date;

import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;

public interface ICargueCertificacionService {
	
	ValidacionArchivoDTO procesarArchivo2(String idMaestroDefinicion, String nombreArchivo, boolean alcance,
			 Date fechaActual, Date fechaAnteriorHabil, Date fechaAnteriorHabil2);
	
	/**
	 * Metodo encargado de realizar la validaciones de un archivo cargado
	 *
	 * @param idMaestroDefinicion
	 * @param nombreArchivo
	 * @return void
	 * @author cesar.castano
	 * @author prv_nparra
	 */
	void validacionesAchivoCargado(String idMaestroDefinicion, String nombreArchivo, boolean alcance,
			Date fechaActual, Date fechaAnteriorHabil, Date fechaAnteriorHabil2);

}
