package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.TransaccionesInternas;

/**
 * Servicios para gestionar las transacciones internas
 * @author Bayron Perez
 */

public interface ITransaccionesInternasService {

	/**
	 * Servicio para obtener los transaccionesInternas totales
	 * @return: TransaccionesInternas
	 */
	List<TransaccionesInternas> getAllTransaccionesInternas();
	
	/**
	 * Servicio para obtner el transaccionesInternas por su identificador
	 * @param idTransaccionesInternas
	 * @return
	 */
	TransaccionesInternas getTransaccionesInternasById(String idTransaccionesInternas);
	
	/**
	 * Servicio para persistir un transaccionesInternas
	 * @param transaccionesInternas
	 * @return
	 */
	TransaccionesInternas saveTransaccionesInternasById(TransaccionesInternas transaccionesInternas);

	/**
	 * Servicio para eliminar el transaccionesInternas por su id
	 * @param idTransaccionesInternas
	 */
	void deleteTransaccionesInternasById(String idTransaccionesInternas);

}
