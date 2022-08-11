package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate encargado de orquestar la lógica de negocio referente a las operaciones Programadas
 * @author duvan.naranjo
 */
public interface IOperacionesProgramadasDelegate {

	/**
	 * Metodo encargado de realizar la generación de operaciones programadas
	 * basado en la tabla archivos cargados y registros cargados, este proceso
	 * se realiza basado en los archivos de programacion preliminar PM
	 * 
	 * @param idArchivo
	 * @return List<OperacionesProgramadasDTO>
	 * @author duvan.naranjo
	 */
	String generarOperacionesProgramadas(String idArchivo);

}
