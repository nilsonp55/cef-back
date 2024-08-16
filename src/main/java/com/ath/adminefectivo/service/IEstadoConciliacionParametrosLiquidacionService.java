package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.entities.EstadoConciliacionParametrosLiquidacion;

public interface IEstadoConciliacionParametrosLiquidacionService  {
	
	/**
	 * Servicio encargado de buscar una liquidacion en EstadoConciliacionParametrosLiquidacion
	 * 
	 * @param idLiquidacion
	 * @return List<EstadoConciliacionParametrosLiquidacion>
	 * @author hector.mercado
	 */
	List<EstadoConciliacionParametrosLiquidacion> buscarLiquidacion(Long idLiquidacion, Integer estado);
	
	
	/**
	 * Servicio encargado de guardar un EstadoConciliacionParametrosLiquidacion
	 * 
	 * @param EstadoConciliacionParametrosLiquidacion
	 * @return EstadoConciliacionParametrosLiquidacion
	 * @author hector.mercado
	 */
	EstadoConciliacionParametrosLiquidacion save(EstadoConciliacionParametrosLiquidacion registro);	

	/**
	 * Servicio encargado de eliminar un EstadoConciliacionParametrosLiquidacion
	 * 
	 * @param EstadoConciliacionParametrosLiquidacion
	 * @author jose.pabon
	 * @return 
	 */
	void delete(EstadoConciliacionParametrosLiquidacion registro);
}
