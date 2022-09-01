package com.ath.adminefectivo.delegate;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;

public interface ICostosFleteCharterDelegate {

	/**
	 * Servicio que consulta los costos por flete charter
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return List<ParametrosLiquidacionCostoDTO>
	 * @author prv_ccastano
	 */
	List<ParametrosLiquidacionCostoDTO> consultarCostosFleteCharter(Date fechaInicial, Date fechaFinal);

	/**
	 * Servicio que graba los costos por flete charter
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return Boolean
	 * @author prv_ccastano
	 */
	Boolean grabarCostosFleteCharter(Date fechaInicial, Date fechaFinal);

}
