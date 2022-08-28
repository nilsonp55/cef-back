package com.ath.adminefectivo.service;

import com.ath.adminefectivo.entities.LogProcesoDiario;

public interface ICierreContabilidadService {
	
	/**
	 * Servicio encargado de validar tipoContabilidad AM O PM
	 * 
	 * @param tipoContabilidad
	 * @author Miller.Caro
	 */
	
	LogProcesoDiario validacionTipoContabilidad(String tipoContabilidad);
}
