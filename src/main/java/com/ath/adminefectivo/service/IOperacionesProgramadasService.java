package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionIntradiaDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesProgramadasNombresDTO;
import com.ath.adminefectivo.dto.compuestos.ContabilidadDTO;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.querydsl.core.types.Predicate;

/**
 * Servicio encargado de generar la lógica de operaciones programadas
 * 
 * @author cesar.castano
 */
public interface IOperacionesProgramadasService {

	/**
	 * Servicio encargado de actualizar el estado en la tabla de Operaciones
	 * Programadas por id
	 * 
	 * @param estado
	 * @param idOperacion
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean actualizarEstadoEnProgramadas(Integer idOperacion, String estado);

	/**
	 * Servicio encargado de enviar la tabla de Operaciones Programadas con nombres
	 * 
	 * @param predicate
	 * @param operacionesProgramadasList
	 * @return OperacionesProgramadasNombresDTO
	 * @author cesar.castano
	 */
	Page<OperacionesProgramadasNombresDTO> getNombresProgramadasConciliadas(
			Page<OperacionesProgramadas> operacionesProgramadasList, Predicate predicate);

	/**
	 * Servicio encargado de generar el numero de operaciones programadas no
	 * conciliadas por rango de fechas
	 * 
	 * @return Integer
	 * @author cesar.castano
	 * @param fechaConciliacion
	 * @param estado
	 */
	Integer numeroOperacionesPorEstadoyFecha(FechasConciliacionDTO fechaConciliacion, String estado);

	/**
	 * Metodo encargado de realizar la logica necesaria para generar una operacion
	 * programdas basada en un archivo cargado
	 * 
	 * @return List<OperacionesProgramadasDTO>
	 * @param archivos
	 * @author duvan.naranjo
	 */
	List<OperacionesProgramadasDTO> generarOperacionesProgramadas(List<ArchivosCargadosDTO> archivos);

	/**

	 * Servicio encargado de obtener los registros de OperacionesProgramadas que
	 * estan dentro de la conciliacion
	 * 
	 * @return List<OperacionesProgramadas>
	 * @author cesar.castano
	 */
	List<OperacionesProgramadas> obtenerOperacionesProgramadas();
/**
	 * Servicio encargado de procesar los archivos cargados a la entidad OperacionesProgramadas
	 * @param archivoscargados
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean procesarArchivos(List<ArchivosCargados> archivosCargados);

	/**
	 * Servicio encargado de consultar las operaciones programadas intradia  
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return List<OperacionIntradiaDTO>
	 * @author duvan.naranjo
	 */
	List<OperacionIntradiaDTO> consultarOperacionesIntradia(Date fechaInicio, Date fechaFin);

	/**
	 * Servicio encargado de obtener la entidad OperacionesProgramadas para un
	 * IdOperacion
	 * 
	 * @return OperacionesProgramadas
	 * @author cesar.castano
	 */
	OperacionesProgramadas obtenerEntidadOperacionesProgramadasporId(Integer idOperacion);

	/**
	 * Servicio encargado de procesar los archivos cargados a la entidad
	 * OperacionesProgramadas
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return List<OperacionesProgramadasDTO>
	 * @author duvan.naranjo
	 */
	List<OperacionesProgramadasDTO> getOperacionesProgramadasPorFechas(String tipoContabilidad, Date fechaInicio,
			Date fechaFin);

}
