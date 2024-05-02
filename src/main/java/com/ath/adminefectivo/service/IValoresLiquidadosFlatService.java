package com.ath.adminefectivo.service;

import com.ath.adminefectivo.entities.ValoresLiquidadosFlatEntity;

public interface IValoresLiquidadosFlatService {
	
	/**
	 * Metodo encargado de actualizar la tabla de valores liquidados 
	 * 
	 * @param ValoresLiquidados
	 * @return ValoresLiquidados
	 * @author hector.mercado
	 */
	ValoresLiquidadosFlatEntity f2actualizarvaloresLiquidadosRepository(ValoresLiquidadosFlatEntity actualizar);
}
