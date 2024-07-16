package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ath.adminefectivo.dto.ParametrosLiquidacionCostoDTO;
import com.ath.adminefectivo.dto.compuestos.EstimadoClasificacionCostosDTO;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCostoFlat;

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

	/**
	 * Metodo encargado de realizar la consulta de los paramtros
	 * liquidacion costos 
	 * 
	 * @return List<ParametrosLiquidacionCostoDTO>
	 * @author duvan.naranjo
	 */
	List<ParametrosLiquidacionCostoDTO> consultarParametrosLiquidacionCostos(Date fechaSistema);

	/**
	 * Metodo encargado de realizar la consulta de parametros liquidacion costos por idLiquidacion
	 * 
	 * @param idLiquidacion
	 * @return ParametrosLiquidacionCosto
	 * @author duvan.naranjo
	 */
	Optional<ParametrosLiquidacionCosto> getParametrosLiquidacionCostosById(Long idLiquidacion);

	/**
	 * Metodo encargado de realizar la consulta de parametros liquidacion costos (flat) por idLiquidacion 
	 * 
	 * @param idLiquidacion
	 * @return ParametrosLiquidacionCosto
	 * @author jose.pabon
	 */
	ParametrosLiquidacionCostoFlat getParametrosLiquidacionCostosByIdFlat(Long idLiquidacion);

	/**
	 * Metodo encargado de eliminar los registros de ParametrosLiquidacionCostos
	 * 
	 * @param ParametrosLiquidacionCosto
	 * @return ParametrosLiquidacionCosto
	 * @author duvan.naranjo
	 */
	ParametrosLiquidacionCostoFlat f2eliminarParametrosLiquidacionCostos(ParametrosLiquidacionCostoFlat eliminar);
	
	/**
	 * Metodo encargado de actualizar los registros de ParametrosLiquidacionCostos
	 * 
	 * @param ParametrosLiquidacionCosto
	 * @return ParametrosLiquidacionCosto
	 * @author duvan.naranjo
	 */
	ParametrosLiquidacionCosto f2actualizarParametrosLiquidacionCostos(ParametrosLiquidacionCosto actualizar);
	
	ParametrosLiquidacionCostoFlat f2actualizarParametrosLiquidacionCostosFlat(ParametrosLiquidacionCostoFlat actualizar);

}
