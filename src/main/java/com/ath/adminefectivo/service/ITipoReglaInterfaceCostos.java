package com.ath.adminefectivo.service;

import java.util.Map;

import com.ath.adminefectivo.dto.ListaDetalleDTO;
import com.ath.adminefectivo.dto.ReglasDetalleArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;

/**
 * Interfaz que contiene la sobrecarga de los servicios de ejecucion de reglas
 *
 * @author jchaparro
 */

public interface ITipoReglaInterfaceCostos {
	
	/**
	 * Método Sobrecargado encargado de ejercutar la logica de la regla y aplicarla a varios campos
	 * en particular
	 * 
	 * @param reglaDetalleAchivo
	 * @param valorCampo
	 * @param validacionArchivo
	 * @return boolean
	 * @author jchaparro
	 */
	public boolean ejecutarRegla(ReglasDetalleArchivoDTO reglaDetalleAchivo, String valorCampo, ValidacionArchivoDTO validacionArchivo, int index, Map<String, ListaDetalleDTO> detalleDefinicionMap);

}
