package com.ath.adminefectivo.delegate;

import java.util.Date;

import com.ath.adminefectivo.dto.AuditoriaProcesosDTO;

/**
 * Delegate que expone los servicios referente a las auditorias de procesos
 * @author duvan.naranjo
 */
public interface IAuditoriaProcesosDelegate {

	/**
	 * Delegate encargado de realizar conectar con el servicio para 
	 * realizar el proceso de consultar auditoria por proceso
	 *  
	 * @param codigoProceso
	 * @param fechaSistema
	 * @return AuditoriaProcesosDTO
	 * @author duvan.naranjo
	 */
	AuditoriaProcesosDTO consultarAuditoriaPorProceso(String codigoProceso, Date fechaSistema);

}
