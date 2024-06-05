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
	
	ValoresLiquidadosFlatEntity consultarPorIdLiquidacion(Long idLiquidacion);
	
	/**
	 * Metodo encargado de realizar la consulta de parametros valores liquidados por idLiquidacion
	 * 
	 * @param idLiquidacion
	 * @return ValoresLiquidadosFlatEntity
	 * @author jose.pabon
	 */
	ValoresLiquidadosFlatEntity getParametrosValoresLiquidadosByIdLiquidacion(Long idLiquidacion);
}
