package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.compuestos.CostosMensualesClasificacionDTO;


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
	List<CostosMensualesClasificacionDTO> getClasificacionMensualCostos(String transportadora, String mesAnio);

	/**
	 * 
	 * @param listadoCostosMensuales
	 * @return
	 */
	List<CostosMensualesClasificacionDTO> liquidarClasificacionCostos(
			List<CostosMensualesClasificacionDTO> listadoCostosMensuales);

	/**
	 * 
	 * @param listadoCostosMensuales
	 * @return
	 */
	String guardarClasificacionCostosMensuales(List<CostosMensualesClasificacionDTO> listadoCostosMensuales);



}
