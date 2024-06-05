package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.DetallesLiquidacionCosto;

public interface IDetallesLiquidacionCostoService {
	
	/**
	 * Metodo encargado de actualizar la tabla de detalles liquidacion costos 
	 * 
	 * @param DetallesLiquidacionCosto
	 * @return DetallesLiquidacionCosto
	 * @author hector.mercado
	 */
	DetallesLiquidacionCosto f2actualizarvaloresLiquidadosRepository(DetallesLiquidacionCosto actualizar);
	
	List<DetallesLiquidacionCosto> consultarPorIdLiquidacion(Long idLiquidacion);
}
