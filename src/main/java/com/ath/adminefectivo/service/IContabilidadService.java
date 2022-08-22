package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionIntradiaDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de los servicios referentes a los archivosCargados
 *
 * @author CamiloBenavides
 */
public interface IContabilidadService {

	/**
	 * Servicio encargado de generar la contabilidad basado en las operaciones 
	 * programadas recibidas
	 * 
	 * @param tipoProceso
	 * @param List<OperacionesProgramadasDTO>
	 * @return int
	 * @author duvan.naranjo
	 */
	int generarContabilidad(String tipoProceso, List<OperacionesProgramadasDTO> listadoOperacionesProgramadas);

	/**
	 * Servicio encargado de generar los movimientos contables de la contabilidad basado en las transacciones 
	 * internas recibidas
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @param tipoContabilidad
	 * @param estadoContabilidadGenerado
	 * @return int
	 * @author duvan.naranjo
	 */
	int generarMovimientosContables(String fechaInicio, String fechaFin, String tipoContabilidad,
			int estadoContabilidadGenerado, String formatoFecha);

	/**
	 * Servicio encargado de generar la contabilidad basado en las operaciones 
	 * programadas intradia recibidas
	 * 
	 * @param tipoProceso
	 * @param List<OperacionIntradiaDTO>
	 * @param consecutivoDia
	 * @return int
	 * @author duvan.naranjo
	 */
	int generarContabilidadIntradia(String tipoContabilidad,
			List<OperacionIntradiaDTO> listadoOperacionesProgramadasIntradia, int consecutivoDia);
	

}
