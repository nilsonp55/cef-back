package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.dto.compuestos.ConteoContabilidadDTO;
import com.ath.adminefectivo.entities.TransaccionesContables;

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
	List<RespuestaContableDTO> getCierreContable(Date fecha,String tipoContabilidad,int codBanco);

	/**
	 * Servicio encargado de obtener las transacciones por proceso
	 * 
	 * @param str
	 * @return List<TransaccionesContablesDTO>
	 * @author Miller.Caro
	 */
	String findBytipoProceso(String str);

	/**
	 * Servicio encargado de consultar el conteo del proceso de contabilidad
	 * 
	 * @param fechaProceso
	 * @param f2
	 * @param tipoContabilidad
	 * @param string
	 * @return ConteoContabilidadDTO
	 * @author duvan.naranjo
	 */
	ConteoContabilidadDTO generarConteoContabilidad(Date fechaProceso, String tipoContabilidad);

	/**
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 */
	void deleteTransaccionesContablesByFechasAndTipoProceso(Date fechaInicio, Date fechaFin, String tipoProceso);

	/**
	 * Metodo encargado de ejecutar el metodo de la base de datos 
	 * encargado de generar el comprobante contable.
	 * 
	 * @param fecha
	 * @param tipoContabilidad
	 * @return String
	 * @author duvan.naranjo
	 */
	String generarComprobanteContable(Date fecha, String tipoContabilidad);

	/**
	 * Metodo encargado de realizar la consulta para el banco de bogota BBOG y concatenar su resultado como
	 * un string
	 * 
	 * @param fecha
	 * @param tipoContabilidad
	 * @param codBanco
	 * @return List<String>
	 * @author duvan.naranjo
	 */
	List<String> cierreContablebyBancoF1String(Date fecha, String tipoContabilidad, int codBanco);
	
	/**
	 * Metodo encargado de realizar la consulta y concatenar su resultado como
	 * un string, de la forma que lo realiza el banco 
	 * 
	 * @param fecha
	 * @param tipoContabilidad
	 * @param codBanco
	 * @return List<String>
	 * @author duvan.naranjo
	 */
	List<String> cierreContablebyBancoF2String(Date fecha, String tipoContabilidad, int codBanco);
}
