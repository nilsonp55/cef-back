package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.DominioMaestroDto;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.DominioMaestro;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.exception.NotFoundException;
import com.ath.adminefectivo.repositories.DominioMaestroRepository;
import com.ath.adminefectivo.service.IDominioMaestroService;

@Service
public class DominioMaestroServiceImpl implements IDominioMaestroService {

	@Autowired
	DominioMaestroRepository dominioMaestroRepository;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DominioMaestroDto> obtenerDominiosMaestro(String estado) {
		List<DominioMaestro> dominioMaestroResponse = dominioMaestroRepository.findByEstado(estado);
		
		List<DominioMaestroDto> listDominioMaestroDto = new ArrayList<>();
		dominioMaestroResponse.forEach(entity -> listDominioMaestroDto.add(DominioMaestroDto.CONVERTER_DTO
				.apply(entity)));
		return listDominioMaestroDto;
	}
	
	@Override
	public List<DominioMaestroDto> obtenerTodosDominiosMaestro() {
		List<DominioMaestro> dominioMaestroResponse = dominioMaestroRepository.findAll();
		List<DominioMaestroDto> listDominioMaestroDto = new ArrayList<>();
		dominioMaestroResponse.forEach(entity -> listDominioMaestroDto.add(DominioMaestroDto.CONVERTER_DTO
				.apply(entity)));
		return listDominioMaestroDto;
	}

	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DominioMaestroDto obtenerDominioMaestroById(String id) {
		if (Objects.isNull(id)) {
			throw new ConflictException(ApiResponseCode.GENERIC_ERROR.getDescription());
		}
		DominioMaestro dominioMaestroResponse = dominioMaestroRepository.findById(id).orElseThrow(() 
				-> new NotFoundException("DominioMaestro", String.valueOf(id)));

		return DominioMaestroDto.CONVERTER_DTO.apply(dominioMaestroResponse);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DominioMaestroDto persistirDominioMaestro(DominioMaestroDto dominioMaestroDto) {
		if (!Objects.isNull(dominioMaestroDto.getDominio()) && dominioMaestroRepository.existsById(dominioMaestroDto.getDominio())) {
			throw new ConflictException(ApiResponseCode.ERROR_DOMINIO_EXIST.getDescription());
		}
		return DominioMaestroDto.CONVERTER_DTO.apply(dominioMaestroRepository.
				save(DominioMaestroDto.CONVERTER_ENTITY.apply(dominioMaestroDto)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DominioMaestroDto actualizarDominioMaestro(DominioMaestroDto dominioMaestroDto) {
		if (Objects.isNull(dominioMaestroDto.getDominio())) {
			throw new ConflictException(ApiResponseCode.ERROR_DOMINIO_NOT_FOUND.getDescription());
		}
		return DominioMaestroDto.CONVERTER_DTO.apply(dominioMaestroRepository.
				save(DominioMaestroDto.CONVERTER_ENTITY.apply(dominioMaestroDto)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean eliminarDominioMaestro(String id) {
		if (Objects.isNull(id)) {
			throw new ConflictException(ApiResponseCode.ERROR_DOMINIO_NOT_FOUND.getDescription());
		}
		try {
			dominioMaestroRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			throw new ConflictException(ApiResponseCode.GENERIC_ERROR.getDescription());
		}
	}

}
