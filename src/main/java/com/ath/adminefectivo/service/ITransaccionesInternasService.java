package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
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
	TransaccionesInternasDTO getTransaccionesInternasById(Long idTransaccionesInternas);
	
	/**
	 * Servicio para persistir un transaccionesInternas
	 * @param transaccionesInternas
	 * @return
	 */
	TransaccionesInternas saveTransaccionesInternasById(TransaccionesInternasDTO transaccionesInternasDTO);

	/**
	 * Servicio para eliminar el transaccionesInternas por su id
	 * @param idTransaccionesInternas
	 */
	void deleteTransaccionesInternasById(String idTransaccionesInternas);
	
	
	/**
	 * Servicio encargado de obtener las transacciones internas segun el 
	 * rango de fechas recibido por parametro
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return TransaccionesInternas
	 * @author duvan.naranjo
	 */
	List<TransaccionesInternasDTO> getTransaccionesInternasByFechas(Date fechaInicio, Date fechaFin);

	/**
	 * Servicio encargado de llamar la funcion encargada de realizar el proceso de movimientos contables
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @param tipoContabilidad
	 * @param estadoContabilidadGenerado
	 * @param estadoContabilidadGenerado
	 * @return boolean
	 * @author duvan.naranjo
	 */
	boolean generarMovimientosContables(Date fechaInicio, Date fechaFin, String tipoContabilidad,
			int estadoContabilidadGenerado);

	/**
	 * Servicio encargado de llamar la funcion encargada de realizar el borrado de las transacciones 
	 * internas que se han procesado en las mismas fechas
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @author duvan.naranjo
	 */
	void deleteTransaccionesInternasByFechas(Date fechaInicio, Date fechaFin);
	
	/**
	 * Servicio encargado de llamar la funcion encargada de realizar el borrado de las transacciones 
	 * internas que se han procesado en las mismas fechas
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @param tipoServicio
	 * @author duvan.naranjo
	 */
	void deleteTransaccionesInternasByFechasAndTipoProceso(Date fechaInicio, Date fechaFin, String tipoProceso);

	/**
	 * Metodo encargado de validar los errores contables por banco
	 * 
	 * @param fecha
	 * @param tipoContabilidad
	 * @param codBanco
	 * @return boolean
	 * @author duvan.naranjo
	 */
	boolean existErroresContablesByBanco(Date fecha, String tipoContabilidad, int codBanco);

	/**
	 * Metodo encargado de validar los errores contables para todos los bancos
	 * 
	 * @param fecha
	 * @param tipoContabilidad
	 * @param codBanco
	 * @return boolean
	 * @author duvan.naranjo
	 */
	boolean existErroresContablesAllBanco(Date fecha, String tipoContabilidad);
	
	

}
