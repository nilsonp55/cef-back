package com.ath.adminefectivo.delegate.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IAuditoriaProcesosDelegate;
import com.ath.adminefectivo.delegate.IBancosDelegate;
import com.ath.adminefectivo.dto.AuditoriaProcesosDTO;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;
import com.ath.adminefectivo.service.IBancosService;
import com.querydsl.core.types.Predicate;

@Service
public class AuditoriaProcesosDelegateImpl implements IAuditoriaProcesosDelegate{

	@Autowired
	IAuditoriaProcesosService auditoriaProcesosService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AuditoriaProcesosDTO consultarAuditoriaPorProceso(String codigoProceso, Date fechaSistema) {
		return auditoriaProcesosService.consultarAuditoriaPorProceso(codigoProceso, fechaSistema);
	}
	
	
	
	
}
