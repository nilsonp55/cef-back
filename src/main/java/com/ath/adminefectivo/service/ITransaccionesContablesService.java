package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.ath.adminefectivo.entities.TransaccionesContables;
import com.ath.adminefectivo.entities.TransaccionesInternas;

/**
 * Servicios para gestionar las transacciones contables
 * @author duvan.naranjo
 */

public interface ITransaccionesContablesService {

	/**
	 * Servicio para obtener los transaccionesInternas totales
	 * @return: TransaccionesInternas
	 */
	List<TransaccionesContables> getAllTransaccionesContables();
	
	/**
	 * Servicio para obtner el transaccionesInternas por su identificador
	 * @param idTransaccionesInternas
	 * @return
	 */
	TransaccionesContables getTransaccionesContablesById(String idTransaccionesContables);
	
	/**
	 * Servicio para persistir un transaccionesInternas
	 * @param TransaccionesContablesDTO
	 * @return TransaccionesContables
	 * @author duvan.naranjo
	 */
	TransaccionesContables saveTransaccionesContablesById(TransaccionesContablesDTO transaccionesContablesDTO);

	/**
	 * Servicio para eliminar el transaccionesContables por su id
	 * 
	 * @param idTransaccionesContables
	 */
	void deleteTransaccionesContablesById(String idTransaccionesContables);
	
	
	/**
	 * Servicio encargado de obtener las transacciones internas segun el 
	 * rango de fechas recibido por parametro
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return List<TransaccionesContablesDTO>
	 * @author duvan.naranjo
	 */
	List<TransaccionesContablesDTO> getTransaccionesContablesByFechas(Date fechaInicio, Date fechaFin);
	

	/**
	 * Servicio encargado de obtener las transacciones internas debito y credito
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return List<TransaccionesContablesDTO>
	 * @author Miller.Caro
	 */
	List<TransaccionesContablesDTO> getTransaccionesContablesByNaturaleza(String Naturaleza);
	
	/**
	 * Servicio encargado de obtener las transacciones internas debito y credito
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return List<TransaccionesContablesDTO>
	 * @author Miller.Caro
	 */
	List<RespuestaContableDTO> getCierreContable(Date fecha,String tipoContabilidad,String numeroBancos,String codBanco);

	/**
	 * Servicio encargado de obtener las transacciones por proceso
	 * 
	 * @param str
	 * @return List<TransaccionesContablesDTO>
	 * @author Miller.Caro
	 */
	String findBytipoProceso(String str);

}
