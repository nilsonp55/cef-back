package com.ath.adminefectivo.service;

import java.util.Date;

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
	AuditoriaProcesosDTO consultarAuditoriaPorProceso(String codigoProceso, Date fechaSistema);

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
	void ActualizarAuditoriaProceso(String codigoProceso, Date fechaSistema, String estado, String mensaje);
	
	/**
	 * 
	 * @param fechaSistema
	 */
	void crearTodosAuditoriaProcesos(Date fechaSistema);
	

}
