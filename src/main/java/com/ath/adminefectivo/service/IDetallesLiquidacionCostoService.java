package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.DetallesLiquidacionCosto;
import com.ath.adminefectivo.entities.DetallesLiquidacionCostoFlatEntity;

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

	List<DetallesLiquidacionCostoFlatEntity> consultarPorIdLiquidacionFlat (Long idLiquidacion);
	
	List<DetallesLiquidacionCostoFlatEntity> f2actualizarListaDetallesValoresLiquidados(List<DetallesLiquidacionCostoFlatEntity> valoresDetallesLiquidacionCostos);
}
