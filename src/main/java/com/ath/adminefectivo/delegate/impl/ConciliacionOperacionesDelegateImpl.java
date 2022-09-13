package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IConciliacionOperacionesDelegate;
import com.ath.adminefectivo.dto.ProgramadasNoConciliadasDTO;
import com.ath.adminefectivo.dto.ResumenConciliacionesDTO;
import com.ath.adminefectivo.dto.UpdateCertificadasFallidasDTO;
import com.ath.adminefectivo.dto.UpdateProgramadasFallidasDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesProgramadasNombresDTO;
import com.ath.adminefectivo.dto.CertificadasNoConciliadasDTO;
import com.ath.adminefectivo.dto.ParametrosConciliacionManualDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.service.IConciliacionOperacionesService;
import com.querydsl.core.types.Predicate;

/**
 * @author cesar.castano
 */
@Service
public class ConciliacionOperacionesDelegateImpl implements IConciliacionOperacionesDelegate {

	@Autowired
	IConciliacionOperacionesService conciliacionOperacionesService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<OperacionesProgramadasNombresDTO> getOperacionesConciliadas(Predicate predicate, Pageable page) {
		
		return conciliacionOperacionesService.getOperacionesConciliadas(predicate, page);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<ProgramadasNoConciliadasDTO> getProgramadaNoConcilliada(Predicate predicate, Pageable page) {

		return conciliacionOperacionesService.getProgramadaNoConcilliada(predicate, page);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<CertificadasNoConciliadasDTO> getCertificadaNoConciliada(Predicate predicate, Pageable page) {

		return conciliacionOperacionesService.getCertificadaNoConciliada(predicate, page);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean updateOperacionesProgramadasFallidas(UpdateProgramadasFallidasDTO updateProgramadasFallidasDTO) {

		return conciliacionOperacionesService.updateOperacionesProgramadasFallidas(updateProgramadasFallidasDTO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean updateOperacionesCertificadasFallidas(UpdateCertificadasFallidasDTO updateCertificadasFallidasDTO) {
		
		return conciliacionOperacionesService.updateOperacionesCertificadasFallidas(updateCertificadasFallidasDTO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean conciliacionManual(List<ParametrosConciliacionManualDTO> conciliacionManualDTO) {
		
		return conciliacionOperacionesService.conciliacionManual(conciliacionManualDTO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResumenConciliacionesDTO consultaResumenConciliaciones(FechasConciliacionDTO fechaConciliacion) {
		
		return conciliacionOperacionesService.consultaResumenConciliaciones(fechaConciliacion);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean cierreConciliaciones() {
		return conciliacionOperacionesService.cierreConciliaciones();
	}

}
