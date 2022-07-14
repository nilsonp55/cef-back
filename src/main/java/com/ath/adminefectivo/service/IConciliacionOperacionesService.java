package com.ath.adminefectivo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.ProgramadasNoConciliadasDTO;
import com.ath.adminefectivo.dto.ResumenConciliacionesDTO;
import com.ath.adminefectivo.dto.UpdateCertificadasFallidasDTO;
import com.ath.adminefectivo.dto.UpdateProgramadasFallidasDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesProgramadasNombresDTO;
import com.ath.adminefectivo.dto.CertificadasNoConciliadasDTO;
import com.ath.adminefectivo.dto.ParametrosConciliacionManualDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.querydsl.core.types.Predicate;

/**
 * 
 * @author cesar.castano
 *
 */
public interface IConciliacionOperacionesService {

	/**
	 * Servicio encargado de consultar todas las operaciones Conciliadas paginadas y por nombres
	 * @return Page<OperacionesProgramadasNombresDTO>
	 * @param predicate
	 * @param page
	 * @author cesar.castano
	 */
	Page<OperacionesProgramadasNombresDTO> getOperacionesConciliadas(Predicate predicate, Pageable page);
	
	/**
	 * Servicio encargado de consultar las operaciones programadas por filtro y con
	 * paginación
	 * 
	 * @param predicate
	 * @param page
	 * @return List<ProgramadasNoConciliadasDTO>
	 * @author cesar.castano
	 */
	Page<ProgramadasNoConciliadasDTO> getProgramadaNoConcilliada(Predicate predicate, Pageable page);
	
	/**
	 * Servicio encargado de consultar las operaciones certificadas por filtro y con
	 * paginación
	 * 
	 * @param predicate
	 * @param page
	 * @return List<CertificadasNoConciliadasDTO>
	 * @author cesar.castano
	 */
	Page<CertificadasNoConciliadasDTO> getCertificadaNoConciliada(Predicate predicate, Pageable page);

	/**
	 * Servicio encargado de actualizar estado y valor de las operaciones Programadas Fallidas
	 * @param UpdateProgramadasFallidasDTO
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean updateOperacionesProgramadasFallidas(UpdateProgramadasFallidasDTO updateProgramadasFallidasDTO);

	/**
	 * Servicio encargado de actualizar estado y valor de las operaciones Certificadas Fallidas
	 * @param UpdateCertificadasFallidasDTO
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean updateOperacionesCertificadasFallidas(UpdateCertificadasFallidasDTO updateCertificadasFallidasDTO);

	/**
	 * Servicio encargado de hacer la conciliacion manual
	 * @param ParametrosConciliacionManualDTO
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean conciliacionManual(List<ParametrosConciliacionManualDTO> conciliacionManualDTO);

	/**
	 * Servicio encargado de hacer el resumen de las conciliaciones
	 * @param fechaConciliacion
	 * @return ResumenConciliacionesDTO
	 * @author cesar.castano
	 */
	ResumenConciliacionesDTO consultaResumenConciliaciones(FechasConciliacionDTO fechaConciliacion);

	/**
	 * Servicio encargado de hacer la conciliacion automatica
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean conciliacionAutomatica();

}
