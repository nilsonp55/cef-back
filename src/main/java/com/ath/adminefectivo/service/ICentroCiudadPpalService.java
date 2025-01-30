package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.CentroCiudadDTO;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de clase servicios para entidad CentroCiudadPpal
 * @author prv_nparra
 */
public interface ICentroCiudadPpalService {
	
	/**
	 * 
	 * Lista los registros de CentroCiudadPpal de acuerdo con el predicado
	 * 
	 * @param predicate parametros para hacer filtrado de registros 
	 * @return List<CentroCiudadDTO>
	 * @author prv_nparra
	 */
	List<CentroCiudadDTO> listCentroCiudad(Predicate predicate);

}
