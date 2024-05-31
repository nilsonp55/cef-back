package com.ath.adminefectivo.service;

import com.ath.adminefectivo.dto.ReglasDetalleArchivoDTO;

/**
 * Interfaz de los servicios referentes a las reglas 
 *
 * @author duvan.naranjo
 */
public interface IReglasDetalleArchivoService {

	/**
	 * Método encargado de consultar una regla
	 * 
	 * @param regla
	 * @return ReglasDetalleArchivoDTO
	 * @author rparra
	 */
	ReglasDetalleArchivoDTO buscarRegla(Integer regla);



}
