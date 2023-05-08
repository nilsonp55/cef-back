package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.ath.adminefectivo.dto.compuestos.ContabilidadDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionIntradiaDTO;

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
	int generarMovimientosContables(Date fechaInicio, Date fechaFin, String tipoContabilidad,
			int estadoContabilidadGenerado);

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

	/**
	 * 
	 * @param fechaProceso
	 * @param f2
	 * @param tipoContabilidad
	 * @param string
	 * @return
	 */
	ContabilidadDTO generarRespuestaContabilidad(Date fechaProceso, String tipoContabilidad, String mensaje);

	/**
	 * Metodo encargado de generar un listado de las transacciones internas nuevas
	 * que se crearon cuando se solucionaron los errores contables, a su vez cambia de 
	 * estado los errores contables a CORREGIDO 
	 * 
	 * 
	 * List<TransaccionesInternasDTO>
	 * @return List<TransaccionesInternasDTO>
	 * @author duvan.naranjo
	 */
	List<TransaccionesInternasDTO> generarRespuestaProcesoContables();

	/**
	 * Metodo encargado de eliminar las transacciones contables e interas generadas en una contabilidad
	 * ejecutada la misma fecha 
	 * 
	 * @param String
	 * @param List<OperacionesProgramadasDTO>
	 * @param fechaInicio
	 * @param fechaFin
	 *
	 * @author duvan.naranjo
	 */
	void procesoEliminarExistentes(String tipoContabilidad, List<OperacionesProgramadasDTO> operacionesProgramadas, Date fechaInicio, Date fechaFin);
	

}
