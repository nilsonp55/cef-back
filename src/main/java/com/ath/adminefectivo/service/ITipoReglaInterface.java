package com.ath.adminefectivo.service;

import com.ath.adminefectivo.dto.ReglasDetalleArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;

/**
 * Interfaz que contiene los servicios de ejecucion de reglas
 *
 * @author RafaelParra
 */
public interface ITipoReglaInterface {

	/**
	 * Método encargado de ejercutar la logica de la regla y aplicarla a un campo
	 * particular
	 * 
	 * @param reglaDetalleAchivo
	 * @param valorCampo
	 * @return boolean
	 * @author RafaelParra
	 */
	public boolean ejecutarRegla(ReglasDetalleArchivoDTO reglaDetalleAchivo, String valorCampo);

}
