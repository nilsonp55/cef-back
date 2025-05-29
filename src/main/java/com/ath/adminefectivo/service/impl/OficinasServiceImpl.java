package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.OficinasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.IOficinasRepository;
import com.ath.adminefectivo.repositories.jdbc.IOficinasJdbcRepository;
import com.ath.adminefectivo.service.IOficinasService;
import com.querydsl.core.types.Predicate;

@Service
public class OficinasServiceImpl implements IOficinasService {

	@Autowired
	IOficinasRepository oficinasRepository;
	
	@Autowired
	IOficinasJdbcRepository oficinasJdbcRepository;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OficinasDTO> getOficinas(Predicate predicate) {
		var oficinas = oficinasRepository.findAll(predicate);
		List<OficinasDTO> listOficinasDto = new ArrayList<>();
		oficinas.forEach(entity -> listOficinasDto.add(OficinasDTO.CONVERTER_DTO.apply(entity)));
		return listOficinasDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getCodigoPunto(Integer codigoOficina, Integer codigoBancoAVAL) {
		var oficinaOpt = oficinasRepository.findByCodigoOficinaAndBancoAVAL(codigoOficina, codigoBancoAVAL);
		if (Objects.isNull(oficinaOpt)) {
			throw new AplicationException(ApiResponseCode.ERROR_OFICINAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_OFICINAS_NO_ENCONTRADO.getDescription()+ " con el codigo oficina = "+codigoBancoAVAL+" con banco Aval codigo = "+codigoBancoAVAL,
					ApiResponseCode.ERROR_OFICINAS_NO_ENCONTRADO.getHttpStatus());
		} else {
			return oficinaOpt.getCodigoPunto();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getCodigoPuntoOficina(Integer codigoPunto) {
		Boolean estado = true;
		var oficinaOpt = oficinasRepository.findByCodigoPunto(codigoPunto);
		if (Objects.isNull(oficinaOpt)) {
			estado = false;
		}
		return estado;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getCodigoPuntoOficinaJdbc(Integer codigoPunto) {
		return oficinasJdbcRepository.existsByCodigoPunto(codigoPunto);
	}
}
