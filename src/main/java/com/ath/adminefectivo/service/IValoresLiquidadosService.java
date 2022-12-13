package com.ath.adminefectivo.service;

import com.ath.adminefectivo.dto.ValoresLiquidadosDTO;
import com.ath.adminefectivo.dto.compuestos.RespuestaLiquidarCostosDTO;
import com.ath.adminefectivo.dto.compuestos.costosCharterDTO;

public interface IValoresLiquidadosService {

	/**
	 * Servicio que actualiza el costo de los fletes por Charter
	 * @param parametros
	 * @return ValoresLiquidados
	 * @author prv_ccastano
	 */
	Boolean ActualizaCostosFletesCharter(costosCharterDTO costosCharter);
	
	/**
	 * Servicio que ejecuta los procesos de costos
	 * @author bayron.perez
	 */
	String procesarPackageCostos();

	/**
	 * 
	 * @return
	 */
	ValoresLiquidadosDTO consultarLiquidacionCostos();
}
