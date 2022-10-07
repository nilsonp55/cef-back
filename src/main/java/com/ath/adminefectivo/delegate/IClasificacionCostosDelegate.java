package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.EscalasDTO;
import com.ath.adminefectivo.dto.compuestos.CostosMensualesClasificacionDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a las funciones dinamicas
 * @author duvan.naranjo
 */
public interface IClasificacionCostosDelegate {

	/**
	 * 
	 * @param transportadora
	 * @param mesAnio
	 * @return
	 */
	List<CostosMensualesClasificacionDTO> getClasificacionMensualPorBanco(String transportadora, String mesAnio);

	/**
	 * 
	 * @param listadoCostosMensuales
	 * @return
	 */
	List<CostosMensualesClasificacionDTO> getClasificacionMensualPorBanco(
			List<CostosMensualesClasificacionDTO> listadoCostosMensuales);

	/**
	 * 
	 * @param listadoCostosMensuales
	 * @return
	 */
	String guardarClasificacionCostosMensuales(List<CostosMensualesClasificacionDTO> listadoCostosMensuales);



}
