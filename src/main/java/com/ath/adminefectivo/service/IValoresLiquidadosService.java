package com.ath.adminefectivo.service;

import com.ath.adminefectivo.dto.compuestos.costosCharterDTO;

public interface IValoresLiquidadosService {

	/**
	 * Servicio que actualiza el costo de los fletes por Charter
	 * @param parametros
	 * @return ValoresLiquidados
	 * @author prv_ccastano
	 */
	Boolean ActualizaCostosFletesCharter(costosCharterDTO costosCharter);
}
