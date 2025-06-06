package com.ath.adminefectivo.service;

import com.ath.adminefectivo.dto.ValorLiquidadoDTO;
import com.ath.adminefectivo.dto.ValoresLiquidadosDTO;
import com.ath.adminefectivo.dto.compuestos.CostosCharterDTO;

public interface IValoresLiquidadosService {

	/**
	 * Servicio que actualiza el costo de los fletes por Charter
	 * @param parametros
	 * @return ValoresLiquidados
	 * @author prv_ccastano
	 */
	Boolean actualizaCostosFletesCharter(CostosCharterDTO costosCharter);
	
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

	/**
	 * Metodo encargado de consultar la tabla de valores liquidados por 
	 * id liquidacion 
	 * 
	 * @param idLiquidacion
	 * @return ValoresLiquidados
	 * @author duvan.naranjo
	 */
	ValorLiquidadoDTO consultarValoresLiquidadosPorIdLiquidacion(Long idLiquidacion);

}
