package com.ath.adminefectivo.delegate;

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

public interface IConciliacionOperacionesDelegate {
	
	/**
	 * Delegate responsable de consultar todas las operaciones conciliadas
	 * @return Page<OperacionesProgramadasConciliadasDTO>
	 * @param predicate
	 * @param page
	 * @author cesar.castano
	 */
	Page<OperacionesProgramadasNombresDTO> getOperacionesConciliadas(Predicate predicate, Pageable page);
	
	/**
	 * Delegate responsable de consultar los OperacionesProgramadas del sistema, por filtros y paginador
	 * @param predicate
	 * @param page
	 * @return Page<ProgramadasNoConciliadasDTO>
	 * @author cesar.castano
	 */
	Page<ProgramadasNoConciliadasDTO> getProgramadaNoConcilliada(Predicate predicate, Pageable page);
	
	/**
	 * Delegate responsable de consultar los OperacionesCertificadas del sistema, por filtros y paginador
	 * @param predicate
	 * @param page
	 * @return Page<CertificadasNoConciliadasDTO>
	 * @author cesar.castano
	 */
	Page<CertificadasNoConciliadasDTO> getCertificadaNoConciliada(Predicate predicate, Pageable page);
	
	/**
	 * Delegate encargado de modificar una operacion programada fallida
	 * @param UpdateProgramadasFallidasDTO
	 * @param page
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean updateOperacionesProgramadasFallidas(UpdateProgramadasFallidasDTO updateProgramadasFallidasDTO);

	/**
	 * Delegate encargado de modificar una operacion certificada fallida
	 * @param UpdateCertificadasFallidasDTO
	 * @param page
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean updateOperacionesCertificadasFallidas(UpdateCertificadasFallidasDTO updateCertificadasFallidasDTO);

	/**
	 * Delegate encargado de hacer la conciliacion manual
	 * @param ParametrosConciliacionManualDTO
	 * @param page
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean conciliacionManual(List<ParametrosConciliacionManualDTO> conciliacionManualDTO);

	/**
	 * Delegate encargado de hacer el resumen de las conciliaciones
	 * @param fechaConciliacion
	 * @return ResumenConciliacionesDTO
	 * @author cesar.castano
	 */
	ResumenConciliacionesDTO consultaResumenConciliaciones(FechasConciliacionDTO fechaConciliacion);

}
