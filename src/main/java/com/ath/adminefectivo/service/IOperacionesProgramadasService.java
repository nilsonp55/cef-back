package com.ath.adminefectivo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesProgramadasNombresDTO;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.querydsl.core.types.Predicate;

/**
 * @author cesar.castano
 */
public interface IOperacionesProgramadasService {

	/**
	 * Servicio encargado de actualizar el estado en la tabla de Operaciones
	 * Programadas por id
	 * @param estado
	 * @param idOperacion
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean actualizarEstadoEnProgramadas(Integer idOperacion, String estado);

	/**
	 * Servicio encargado de enviar la tabla de Operaciones Programadas con nombres
	 * @param predicate
	 * @param operacionesProgramadasList
	 * @return OperacionesProgramadasNombresDTO
	 * @author cesar.castano
	 */
	Page<OperacionesProgramadasNombresDTO> getNombresProgramadasConciliadas(
			List<OperacionesProgramadas> operacionesProgramadasList, Predicate predicate);

	/**
	 * Servicio encargado de generar el numero de operaciones programadas no
	 * conciliadas por rango de fechas
	 * @return Integer
	 * @author cesar.castano
	 * @param fechaConciliacion
	 * @param estado
	 */
	Integer numeroOperacionesPorEstadoyFecha(FechasConciliacionDTO fechaConciliacion, String estado);

}
