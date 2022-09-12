package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.ath.adminefectivo.dto.compuestos.OperacionespConciliadoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.dto.CertificadasNoConciliadasDTO;
import com.ath.adminefectivo.dto.ParametrosConciliacionManualDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.entities.OperacionesCertificadas;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IOperacionesCertificadasRepository;
import com.ath.adminefectivo.repositories.IOperacionesProgramadasRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.IConciliacionOperacionesService;
import com.ath.adminefectivo.service.IConciliacionServiciosService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IFondosService;
import com.ath.adminefectivo.service.IOperacionesCertificadasService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.service.ITransportadorasService;
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
	
	@Autowired
	IFondosService fondoService;
	
	@Autowired
	IBancosService bancosService;
	
	@Autowired
	IPuntosService puntosService;
	
	@Autowired
	ITransportadorasService transportadorasService;

	List<OperacionesProgramadas> operacionesp;
	List<OperacionesCertificadas> operacionesc;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<OperacionesProgramadasNombresDTO> getOperacionesConciliadas(Predicate predicate, Pageable page) {

		Page<OperacionesProgramadas> operacionesProgramadas = operacionesProgramadasRepository.
				findByEstadoConciliacion(dominioService.valorTextoDominio(
						Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_CONCILIADO), page);
		if (operacionesProgramadas.isEmpty()) {
			throw new NegocioException(ApiResponseCode.ERROR_CONCILIADOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_CONCILIADOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_CONCILIADOS_NO_ENCONTRADO.getHttpStatus());
		} else {
			return operacionesProgramadasService.getNombresProgramadasConciliadas(
					operacionesProgramadas, predicate, page);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<ProgramadasNoConciliadasDTO> getProgramadaNoConcilliada(Predicate predicate, Pageable page) {

		Page<OperacionesProgramadas> archivos = operacionesProgramadasRepository.findAll(predicate, page);
		if (archivos.isEmpty()) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
		}
		return consultarProgramasNoConciliadas(archivos, page);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<CertificadasNoConciliadasDTO> getCertificadaNoConciliada(Predicate predicate, Pageable page) {

		Page<OperacionesCertificadas> archivos = operacionesCertificadasRepository.findAll(predicate, page);
		if (archivos.isEmpty()) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}
		return consultarCertificadasNoConciliadas(archivos, page);
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
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean conciliacionAutomatica() {
		operacionesp = operacionesProgramadasService.obtenerOperacionesProgramadas();
		operacionesc = operacionesCertificadasService.obtenerOperacionesCertificaciones();
		actualizaOperacionesProgramadas();
		actualizaOperacionesCertificadas();
		actualizarConciliacionServicios();
		return true;
	}

	/**
	 * Metod encargado de consultar las operaciones programadas y no conciliadas
	 * @param operacionesProgramadas
	 * @return Page<ProgramadasNoConciliadasDTO>
	 * @author cesar.castano
	 */
	private Page<ProgramadasNoConciliadasDTO> consultarProgramasNoConciliadas(
										Page<OperacionesProgramadas> operacionesProgramadas, Pageable page) {

		operacionesProgramadas.forEach(entity -> {
						var programadasNoConciliadasDTO = new ProgramadasNoConciliadasDTO();
						programadasNoConciliadasDTO = ProgramadasNoConciliadasDTO.CONVERTER_DTO.apply(entity);
						String banco = puntosService.getNombrePunto(dominioService.valorTextoDominio(
					  												Constantes.DOMINIO_TIPOS_PUNTO, 
					  												Dominios.TIPOS_PUNTO_BANCO),
							  			fondoService.getEntidadFondo(
							  					programadasNoConciliadasDTO.getCodigoFondoTDV()).getBancoAVAL());
						entity.setBancoAVAL(banco);
						String transportadora = transportadorasService.getNombreTransportadora(
							fondoService.getEntidadFondo(programadasNoConciliadasDTO.getCodigoFondoTDV()).getTdv());
						entity.setTdv(transportadora);
						});		
	
		return new PageImpl<>(operacionesProgramadas.getContent().stream()
                .map(ProgramadasNoConciliadasDTO.CONVERTER_DTO).collect(Collectors.<ProgramadasNoConciliadasDTO>toList()), page,
                operacionesProgramadas.getTotalElements());	
	}

	/**
	 * Metod encargado de consultar las operaciones certificadas y no conciliadas
	 * @param operacionesCertificadas
	 * @return Page<CertificadasNoConciliadasDTO>
	 * @author cesar.castano
	 */
	private Page<CertificadasNoConciliadasDTO> consultarCertificadasNoConciliadas(
			Page<OperacionesCertificadas> operacionesCertificadas, Pageable page) {

		operacionesCertificadas.forEach(entity -> {
						var certificadasNoConciliadas = new CertificadasNoConciliadasDTO();
						certificadasNoConciliadas = CertificadasNoConciliadasDTO.CONVERTER_DTO.apply(entity);
						String banco = puntosService.getNombrePunto(dominioService.valorTextoDominio(
																	Constantes.DOMINIO_TIPOS_PUNTO, 
																	Dominios.TIPOS_PUNTO_BANCO),
										fondoService.getEntidadFondo(
										certificadasNoConciliadas.getCodigoFondoTDV()).getBancoAVAL());
						entity.setBancoAVAL(banco);
						String transportadora = transportadorasService.getNombreTransportadora(
								fondoService.getEntidadFondo(certificadasNoConciliadas.getCodigoFondoTDV()).getTdv());
						entity.setTdv(transportadora);
						});
		return new PageImpl<>(operacionesCertificadas.getContent().stream()
                .map(CertificadasNoConciliadasDTO.CONVERTER_DTO).collect(Collectors.<CertificadasNoConciliadasDTO>toList()), page,
                operacionesCertificadas.getTotalElements());	
	}

	/**
	 * Metodo encargado de actualizar la tabla de Conciliacion Servicios
	 * @author cesar.castano
	 */
	private void actualizarConciliacionServicios() {
		for (var i = 0; i < operacionesp.size(); i++) {
			var parametros = new ParametrosConciliacionManualDTO();
			var conciliado = new OperacionespConciliadoDTO();
			conciliado.setCodigoFondoTDV(operacionesp.get(i).getCodigoFondoTDV());
			conciliado.setCodigoPuntoDestino(operacionesp.get(i).getCodigoPuntoDestino());
			conciliado.setCodigoPuntoOrigen(operacionesp.get(i).getCodigoPuntoOrigen());
			conciliado.setEstadoConciliacion(operacionesp.get(i).getEstadoConciliacion());
			conciliado.setFechaDestino(operacionesp.get(i).getFechaDestino());
			conciliado.setFechaOrigen(operacionesp.get(i).getFechaOrigen());
			conciliado.setTipoOperacion(operacionesp.get(i).getTipoOperacion());
			conciliado.setValorTotal(operacionesp.get(i).getValorTotal());

			OperacionesCertificadas operaciones = operacionesc.stream().filter(puntoT ->
			puntoT.getCodigoFondoTDV().equals(conciliado.getCodigoFondoTDV()) &&
			puntoT.getCodigoPuntoDestino().equals(conciliado.getCodigoPuntoDestino()) && 
			puntoT.getCodigoPuntoOrigen().equals(conciliado.getCodigoPuntoOrigen()) &&
			(puntoT.getFechaEjecucion().equals(conciliado.getFechaOrigen()) ||
			 puntoT.getFechaEjecucion().equals(conciliado.getFechaDestino())) &&
			puntoT.getValorTotal().equals(conciliado.getValorTotal()) &&
			puntoT.getEstadoConciliacion().equals(conciliado.getEstadoConciliacion()) &&
			puntoT.getTipoOperacion().equals(conciliado.getTipoOperacion()))
					.findFirst().orElse(null);

			parametros.setIdOperacion(operacionesp.get(i).getIdOperacion());
			parametros.setIdCertificacion(operaciones.getIdCertificacion());
			conciliacionServicesService.crearRegistroConciliacion(parametros);
		}
	}

	/**
	 * Metodo encargado de actualizar la tabla de Operaciones Programadas
	 * @author cesar.castano
	 */
	private void actualizaOperacionesProgramadas() {
		for (var i = 0; i < operacionesp.size(); i++) {
			operacionesProgramadasService.actualizarEstadoEnProgramadas(operacionesp.get(i).getIdOperacion(),
				dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADO_CONCILIACION,
						Dominios.ESTADO_CONCILIACION_CONCILIADO));
		}
	}

	/**
	 * Metodo encargado de actualizar la tabla de Operaciones Certificadas
	 * @author cesar.castano
	 */
	private void actualizaOperacionesCertificadas() {
		for (var i = 0; i < operacionesc.size(); i++) {
			operacionesCertificadasService.actualizarEstadoEnCertificadas(operacionesc.get(i).getIdCertificacion(), 
					dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADO_CONCILIACION,
							Dominios.ESTADO_CONCILIACION_CONCILIADO));
		}
	}
}
