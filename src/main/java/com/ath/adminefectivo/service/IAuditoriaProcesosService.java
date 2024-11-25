package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.AuditoriaProcesosDTO;

public interface IAuditoriaProcesosService {

	/**
	 * Servicio encargado de realizar la logica para consultar una
	 * auditoria por proceso y fecha
	 * 
	 * @param codigoProceso
	 * @param fechaSistema
	 * @return AuditoriaProcesosDTO
	 * @author duvan_naranjo
	 */
	AuditoriaProcesosDTO consultarAuditoriaPorProceso(String codigoProceso);

	/**
	 * 
	 * @param codigoProceso
	 * @param fechaSistema
	 */
	void crearAuditoriaProceso(String codigoProceso, Date fechaSistema);

	/**
	 * 
	 * @param codigoProceso
	 * @param fechaSistema
	 * @param estado
	 */
	void actualizarAuditoriaProceso(String codigoProceso, Date fechaSistema, String estado, String mensaje);
	
	/**
	 * 
	 * @param fechaSistema
	 */
	void crearTodosAuditoriaProcesos(Date fechaSistema);
	
	/**
	 * Consulta las fechas de AuditoriaProceso registras en base de datos
	 * @return Lista de fechas procesadas de AuditoriaProceso
	 * @author prv_nparra
	 */
	List<Date> consultarFechasProcesadas();
	
	/**
	 * Crear un nuevo registro en AuditoriaProcesos a partir del DTO
	 * @param auditoriaProcesosDTO objeto que contiene valores para crear entidad AuditoriaProcesos
	 * @return Objet AuditoriaProcesos crea en base de datos
	 * @author prv_nparra
	 */
	AuditoriaProcesosDTO crearAuditoriaProceso(AuditoriaProcesosDTO auditoriaProcesosDTO);

}
