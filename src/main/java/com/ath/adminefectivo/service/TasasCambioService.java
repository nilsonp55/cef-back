package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.TasasCambio;
import com.ath.adminefectivo.entities.TasasCambioPK;
import com.querydsl.core.types.Predicate;

public interface TasasCambioService {

	/**
	 * Servicio encargado de consultar la lista de todos los tasasCambio 
	 * @param predicate
	 * @return List<TasasCambio>
	 * @author bayron.perez
	 */
	List<TasasCambio> getTasasCambios(Predicate predicate);
	
	/**
	 * Metodo encargado de obtener un usaurio por su ID
	 * @param idTasasCambio
	 * @return
	 * @author bayron.perez
	 */
	TasasCambio getTasasCambioById(TasasCambioPK tasasCambioPK);
	
	/**
	 * Metodo encargado de guardar un usaurio 
	 * @param idTasasCambio
	 * @return
	 * @author bayron.perez
	 */
	TasasCambio postTasasCambio(TasasCambio tasasCambio);
	
	/**
	 * Metodo encargado de actualizar un usaurio 
	 * @param idTasasCambio
	 * @return
	 * @author bayron.perez
	 */
	TasasCambio putTasasCambio(TasasCambio tasasCambio);
	
	/**
	 * Metodo encargado de eliminar un usaurio 
	 * @param idTasasCambio
	 * @return
	 * @author bayron.perez
	 */
	void deleteTasasCambio(TasasCambio tasasCambio);
	
}
