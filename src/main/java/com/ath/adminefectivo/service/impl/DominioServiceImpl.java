package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.DominioDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Dominio;
import com.ath.adminefectivo.entities.id.DominioPK;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.DominioRepository;
import com.ath.adminefectivo.service.IDominioService;
import com.querydsl.core.types.Predicate;

import lombok.EqualsAndHashCode;

@Service
@EqualsAndHashCode
public class DominioServiceImpl implements IDominioService {

	@Autowired
	DominioRepository dominioRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DominioDTO> getDominios(Predicate predicate) {
		var dominios = dominioRepository.findAll(predicate);
		List<DominioDTO> listArchivosDto = new ArrayList<>();
		dominios.forEach(entity -> listArchivosDto.add(DominioDTO.CONVERTER_DTO.apply(entity)));
		return listArchivosDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String valorTextoDominio(String dominio, String codigo) {
		var dominioOpt = dominioRepository.findById(DominioPK.builder().dominio(dominio).codigo(codigo).build());

		if (dominioOpt.isPresent() && Objects.nonNull(dominioOpt.get().getValorTexto())) {
			return dominioOpt.get().getValorTexto();
		} else {
			throw new AplicationException(ApiResponseCode.ERROR_DOMINIO_NOT_FOUND.getCode(),
					ApiResponseCode.ERROR_DOMINIO_NOT_FOUND.getDescription(),
					ApiResponseCode.ERROR_DOMINIO_NOT_FOUND.getHttpStatus());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double valorNumericoDominio(String dominio, String codigo) {
		var dominioOpt = dominioRepository.findById(DominioPK.builder().dominio(dominio).codigo(codigo).build());

		if (dominioOpt.isPresent() && Objects.nonNull(dominioOpt.get().getValorNumero())) {
			return dominioOpt.get().getValorNumero();
		} else {
			throw new AplicationException(ApiResponseCode.ERROR_DOMINIO_NOT_FOUND.getCode(),
					ApiResponseCode.ERROR_DOMINIO_NOT_FOUND.getDescription(),
					ApiResponseCode.ERROR_DOMINIO_NOT_FOUND.getHttpStatus());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> consultaListValoresPorDominio(String dominio) {
		var dominios = dominioRepository.findByDominioPKDominio(dominio);
		return dominios.stream().filter(x -> Objects.nonNull(x.getValorTexto())).map(Dominio::getValorTexto).toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Double> consultaListValoresNumPorDominio(String dominio) {
		var dominios = dominioRepository.findByDominioPKDominio(dominio);
		return dominios.stream().filter(x -> Objects.nonNull(x.getValorTexto())).map(Dominio::getValorNumero).toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String persistirDominio(DominioDTO dominioDto) {
		if(!Objects.isNull(dominioRepository.save(DominioDTO.CONVERTER_ENTITY.apply(dominioDto)))) {
			return "Dominio Creado con éxito";
		}
		return "No se pudo insertar el dominio";
	}

	@Override
	public Boolean eliminarDominio(DominioPK dominioPK) {
		Dominio dominioEliminar = dominioRepository.findById(dominioPK).get();
		if(!Objects.isNull(dominioEliminar)) {
			dominioEliminar.setEstado(Constantes.ESTADO_REGISTRO_INACTIVO.toString());
			dominioRepository.save(dominioEliminar);
			return true;
		}
		return false;
		
	}

}
