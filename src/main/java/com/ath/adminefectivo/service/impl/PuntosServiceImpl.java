package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.EscalasDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CajerosATM;
import com.ath.adminefectivo.entities.Escalas;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.Oficinas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.CajerosATMRepository;
import com.ath.adminefectivo.repositories.IBancosRepository;
import com.ath.adminefectivo.repositories.IFondosRepository;
import com.ath.adminefectivo.repositories.IOficinasRepository;
import com.ath.adminefectivo.repositories.IPuntosRepository;
import com.ath.adminefectivo.repositories.ISitiosClientesRepository;
import com.ath.adminefectivo.service.IPuntosService;
import com.querydsl.core.types.Predicate;

@Service
public class PuntosServiceImpl implements IPuntosService {

	@Autowired
	IPuntosRepository puntosRepository;

	@Autowired
	IBancosRepository bancosRepository;
	
	@Autowired
	IOficinasRepository oficinasRepository;

	@Autowired
	CajerosATMRepository cajerosATMRepository;
	
	@Autowired
	ISitiosClientesRepository sitiosClienteRepository;
	
	@Autowired
	IFondosRepository fondosRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<PuntosDTO> getPuntos(Predicate predicate, Pageable page) {
		
		Page<Puntos> puntos = puntosRepository.findAll(predicate, page);
		
		return new PageImpl<>(puntos.getContent().stream().map(PuntosDTO
		.CONVERTER_DTO).toList(), puntos.getPageable(), puntos.getTotalElements());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PuntosDTO> getPuntos(Predicate predicate) {
		var puntos = puntosRepository.findAll(predicate);
		List<PuntosDTO> listPuntosDto = new ArrayList<>();
		puntos.forEach(entity -> listPuntosDto.add(PuntosDTO.CONVERTER_DTO.apply(entity)));
		return listPuntosDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Puntos getEntidadPunto(String tipoPunto, Integer codigoPunto) {
		var puntosOpt = puntosRepository.findByCodigoPuntoAndTipoPunto(codigoPunto, tipoPunto);
		if (Objects.isNull(puntosOpt)) {
			throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription()+ " punto no encontrado para tipoPunto = "+tipoPunto+" codigoPunto = "+codigoPunto,
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		} 
		return puntosOpt;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNombrePunto(String tipoPunto, Integer codigoPunto) {
		System.out.println("String tipoPunto, Integer codigoPunto = "+tipoPunto+ "  "+codigoPunto);
		var puntosOpt = puntosRepository.findByCodigoPuntoAndTipoPunto(codigoPunto, tipoPunto);
		if (Objects.isNull(puntosOpt)) { 
			throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription()+ " punto no encontrado para tipoPunto = "+tipoPunto+" codigoPunto = "+codigoPunto,
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		} 
		return puntosOpt.getNombrePunto();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNombrePunto(Integer codigoPunto) {
		var puntosOpt = puntosRepository.findByCodigoPunto(codigoPunto);
		if (Objects.isNull(puntosOpt)) { 
			throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription()+ " punto no encontrado para codigoPunto = "+codigoPunto,
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		} 
		return puntosOpt.getNombrePunto();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getcodigoPunto(String tipoPunto, String nombrePunto) {
		var puntosOpt = puntosRepository.findByTipoPuntoAndNombrePunto(tipoPunto, nombrePunto);
		if (Objects.isNull(puntosOpt)) {
			throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription()+ " punto no encontrado para tipoPunto = "+tipoPunto+" nombrePunto = "+nombrePunto,
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		} 
		return puntosOpt.getCodigoPunto();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Puntos guardarPuntoBanco(Puntos punto, Bancos banco) {
		if (punto.getCodigoPunto() != null && puntosRepository
				.existsById(punto.getCodigoPunto())) {		
			throw new ConflictException(ApiResponseCode.ERROR_PUNTO_EXIST.getDescription());		
		}
		Puntos puntoResponse = puntosRepository.save(punto);
		
		if (banco.getCodigoPunto() != null && bancosRepository
				.existsById(banco.getCodigoPunto())) {		
			throw new ConflictException(ApiResponseCode.ERROR_BANCO_EXIST.getDescription());		
		}
		
		banco.setCodigoPunto(puntoResponse.getCodigoPunto());
		bancosRepository.save(banco);
		
		return puntoResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Puntos guardarPuntoOficina(Puntos punto, Oficinas oficina) {
		if (punto.getCodigoPunto() != null && puntosRepository
				.existsById(punto.getCodigoPunto())) {		
			throw new ConflictException(ApiResponseCode.ERROR_PUNTO_EXIST.getDescription());		
		}
		Puntos puntoResponse = puntosRepository.save(punto);
		
		if (oficina.getCodigoPunto() != null && oficinasRepository
				.existsById(oficina.getCodigoPunto())) {		
			throw new ConflictException(ApiResponseCode.ERROR_OFICINA_EXIST.getDescription());		
		}
		
		oficina.setCodigoPunto(puntoResponse.getCodigoPunto());
		oficinasRepository.save(oficina);
		
		return puntoResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Puntos guardarPuntoCajeroATM(Puntos punto, CajerosATM cajerosATM) {
		if (punto.getCodigoPunto() != null && puntosRepository
				.existsById(punto.getCodigoPunto())) {		
			throw new ConflictException(ApiResponseCode.ERROR_PUNTO_EXIST.getDescription());		
		}
		Puntos puntoResponse = puntosRepository.save(punto);
		
		if (cajerosATM.getCodigoPunto() != null && cajerosATMRepository
				.existsById(cajerosATM.getCodigoPunto())) {		
			throw new ConflictException(ApiResponseCode.ERROR_CAJERO_EXIST.getDescription());		
		}
		
		cajerosATM.setCodigoPunto(puntoResponse.getCodigoPunto());
		cajerosATMRepository.save(cajerosATM);
		
		return puntoResponse;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosDTO getPuntoByNombrePunto(String nombrePunto) {
		var punto = puntosRepository.findByNombrePunto(nombrePunto);
		if(!Objects.isNull(punto)) {
			return PuntosDTO.CONVERTER_DTO.apply(punto);
		}
		return null;
	}
	
	public Puntos guardarPuntoSitioCliente(Puntos punto, SitiosClientes sitiosClientes) {
		if (punto.getCodigoPunto() != null && puntosRepository
				.existsById(punto.getCodigoPunto())) {		
			throw new ConflictException(ApiResponseCode.ERROR_PUNTO_EXIST.getDescription());		
		}
		Puntos puntoResponse = puntosRepository.save(punto);
		
		if (sitiosClientes.getCodigoPunto() != null && sitiosClienteRepository
				.existsById(sitiosClientes.getCodigoPunto())) {		
			throw new ConflictException(ApiResponseCode.ERROR_SITIO_CLIENTE_EXIST.getDescription());		
		}
		
		sitiosClientes.setCodigoPunto(puntoResponse.getCodigoPunto());
		sitiosClienteRepository.save(sitiosClientes);
		
		return puntoResponse;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Puntos guardarPuntoFondo(Puntos punto, Fondos fondo) {
		if (punto.getCodigoPunto() != null && puntosRepository
				.existsById(punto.getCodigoPunto())) {		
			throw new ConflictException(ApiResponseCode.ERROR_PUNTO_EXIST.getDescription());		
		}
		Puntos puntoResponse = puntosRepository.save(punto);
		
		if (fondo.getCodigoPunto() != null && fondosRepository
				.existsById(fondo.getCodigoPunto())) {		
			throw new ConflictException(ApiResponseCode.ERROR_FONDO_EXIST.getDescription());		
		}
		
		fondo.setCodigoPunto(puntoResponse.getCodigoPunto());
		fondosRepository.save(fondo);
		
		return puntoResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosDTO getPuntoByTipoPuntoAndCodigoCiudad(String tipoPunto, String codigoCiudad) {
		var punto = puntosRepository.findByTipoPuntoAndCodigoCiudad(tipoPunto, codigoCiudad);
		if(!Objects.isNull(punto)) {
			return PuntosDTO.CONVERTER_DTO.apply(punto);
		}
		return null;
	}
	/**
	 * {@inheritDoc}
	 */
	public Puntos getPuntoById(Integer idPunto) {
		try {
			return puntosRepository.findById(idPunto).get();
		} catch (Exception e) {
			throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription()+" no encontrado para idPunto = "+idPunto,
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		}
		 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getEntidadPuntoBanrep(String tipoPunto, Integer codigoPunto) {
		var puntosOpt = puntosRepository.findByCodigoPuntoAndTipoPunto(codigoPunto, tipoPunto);
		if (Objects.isNull(puntosOpt)) {
			return false;
		} 
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Puntos getEntidadPunto(Integer codigo_banco_aval) {
		Puntos puntos = puntosRepository.obtenerCodigoPunto(codigo_banco_aval);
		if (Objects.isNull(puntos)) {
			throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription() +" no encontrado para codigoBancoAval = "+codigo_banco_aval,
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		} else
			return puntos;
	}

}
