package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IBancosDelegate;
import com.ath.adminefectivo.delegate.ILogProcesoMensualDelegate;
import com.ath.adminefectivo.dto.LogProcesoMensualDTO;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ILogProcesoMensualService;
import com.querydsl.core.types.Predicate;

@Service
public class LogProcesoMensualDelegateImpl implements ILogProcesoMensualDelegate{

	@Autowired
	ILogProcesoMensualService logProcesoMensualService;
	
	/**
	 * {@inheritDoc}
	 */
	public List<LogProcesoMensualDTO> getLogsProcesosMensuales(Predicate predicate) {
		return logProcesoMensualService.getLogsProcesosMensual(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String cerrarLogProcesoMensual(String proceso) {
		var logProcesoMensual = logProcesoMensualService.getLogsProcesosMensualByCodigoProcesoAndPendiente(proceso);
		boolean estadoValidacion = logProcesoMensualService.validarLogProceso(logProcesoMensual);
		
		return null;
	}

	
}
