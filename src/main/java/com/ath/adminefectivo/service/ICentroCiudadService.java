package com.ath.adminefectivo.service;

import java.util.List;
import com.ath.adminefectivo.dto.CentroCiudadDTO;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de los servicios a la tabla de CentroCiudadService
 * @author cesar.castano
 */
public interface ICentroCiudadService {

	/**
	 * Servicio encargado de consultar la lista de todos registros de CentroCiudad 
	 * con el predicado
	 * 
	 * @return List<CentroCiudadDTO>
	 * @author cesar.castano
	 */
	List<CentroCiudadDTO> getCentrosCiudades(Predicate predicate);
	
	/**
	 * Servicio encargado de consultar una CentroCiudad por su identificador unico
	 * 
	 * @return List<CentroCiudadDTO>
	 * @author cesar.castano
	 */
	CentroCiudadDTO getCentroCiudadById(Integer idCentroCiudad);
	
	/**
	 * Servicio encargado de persistir una CentroCiudad por su identificador unico
	 * 
	 * @return List<CentroCiudadDTO>
	 * @author cesar.castano
	 */
	CentroCiudadDTO postCentroCiudad(CentroCiudadDTO centroCiudadDTO);
	
	/**
	 * Servicio encargado de actualiar una CentroCiudad por su identificador unico
	 * 
	 * @return List<CentroCiudadDTO>
	 * @author cesar.castano
	 */
	CentroCiudadDTO putCentroCiudad(CentroCiudadDTO centroCiudadDTO);
	
    /**
     * 
     * @param idCentroCiudad
     * @author prv_nparra
     */
    void deleteCentroCiudad(Integer idCentroCiudad);
	
}
