package com.ath.adminefectivo.service;

import java.util.Map;

import com.ath.adminefectivo.dto.ListaDetalleDTO;
import com.ath.adminefectivo.dto.ReglasDetalleArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionMotorDTO;

/**
 * Interfaz de los servicios referentes al motor de reglas 
 *
 * @author rparra
 */
public interface IMotorReglasService {

	/**
	 * 
	 * @param regla
	 * @param valorCampo
	 * @return boolean
	 * @author rparra
	 */
	ValidacionMotorDTO evaluarReglaSimple(String regla, String valorCampo, ValidacionArchivoDTO validacionArchivo, int index, Map<String, ListaDetalleDTO> detalleDefinicionMap);

	/**
	 * Metodo encargado de realizar la ejecución de una regla
	 * 
	 * @param reglaVO
	 * @param valorCampo
	 * @return booolean
	 * @author rparra
	 */
	boolean compilarRegla(ReglasDetalleArchivoDTO reglaVO, String valorCampo, ValidacionArchivoDTO validacionArchivo, int index, Map<String, ListaDetalleDTO> detalleDefinicionMap);

	/**
	 * 
	 * @param regla
	 * @param valorCampo
	 * @return
	 * @return boolean
	 * @author CamiloBenavides
	 */
	ValidacionMotorDTO evaluarReglaMultiple(String regla, String valorCampo, ValidacionArchivoDTO validacionArchivo, int index, Map<String, ListaDetalleDTO> detalleDefinicionMap);

	


}
