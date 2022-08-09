package com.ath.adminefectivo.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.ProgramadasNoConciliadasDTO;
import com.ath.adminefectivo.dto.ResumenConciliacionesDTO;
import com.ath.adminefectivo.dto.UpdateCertificadasFallidasDTO;
import com.ath.adminefectivo.dto.UpdateProgramadasFallidasDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesProgramadasNombresDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.CertificadasNoConciliadasDTO;
import com.ath.adminefectivo.dto.ParametrosConciliacionManualDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.entities.OperacionesCertificadas;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IOperacionesCertificadasRepository;
import com.ath.adminefectivo.repositories.IOperacionesProgramadasRepository;
import com.ath.adminefectivo.service.IConciliacionOperacionesService;
import com.ath.adminefectivo.service.IConciliacionServiciosService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IOperacionesCertificadasService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;
import com.querydsl.core.types.Predicate;

@Service
public class ConciliacionOperacionesServiceImpl implements IConciliacionOperacionesService {

	@Autowired
	IOperacionesProgramadasRepository operacionesProgramadasRepository;

	@Autowired
	IOperacionesCertificadasRepository operacionesCertificadasRepository;

	@Autowired
	IDominioService dominioService;

	@Autowired
	IOperacionesProgramadasService operacionesProgramadasService;

	@Autowired
	IOperacionesCertificadasService operacionesCertificadasService;

	@Autowired
	IConciliacionServiciosService conciliacionServicesService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<OperacionesProgramadasNombresDTO> getOperacionesConciliadas(Predicate predicate, Pageable page) {

		List<OperacionesProgramadas> operacionesProgramadas = operacionesProgramadasRepository.
				findByEstadoConciliacion(dominioService.valorTextoDominio(
						Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_CONCILIADO));
		if (operacionesProgramadas.isEmpty()) {
			throw new NegocioException(ApiResponseCode.ERROR_CONCILIADOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_CONCILIADOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_CONCILIADOS_NO_ENCONTRADO.getHttpStatus());
		} else {
			return operacionesProgramadasService.getNombresProgramadasConciliadas(operacionesProgramadas, predicate);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<ProgramadasNoConciliadasDTO> getProgramadaNoConcilliada(Predicate predicate, Pageable page) {

		var archivos = operacionesProgramadasRepository.findAll(predicate, page);
		if (archivos.isEmpty()) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
		}
		return new PageImpl<>(archivos.getContent().stream().map(ProgramadasNoConciliadasDTO.CONVERTER_DTO).toList(),
				archivos.getPageable(), archivos.getTotalElements());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<CertificadasNoConciliadasDTO> getCertificadaNoConciliada(Predicate predicate, Pageable page) {

		var archivos = operacionesCertificadasRepository.findAll(predicate, page);
		if (archivos.isEmpty()) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}
		return new PageImpl<>(archivos.getContent().stream().map(CertificadasNoConciliadasDTO.CONVERTER_DTO).toList(),
				archivos.getPageable(), archivos.getTotalElements());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean updateOperacionesProgramadasFallidas(UpdateProgramadasFallidasDTO updateProgramadasFallidasDTO) {

		Optional<OperacionesProgramadas> programadas = operacionesProgramadasRepository
				.findById(updateProgramadasFallidasDTO.getIdOperacion());
		if (programadas.isPresent()) {
			try {
				programadas.get().setIdOperacion(updateProgramadasFallidasDTO.getIdOperacion());
				programadas.get().setEstadoConciliacion(updateProgramadasFallidasDTO.getEstado());
				programadas.get().setValorTotal(updateProgramadasFallidasDTO.getValor());
				operacionesProgramadasRepository.save(programadas.get());
			} catch (Exception e) {
				e.getMessage();
			}
			return true;
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean updateOperacionesCertificadasFallidas(UpdateCertificadasFallidasDTO updateCertificadasFallidasDTO) {

		Optional<OperacionesCertificadas> certificadas = operacionesCertificadasRepository
				.findById(updateCertificadasFallidasDTO.getIdCertificacion());
		if (certificadas.isPresent()) {
			try {
				certificadas.get().setIdCertificacion(updateCertificadasFallidasDTO.getIdCertificacion());
				certificadas.get().setEstadoConciliacion(updateCertificadasFallidasDTO.getEstado());
				operacionesCertificadasRepository.save(certificadas.get());
			} catch (Exception e) {
				e.getMessage();
			}
			return true;
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean conciliacionManual(List<ParametrosConciliacionManualDTO> conciliacionManualDTO) {

		for (ParametrosConciliacionManualDTO elemento : conciliacionManualDTO) {
			var conciliaciones = operacionesProgramadasRepository.conciliacionManual(
					dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADO_CONCILIACION,
							Dominios.ESTADO_CONCILIACION_NO_CONCILIADO),
					elemento.getIdOperacion(), elemento.getIdCertificacion());
			if (Objects.isNull(conciliaciones)) {
				throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO.getDescription(),
						ApiResponseCode.ERROR_OPERACIONES_A_CONCILIAR_NO_ENCONTRADO.getHttpStatus());
			} 
			operacionesProgramadasService.actualizarEstadoEnProgramadas(elemento.getIdOperacion(),
						dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADO_CONCILIACION,
								Dominios.ESTADO_CONCILIACION_CONCILIADO));
			operacionesCertificadasService.actualizarEstadoEnCertificadas(elemento.getIdCertificacion(),
						dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADO_CONCILIACION,
										Dominios.ESTADO_CONCILIACION_CONCILIADO));
			conciliacionServicesService.crearRegistroConciliacion(elemento);
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResumenConciliacionesDTO consultaResumenConciliaciones(FechasConciliacionDTO fechaConciliacion) {
		var resumenConciliaciones = new ResumenConciliacionesDTO();
		resumenConciliaciones.setCertificadasNoConciliadas(operacionesCertificadasService
				.numeroOperacionesPorEstadoyFecha(fechaConciliacion, dominioService.valorTextoDominio(
						Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_NO_CONCILIADO)));
		resumenConciliaciones.setConciliadas(conciliacionServicesService.
				numeroOperacionesPorRangoFechas(
				fechaConciliacion));
		resumenConciliaciones.setConciliacionesCanceladas(operacionesProgramadasService
				.numeroOperacionesPorEstadoyFecha(fechaConciliacion, dominioService.valorTextoDominio(
						Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_CANCELADA)));
		resumenConciliaciones.setConciliacionesFallidas(operacionesProgramadasService.
				numeroOperacionesPorEstadoyFecha(
				fechaConciliacion, dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADO_CONCILIACION,
						Dominios.ESTADO_CONCILIACION_FALLIDA)));
		resumenConciliaciones.setConciliacionesFueraConciliacion(operacionesProgramadasService
				.numeroOperacionesPorEstadoyFecha(fechaConciliacion, dominioService.valorTextoDominio(
						Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_FUERA_DE_CONCILIACION)));
		resumenConciliaciones.setConciliacionesPospuestas(operacionesProgramadasService
				.numeroOperacionesPorEstadoyFecha(fechaConciliacion, dominioService.valorTextoDominio(
						Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_POSPUESTA)));
		resumenConciliaciones.setProgramadasNoConciliadas(operacionesProgramadasService
				.numeroOperacionesPorEstadoyFecha(fechaConciliacion, dominioService.valorTextoDominio(
						Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_NO_CONCILIADO)));
		return resumenConciliaciones;
	}
}
