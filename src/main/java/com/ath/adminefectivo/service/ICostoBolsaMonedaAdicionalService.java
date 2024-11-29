package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.CostoBolsaMonedaAdicionalDTO;
import com.ath.adminefectivo.dto.ListCostoBolsaMonedaAdicionalDTO;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.entities.CostoBolsaMonedaAdicional;
import com.querydsl.core.types.Predicate;

public interface ICostoBolsaMonedaAdicionalService {

	/**
	 * Servicio encargado de contener la logica para listar los
	 * costos de bolsas de monedas adicionales
	 * 
	 * @param predicate
	 * @return List<CostoBolsaMonedaAdicional>
	 * @author hector.mercado
	 */
	Page<CostoBolsaMonedaAdicionalDTO> getCostoBolsaMonedaAdicional(Predicate predicate, Pageable pageable);

	/**
	 * Servicio encargado de contener la logica para consultar por transportadora los
	 * costos de bolsas de monedas adicionales
	 * 
	 * @param idCostoBolsaAdicional
	 * @return CostoBolsaMonedaAdicional
	 * @author hector.mercado
	 */
	Page<CostoBolsaMonedaAdicionalDTO> getCostoBolsaMonedaAdicionalByTransportadora(String codigoTdv);

	/**
	 * Servicio encargado de contener la logica guardar o persistir  
	 * costos de bolsas de monedas adicionales
	 * 
	 * @param CostoBolsaMonedaAdicional
	 * @return CostoBolsaMonedaAdicional
	 * @author hector.mercado
	 */
	CostoBolsaMonedaAdicionalDTO saveCostoBolsaMonedaAdicional(CostoBolsaMonedaAdicionalDTO costoBolsaMonedaAdicionalDTO);

	/**
	 * Servicio encargado de contener la logica eliminar costos de bolsas de monedas adicionales
	 * 
	 * @param idCostoBolsaAdicional
	 * @return boolean
	 * @author hector.mercado
	 */
	boolean eliminarCostoBolsaMonedaAdicional(ListCostoBolsaMonedaAdicionalDTO registrosEliminar);

	
	

}
