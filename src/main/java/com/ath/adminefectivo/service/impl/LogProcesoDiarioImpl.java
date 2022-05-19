package com.ath.adminefectivo.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.repositories.LogProcesoDiarioRepository;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IParametroService;

@Service
public class LogProcesoDiarioImpl implements ILogProcesoDiarioService {

	@Autowired
	LogProcesoDiarioRepository logProcesoDiarioRepository;

	@Autowired
	IParametroService parametroService;

	@Override
	public boolean esDiaCompleto(Date diaCierre) {
		int numProcesosTotales = parametroService.valorParametroEntero(Parametros.NUMERO_PROCESOS_TOTALES_DIA);

		int numProcesosDia = logProcesoDiarioRepository.countByFechaFinalizacionAndEstadoProceso(diaCierre,
				Dominios.ESTADO_PROCESO_DIA_COMPLETO);

		return numProcesosTotales == numProcesosDia;

	}

}
