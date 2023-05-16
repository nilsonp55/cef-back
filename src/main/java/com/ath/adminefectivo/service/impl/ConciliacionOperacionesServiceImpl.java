package com.ath.adminefectivo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.CertificadasNoConciliadasDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.LogProcesoDiarioDTO;
import com.ath.adminefectivo.dto.ParametrosConciliacionDTO;
import com.ath.adminefectivo.dto.ProgramadasNoConciliadasDTO;
import com.ath.adminefectivo.dto.ResumenConciliacionesDTO;
import com.ath.adminefectivo.dto.UpdateCertificadasFallidasDTO;
import com.ath.adminefectivo.dto.UpdateProgramadasFallidasDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesProgramadasNombresDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionespConciliadoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.LogProcesoDiario;
import com.ath.adminefectivo.entities.OperacionesCertificadas;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IConciliacionOperacionesRepository;
import com.ath.adminefectivo.repositories.IOperacionesCertificadasRepository;
import com.ath.adminefectivo.repositories.IOperacionesProgramadasRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.ICiudadesService;
import com.ath.adminefectivo.service.IConciliacionOperacionesService;
import com.ath.adminefectivo.service.IConciliacionServiciosService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IFondosService;
import com.ath.adminefectivo.service.ILogProcesoDiarioService;
import com.ath.adminefectivo.service.IOperacionesCertificadasService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.querydsl.core.types.Predicate;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Setter
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
	IConciliacionOperacionesRepository conciliacionOperacionesRepository;
	
	@Autowired
	IFondosService fondoService;
	
	@Autowired
	IBancosService bancosService;
	
	@Autowired
	IPuntosService puntosService;
	
	@Autowired
	ITransportadorasService transportadorasService;
	
	@Autowired
	ILogProcesoDiarioService logProcesoDiarioService;
	
	@Autowired
	ICiudadesService ciudadService;

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
			log.info("Consultar Operaciones programadas: predicate: {} - page: {}", predicate.toString(), page.toString());
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
		}
		log.debug("Operaciones programadas registros: {}", archivos.getSize());
		return consultarProgramasNoConciliadas(archivos, page);
	}

	private OperacionesProgramadas obtenerNombresProgramadasNoConciliadas(
			OperacionesProgramadas programadaNoConciliadaNombre) {
		
		String nombreFondoTdv = puntosService.getPuntoById(programadaNoConciliadaNombre.getCodigoFondoTDV()).getNombrePunto();
		programadaNoConciliadaNombre.setNombreFondoTDV(nombreFondoTdv);
		
		Puntos puntoOrigen = puntosService.getPuntoById(programadaNoConciliadaNombre.getCodigoPuntoOrigen());
		programadaNoConciliadaNombre.setNombrePuntoOrigen(puntoOrigen.getNombrePunto());
		
		Puntos puntoDestino = puntosService.getPuntoById(programadaNoConciliadaNombre.getCodigoPuntoDestino());
		programadaNoConciliadaNombre.setNombrePuntoDestino(puntoDestino.getNombrePunto());
		
		programadaNoConciliadaNombre.setNombreCiudadOrigen(ciudadService.getCiudadPorCodigoDane(puntoOrigen.getCodigoCiudad()).getNombreCiudad());
		programadaNoConciliadaNombre.setNombreCiudadDestino(ciudadService.getCiudadPorCodigoDane(puntoDestino.getCodigoCiudad()).getNombreCiudad());
		return programadaNoConciliadaNombre;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<CertificadasNoConciliadasDTO> getCertificadaNoConciliada(Predicate predicate, Pageable page) {
		Page<OperacionesCertificadas> archivos = operacionesCertificadasRepository.findAll(predicate, page);
		if (archivos.isEmpty()) {
			log.info("Consultar Operaciones certificadas: predicate: {} - page: {}", predicate.toString(), page.toString());
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}
		log.debug("Operaciones Certificadas registros: {}", archivos.getSize());
		return consultarCertificadasNoConciliadas(archivos, page);
	}
	
	private OperacionesCertificadas obtenerNombresCertificadasNoConciliadas(
			OperacionesCertificadas certificadasNoConciliadaNombre) {
		
		String nombreFondoTdv = puntosService.getPuntoById(certificadasNoConciliadaNombre.getCodigoFondoTDV()).getNombrePunto();
		certificadasNoConciliadaNombre.setNombreFondoTDV(nombreFondoTdv);
		

		String nombrePuntoOrigen = puntosService.getPuntoById(certificadasNoConciliadaNombre.getCodigoPuntoOrigen()).getNombrePunto();
		certificadasNoConciliadaNombre.setNombrePuntoOrigen(nombrePuntoOrigen);
		
		String nombrePuntoDestino = puntosService.getPuntoById(certificadasNoConciliadaNombre.getCodigoPuntoDestino()).getNombrePunto();
		certificadasNoConciliadaNombre.setNombrePuntoDestino(nombrePuntoDestino);
		
		return certificadasNoConciliadaNombre;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean updateOperacionesProgramadasFallidas(UpdateProgramadasFallidasDTO updateProgramadasFallidasDTO) {

		if(Objects.isNull(updateProgramadasFallidasDTO.getIdOperacion())) {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_UPDATE_NULL.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_UPDATE_NULL.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_UPDATE_NULL.getHttpStatus());
		}
		OperacionesProgramadas programadas = operacionesProgramadasRepository
				.findById(updateProgramadasFallidasDTO.getIdOperacion()).orElse(null);
		if (!Objects.isNull(programadas)) {
			try {
				programadas.setIdOperacion(updateProgramadasFallidasDTO.getIdOperacion());
				programadas.setEstadoConciliacion(updateProgramadasFallidasDTO.getEstado());
				programadas.setValorTotal(updateProgramadasFallidasDTO.getValor());
				operacionesProgramadasRepository.save(programadas);
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

		if(Objects.isNull(updateCertificadasFallidasDTO.getIdCertificacion())) {
			log.error("Not IdCertificacion: {}", updateCertificadasFallidasDTO.getValor());
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_UPDATE_NULL.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_UPDATE_NULL.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_UPDATE_NULL.getHttpStatus());
		}
		OperacionesCertificadas certificadas = operacionesCertificadasRepository
				.findById(updateCertificadasFallidasDTO.getIdCertificacion()).orElse(null);
		if (!Objects.isNull(certificadas)) {
			try {
				certificadas.setIdCertificacion(updateCertificadasFallidasDTO.getIdCertificacion());
				certificadas.setEstadoConciliacion(updateCertificadasFallidasDTO.getEstado());
				certificadas.setValorTotal(updateCertificadasFallidasDTO.getValor());
				operacionesCertificadasRepository.save(certificadas);
			} catch (Exception e) {
				log.error("Failed to update Operaciones Certificadas Fallidas: {}", e.getMessage());
			}
			return true;
		} else {
			log.error("IdCertificacion not found: {}", updateCertificadasFallidasDTO.getIdCertificacion());
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_CERTIFICADAS_NO_ENCONTRADO.getHttpStatus());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional			   
	public Boolean conciliacionManual(List<ParametrosConciliacionDTO> conciliacionManualDTO) {

		for (ParametrosConciliacionDTO elemento : conciliacionManualDTO) {
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
			elemento.setTipoConciliacion(dominioService.valorTextoDominio(
									Constantes.DOMINIO_TIPOS_CONCILIACION,
									Dominios.TIPO_CONCILIACION_MANUAL));
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
				.numeroOperacionesPorEstadoFechaYConciliable(fechaConciliacion, dominioService.valorTextoDominio(
						Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_NO_CONCILIADO), Constantes.SI));
		
		resumenConciliaciones.setCertificadasNoConciliables(operacionesCertificadasService
				.numeroOperacionesPorEstadoFechaYConciliable(fechaConciliacion, dominioService.valorTextoDominio(
						Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_NO_CONCILIADO), Constantes.NO));
		
		resumenConciliaciones.setConciliadas(operacionesProgramadasRepository.countByEstadoConciliacionAndFechaOrigenBetween
                (dominioService.valorTextoDominio(Constantes.DOMINIO_ESTADO_CONCILIACION, Dominios.ESTADO_CONCILIACION_CONCILIADO),
                        fechaConciliacion.getFechaConciliacionInicial(), fechaConciliacion.getFechaConciliacionFinal() ));
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
	 * {@inheritDoc}
	 */
	@Override
	@Transactional			   
	public Boolean cierreConciliaciones() {
		String resultado = conciliacionOperacionesRepository.validarcierreconciliacion();
		
		if(resultado.toUpperCase().startsWith("OK")) {
			LogProcesoDiario logProcesoDiario = logProcesoDiarioService.obtenerEntidadLogProcesoDiario(
					Dominios.CODIGO_PROCESO_LOG_CONCILIACION);
			LogProcesoDiarioDTO logProcesoDiarioDTO = new LogProcesoDiarioDTO();
			logProcesoDiarioDTO.setIdLogProceso(logProcesoDiario.getIdLogProceso());
			logProcesoDiarioDTO.setCodigoProceso(Dominios.CODIGO_PROCESO_LOG_CONCILIACION);
			logProcesoDiarioDTO.setEstado(Constantes.REGISTRO_ACTIVO);
			logProcesoDiarioDTO.setFechaFinalizacion(new Date());
			logProcesoDiarioDTO.setFechaModificacion(new Date());
			logProcesoDiarioDTO.setFechaCreacion(logProcesoDiario.getFechaCreacion());
			logProcesoDiarioDTO.setUsuarioCreacion(logProcesoDiario.getUsuarioCreacion());
			logProcesoDiarioDTO.setUsuarioModificacion(logProcesoDiario.getUsuarioModificacion());
			logProcesoDiarioDTO.setEstadoProceso(Dominios.ESTADO_PROCESO_DIA_COMPLETO);
			logProcesoDiarioService.actualizarLogProcesoDiario(logProcesoDiarioDTO);
			return true;
		}else {
			return false;
		}
		
	}

	/**
	 * Metod encargado de consultar las operaciones programadas y no conciliadas
	 * @param operacionesProgramadas
	 * @return Page<ProgramadasNoConciliadasDTO>
	 * @author cesar.castano
	 */
	private Page<ProgramadasNoConciliadasDTO> consultarProgramasNoConciliadas(
										Page<OperacionesProgramadas> operacionesProgramadas, Pageable page) {
	
		return new PageImpl<>(operacionesProgramadas.getContent().stream()
                .map(e -> ProgramadasNoConciliadasDTO.CONVERTER_DTO.apply(this.obtenerNombresProgramadasNoConciliadas(e)))
                .collect(Collectors.<ProgramadasNoConciliadasDTO>toList()), page,
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
						var certificadasNoConciliadas = CertificadasNoConciliadasDTO.CONVERTER_DTO.apply(entity);
						entity.setEntradaSalida(certificadasNoConciliadas.getEntradaSalida());
						this.obtenerNombresCertificadasNoConciliadas(entity);
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
			var parametros = new ParametrosConciliacionDTO();
			var conciliado = new OperacionespConciliadoDTO();
			conciliado.setCodigoFondoTDV(operacionesp.get(i).getCodigoFondoTDV());
			conciliado.setCodigoPuntoDestino(operacionesp.get(i).getCodigoPuntoDestino());
			conciliado.setCodigoPuntoOrigen(operacionesp.get(i).getCodigoPuntoOrigen());
			conciliado.setEstadoConciliacion(operacionesp.get(i).getEstadoConciliacion());
			conciliado.setFechaDestino(operacionesp.get(i).getFechaDestino());
			conciliado.setFechaOrigen(operacionesp.get(i).getFechaOrigen());
			conciliado.setTipoOperacion(operacionesp.get(i).getTipoOperacion());
			conciliado.setValorTotal(operacionesp.get(i).getValorTotal());
			conciliado.setEntradaSalida(operacionesp.get(i).getEntradaSalida());

			OperacionesCertificadas operaciones = operacionesc.stream().filter(puntoT ->
			puntoT.getCodigoFondoTDV().equals(conciliado.getCodigoFondoTDV()) &&
			puntoT.getCodigoPuntoDestino().equals(conciliado.getCodigoPuntoDestino()) && 
			puntoT.getCodigoPuntoOrigen().equals(conciliado.getCodigoPuntoOrigen()) &&
			(puntoT.getFechaEjecucion().equals(conciliado.getFechaOrigen()) ||
			 puntoT.getFechaEjecucion().equals(conciliado.getFechaDestino())) &&
			puntoT.getValorTotal().equals(conciliado.getValorTotal()) &&
			puntoT.getEstadoConciliacion().equals(conciliado.getEstadoConciliacion()) &&
			puntoT.getEntradaSalida().equals(conciliado.getEntradaSalida()))
					.findFirst().orElse(null);

			parametros.setIdOperacion(operacionesp.get(i).getIdOperacion());
			parametros.setIdCertificacion(operaciones.getIdCertificacion());
			parametros.setTipoConciliacion(dominioService.valorTextoDominio(
										Constantes.DOMINIO_TIPOS_CONCILIACION,
										Dominios.TIPO_CONCILIACION_AUTOMATICA));
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
