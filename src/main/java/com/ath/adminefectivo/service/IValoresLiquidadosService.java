package com.ath.adminefectivo.service;

import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;

public interface IValoresLiquidadosService {

	/**
	 * Servicio que actualiza el costo de los fletes por Charter
	 * @param parametros
	 * @return ValoresLiquidados
	 * @author prv_ccastano
	 */
	void ActualizaCostosFletesCharter(ParametrosLiquidacionCosto parametros);
}
