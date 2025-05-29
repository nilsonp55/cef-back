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
    
    /**
     * Crea un nuevo registro de CentroCiudadPpal
     * 
     * @param centroCiudadDTO DTO con los datos a persistir
     * @return CentroCiudadDTO con los datos del registro creado
     * @author prv_nparra
     */
    CentroCiudadDTO create(CentroCiudadDTO centroCiudadDTO);

    /**
     * Actualiza un registro en CentroCiudadPpal
     * @param centroCiudadDTO datos de CentroCiudadPpal para actualizar en Entity
     * @return DTO con los datos actualizados en el registro
     * @author prv_nparra
     */
	CentroCiudadDTO update(CentroCiudadDTO centroCiudadDTO);

	/**
	 * Elimina registro de CentroCiudadPpal si existe
	 * @param idCentroCiudad Id de centroCiudadPpal a eliminar
	 * @author prv_nparra 
	 */
	void delete(Integer idCentroCiudad);
}
