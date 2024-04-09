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

}
