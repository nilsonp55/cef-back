package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.ErroresContablesDTO;
import com.ath.adminefectivo.dto.OperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.ath.adminefectivo.dto.compuestos.ContabilidadDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionIntradiaDTO;
import com.ath.adminefectivo.dto.compuestos.ResultadoErroresContablesDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.entities.TransaccionesInternas;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de los servicios referentes a los errores contables
 *
 * @author duvan.naranjo
 */
public interface IErroresContablesService {

	/**
	 * Servicio encargado de generar la contabilidad basado en las operaciones 
	 * programadas recibidas
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return ErroresContablesDTO
	 * @author duvan.naranjo
	 */
	List<ErroresContablesDTO> consultarErroresContablesByFechaAndTipoProceso(Date fechaFin, String tipoProceso);

	/**
	 * Servicio encargado de generar el listado de los errores contables 
	 * 
	 * @return ResultadoErroresContablesDTO
	 * @author duvan.naranjo
	 */
	List<ResultadoErroresContablesDTO> consultarErroresContables();

	/**
	 * Servicio encargado de realizar el proceso de las transacciones internas 
	 * nuevas que se corrigieron los errores que se presentaron en errores contables
	 * 
	 * @return List<TransaccionesInternasDTO>
	 * @author duvan.naranjo
	 */
	List<TransaccionesInternasDTO> generarRespuestaProcesoContables();

	
	/**
	 * Servicio encargado de realizar el proceso de eliminar un error contable por 
	 * el id de la transaccion interna.
	 * 
	 * @return List<TransaccionesInternasDTO>
	 * @author duvan.naranjo
	 */
	void eliminarErrorContableByIdTransaccionInterna(long idTransaccionesInternas);

	
	

}
