package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.costosCharterDTO;

public interface ICostosFleteCharterService {

	/**
	 * Servicio que retorna los parametros de la liquidacion de costos 
	 * con base en un rango de fechas
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return List<ParametrosLiquidacionCostoDTO>
	 * @author prv_ccastano
	 */
	List<ParametrosLiquidacionCostoDTO> ConsultarCostosFleteCharter(Date fechaInicial, Date fechaFinal);

	/**
	 * Servicio que consulta y actualiza los costos del flete por charter
	 * @param List<ParametrosLiquidacionCostoDTO>
	 * @return Boolean
	 * @author prv_ccastano
	 */
	Boolean GrabarCostosFleteCharter(costosCharterDTO costosCharter);
}
