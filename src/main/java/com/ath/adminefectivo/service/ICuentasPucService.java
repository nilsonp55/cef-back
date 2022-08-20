package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.CuentasPucDTO;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de los servicios a la tabla de CuentasPucService
 * @author bayronPerez
 */

public interface ICuentasPucService {

	/**
	 * Servicio encargado de consultar la lista de todos registros de CuentasPuc 
	 * con el predicado
	 * 
	 * @return List<CuentasPucDTO>
	 * @author bayronPerez
	 */
	List<CuentasPucDTO> getCuentasPuc(Predicate predicate);
	
	/**
	 * Servicio encargado de consultar una CuentasPuc por su identificador unico
	 * 
	 * @return List<CuentasPucDTO>
	 * @author bayronPerez
	 */
	CuentasPucDTO getCuentasPucById(Long idCuentasPuc);
	
	/**
	 * Servicio encargado de persistir una CuentasPuc por su identificador unico
	 * 
	 * @return List<CuentasPucDTO>
	 * @author bayronPerez
	 */
	CuentasPucDTO postCuentasPuc(CuentasPucDTO cuentasPucDTO);
	
	/**
	 * Servicio encargado de actualiar una CuentasPuc por su identificador unico
	 * 
	 * @return List<CuentasPucDTO>
	 * @author bayronPerez
	 */
	CuentasPucDTO putCuentasPuc(CuentasPucDTO cuentasPucDTO);
	
	/**
	 * Servicio encargado de eliminar una CuentasPuc por su identificador unico
	 * 
	 * @return List<>
	 * @author bayronPerez
	 */
	void deleteCuentasPuc(CuentasPucDTO cuentasPucDTO);
}
 