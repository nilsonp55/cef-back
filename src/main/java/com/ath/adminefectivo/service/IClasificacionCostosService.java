package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.EscalasDTO;
import com.ath.adminefectivo.dto.compuestos.CostosMensualesClasificacionDTO;
import com.querydsl.core.types.Predicate;

public interface IClasificacionCostosService {

	

	/**
	 * Servicio encargado de contener la logica para consultar la clasificacion mensual
	 * de costos
	 * 
	 * @param transportadora
	 * @param mesAnio
	 * @return List<CostosMensualesClasificacionDTO>
	 * @author duvan.naranjo
	 */
	List<CostosMensualesClasificacionDTO> getClasificacionMensualCostos(String transportadora);

	/**
	 * Metodo encargado de generar la liquidacion de costos mensual
	 * 
	 * @param listadoCostosMensuales
	 * @return List<CostosMensualesClasificacionDTO> 
	 * @author duvan.naranjo
	 */
	List<CostosMensualesClasificacionDTO> liquidarClasificacionCostos(
			List<CostosMensualesClasificacionDTO> listadoCostosMensuales);

	/**
	 * Metodo encargado de guardar los costos mensuales por banco 
	 * segun lo asignado por el usuario 
	 * 
	 * @param listadoCostosMensuales
	 * @return String
	 * @author duvan.naranjo
	 */
	String guardarClasificacionCostosMensuales(List<CostosMensualesClasificacionDTO> listadoCostosMensuales);

	

}
