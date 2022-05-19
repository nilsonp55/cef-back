package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.FechasConciliacionDTO;
import com.ath.adminefectivo.dto.FondosDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.dto.TransportadorasDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionesProgramadasNombresDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IOperacionesProgramadasRepository;
import com.ath.adminefectivo.service.ICiudadesService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IFondosService;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.querydsl.core.types.Predicate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Service
public class OperacionesProgramadasServiceImpl implements IOperacionesProgramadasService {

	@Autowired
	IOperacionesProgramadasRepository operacionesProgramadasRepository;

	@Autowired
	IFondosService fondosService;

	@Autowired
	ITransportadorasService transportadorasService;

	@Autowired
	IPuntosService puntosService;

	@Autowired
	ICiudadesService ciudadService;
	
	@Autowired
	IDominioService dominioService;
	
	private List<TransportadorasDTO> listaTransportadoras;
	private List<FondosDTO> listaFondos;
	private List<PuntosDTO> listaPuntos;
	private List<CiudadesDTO> listaCiudades;
	
	/**
	 * {@inheritDoc}
	 */
	@Transactional
	@Override
	public Boolean actualizarEstadoEnProgramadas(Integer idOperacion, String estado) {

		Optional<OperacionesProgramadas> operaciones = operacionesProgramadasRepository.findById(idOperacion);
		if (operaciones.isPresent()) {
			try {
				operaciones.get().setEstadoConciliacion(estado);
				operaciones.get().setIdOperacion(idOperacion);
				operaciones.get().setFechaModificacion(new Date());
				operaciones.get().setUsuarioModificacion("user1");
				operacionesProgramadasRepository.save(operaciones.get());
			} catch (Exception e) {
				e.getMessage();
			}
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<OperacionesProgramadasNombresDTO> getNombresProgramadasConciliadas(
			List<OperacionesProgramadas> operacionesProgramadasList, Predicate predicate) {
		
		List<OperacionesProgramadasNombresDTO> listOperacionesProgramas = new ArrayList<>();
		this.getListados(predicate);
		for (OperacionesProgramadas programadas : operacionesProgramadasList) {
			var operacionesProgramadas = new OperacionesProgramadasNombresDTO();
			// Obtiene nombres de transportadora y Banco dueño del fondo
			operacionesProgramadas.setNombreTransportadora(this.getNombreTransportadora(
						programadas.getCodigoFondoTDV()));	
			operacionesProgramadas.setNombreBanco(this.getNombreBanco(programadas.getCodigoFondoTDV()));	
			// Obtiene nombres de tipo origen y nombre ciudad origen
			operacionesProgramadas.setNombrePuntoOrigen(this.getNombrePunto(
						programadas.getTipoPuntoOrigen(), programadas.getCodigoPuntoOrigen()));
			operacionesProgramadas.setNombreCiudadOrigen(this.getNombreCiudad(
						programadas.getTipoPuntoOrigen(), programadas.getCodigoPuntoOrigen()));
			// Obtiene nombres de tipo destino y nombre ciudad destino
			operacionesProgramadas.setNombrePuntoDestino(this.getNombrePunto(
						programadas.getTipoPuntoDestino(), programadas.getCodigoPuntoDestino()));
			operacionesProgramadas.setNombreCiudadDestino(this.getNombreCiudad(
						programadas.getTipoPuntoDestino(), programadas.getCodigoPuntoDestino()));
			// Datos sin nombres
			operacionesProgramadas.setTipoOperacion(programadas.getTipoOperacion());
			operacionesProgramadas.setValorTotal(programadas.getValorTotal());
			operacionesProgramadas.setFechaEjecucion(programadas.getFechaOrigen());
			operacionesProgramadas.setEstadoConciliacion(programadas.getEstadoConciliacion());
			// Obtiene datos de la tabla de Conciliacion Servicios
			operacionesProgramadas
						.setTipoConciliacion(programadas.getConciliacionServicios().get(0).getTipoConciliacion());
			operacionesProgramadas
						.setIdConciliacion(programadas.getConciliacionServicios().get(0).getIdConciliacion());
			listOperacionesProgramas.add(operacionesProgramadas);
		}
		return new PageImpl<>(listOperacionesProgramas);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer numeroOperacionesPorEstadoyFecha(FechasConciliacionDTO fechaConciliacion, String estado) {
		return operacionesProgramadasRepository.countByEstadoConciliacionAndFechaOrigenBetween(estado,
				fechaConciliacion.getFechaConciliacionInicial(), fechaConciliacion.getFechaConciliacionFinal());
	}
	
	/**
	 * Metodo que consiste en obtener los listados de transportadoras, puntos codigo, 
	 * puntos y ciudades en memoria
	 * @param predicate
	 * @author cesar.castano
	 */
	private void getListados(Predicate predicate) {
		
		this.setListaTransportadoras(transportadorasService.getTransportadoras(predicate));
		this.setListaFondos(fondosService.getFondos(predicate));
		this.setListaPuntos(puntosService.getPuntos(predicate));
		this.setListaCiudades(ciudadService.getCiudades(predicate));
	}
	
	/**
	 * Metodo que obtiene el nombre del punto de una lista cuando el codigo punto es un Banco
	 * @param codigoFondoTDV
	 * @return String
	 * @author cesar.castano
	 */
	private String getNombreBanco(Integer codigoFondoTDV) {
		FondosDTO fondos = listaFondos.stream().filter(fondo ->
		fondo.getCodigoPunto().equals(codigoFondoTDV)).findFirst().orElse(null);
		if(!Objects.isNull(fondos)) {
			var punto = listaPuntos.stream().filter(puntoT -> 
				puntoT.getCodigoPunto().equals(fondos.getBancoAVAL()) && 
				puntoT.getTipoPunto().equals(dominioService.valorTextoDominio(
						Constantes.DOMINIO_TIPOS_PUNTO,
						Dominios.TIPOS_PUNTO_BANCO))).findFirst().orElse(null);
			if(Objects.isNull(punto)) {
				throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
						ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
			}
			return punto.getNombrePunto();
		}else {
			throw new NegocioException(ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getHttpStatus());
		}
	}

	/**
	 * Metodo que se encarga de obtener el nombre de la transportadora de la lista de transportadoras
	 * @param codigoFondoTDV
	 * @return String
	 * @author cesar.castano
	 */
	private String getNombreTransportadora(Integer codigoFondoTDV) {
		FondosDTO fondos = listaFondos.stream().filter(fondo ->
							fondo.getCodigoPunto().equals(codigoFondoTDV)).findFirst().orElse(null);
		if(!Objects.isNull(fondos)) {
			TransportadorasDTO transportadora = listaTransportadoras.stream().filter(trans ->
							trans.getCodigo().equals(fondos.getTdv())).findFirst().orElse(null);
			if(!Objects.isNull(transportadora)) {
				return transportadora.getNombreTransportadora();
			}else {
				throw new NegocioException(ApiResponseCode.ERROR_TRANSPORTADORAS_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_TRANSPORTADORAS_NO_ENCONTRADO.getDescription(),
						ApiResponseCode.ERROR_TRANSPORTADORAS_NO_ENCONTRADO.getHttpStatus());
			}
		}else {
			throw new NegocioException(ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_FONDOS_NO_ENCONTRADO.getHttpStatus());
		}
	}
	
	/**
	 * Metodo que se encarga de obtener el nombre del punto de la lista de puntos
	 * @param tipoPunto
	 * @param codigoPunto
	 * @return String
	 * @author cesar.castano
	 */
	private String getNombrePunto(String tipoPunto, Integer codigoPunto) {
		PuntosDTO puntos = listaPuntos.stream().filter(punto -> 
			punto.getTipoPunto().equals(tipoPunto) &&
			punto.getCodigoPunto().equals(codigoPunto)).findFirst().orElse(null);
		if(!Objects.isNull(puntos)) {
			return puntos.getNombrePunto();
		}else {
			throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		}
	}
	
	/**
	 * Metodo que se encarga de obtener el nombre de la ciudad de la lista de ciudades
	 * @param tipoPunto
	 * @param codigoPunto
	 * @return String
	 * @author cesar.castano
	 */
	private String getNombreCiudad(String tipoPunto, Integer codigoPunto) {
		PuntosDTO puntos = listaPuntos.stream().filter(punto -> 
					punto.getTipoPunto().equals(tipoPunto) &&
					punto.getCodigoPunto().equals(codigoPunto)).findFirst().orElse(null);
		if(!Objects.isNull(puntos)) {
			CiudadesDTO ciudad = listaCiudades.stream().filter(ciud ->
					ciud.getCodigoDANE().equals(puntos.getCodigoCiudad())).findFirst().orElse(null);
			if(Objects.isNull(ciudad)) {
				throw new NegocioException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription(),
						ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());
			}else {
				return ciudad.getNombreCiudad();
			}
		}else {
			throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
				ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription(),
				ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		}
	}
}
