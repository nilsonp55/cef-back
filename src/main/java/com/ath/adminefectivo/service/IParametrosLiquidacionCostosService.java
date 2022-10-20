package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.EscalasDTO;
import com.ath.adminefectivo.dto.compuestos.EstimadoClasificacionCostosDTO;
import com.querydsl.core.types.Predicate;

public interface IParametrosLiquidacionCostosService {

	/**
	 * Servicio encargado de consultar y agrupar los costos estimados 
	 * por transportadora, banco aval, por mes y anio
	 * 
	 * @param transportadora
	 * @param bancoAval
	 * @param mesI
	 * @param anioI
	 * @return EstimadoClasificacionCostosDTO
	 * @author duvan.naranjo
	 */
	EstimadoClasificacionCostosDTO consultaEstimadosCostos(String transportadora, int bancoAval, int mesI, int anioI);

	

	

}
