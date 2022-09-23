package com.ath.adminefectivo.service;

import java.util.Date;

import com.ath.adminefectivo.entities.LogProcesoDiario;

public interface ICierreContabilidadService {
	
	/**
	 * Servicio encargado de validar tipoContabilidad AM O PM
	 * 
	 * @param tipoContabilidad
	 * @author Miller.Caro
	 */
	
	boolean validacionTipoContabilidad(String tipoContabilidad);

	/**
	 * Metodo que contiene la logica de decidir que proceso 
	 * debe ejecutar para validar los errores contables 
	 * 
	 * @param fechaSistema
	 * @param tipoContabilidad
	 * @param codBanco
	 * @return boolean
	 * @author duvan.naranjo
	 */
	boolean existsErroresContablesByTipoContabilidadAndFecha(Date fechaSistema, String tipoContabilidad, int codBanco);
}
