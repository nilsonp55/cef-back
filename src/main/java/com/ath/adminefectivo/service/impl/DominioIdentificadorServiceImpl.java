package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.DominioIdentificadorDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.DominioIdentificador;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.exception.NotFoundException;
import com.ath.adminefectivo.repositories.DominioIdentificadorReporitory;
import com.ath.adminefectivo.service.IDominioIdentificadorService;

@Service
public class DominioIdentificadorServiceImpl implements IDominioIdentificadorService {

	@Autowired
	DominioIdentificadorReporitory dominioIdentificadorRepository;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DominioIdentificadorDTO> obtenerDominiosIdentificador(String estado) {
		List<DominioIdentificador> dominioIdentificadorResponse = dominioIdentificadorRepository.findByEstado(estado);
		
		List<DominioIdentificadorDTO> listDominioIdentificadorDTO = new ArrayList<>();
		dominioIdentificadorResponse.forEach(entity -> listDominioIdentificadorDTO.add(DominioIdentificadorDTO.CONVERTER_DTO
				.apply(entity)));
		return listDominioIdentificadorDTO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DominioIdentificadorDTO obtenerDominioIdentificadorById(Integer id) {
		if (Objects.isNull(id)) {
			throw new ConflictException(ApiResponseCode.GENERIC_ERROR.getDescription());
		}
		DominioIdentificador dominioIdentificadorResponse = dominioIdentificadorRepository.findById(id).orElseThrow(() 
				-> new NotFoundException("DominioIdentificador", String.valueOf(id)));

		return DominioIdentificadorDTO.CONVERTER_DTO.apply(dominioIdentificadorResponse);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DominioIdentificadorDTO persistirDominioIdentificador(DominioIdentificadorDTO dominioIdentificadorDto) {
		if (!Objects.isNull(dominioIdentificadorDto.getDominio()) && dominioIdentificadorRepository.existsById(dominioIdentificadorDto.getCodigo())) {
			throw new ConflictException(ApiResponseCode.ERROR_DOMINIO_EXIST.getDescription());
		}
		return DominioIdentificadorDTO.CONVERTER_DTO.apply(dominioIdentificadorRepository.
				save(DominioIdentificadorDTO.CONVERTER_ENTITY.apply(dominioIdentificadorDto)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DominioIdentificadorDTO actualizarDominioIdentificador(DominioIdentificadorDTO dominioIdentificadorDto) {
		if (Objects.isNull(dominioIdentificadorDto.getDominio())) {
			throw new ConflictException(ApiResponseCode.ERROR_DOMINIO_NOT_FOUND.getDescription());
		}
		return DominioIdentificadorDTO.CONVERTER_DTO.apply(dominioIdentificadorRepository.
				save(DominioIdentificadorDTO.CONVERTER_ENTITY.apply(dominioIdentificadorDto)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean eliminarDominioIdentificador(Integer id) {
		if (Objects.isNull(id)) {
			throw new ConflictException(ApiResponseCode.ERROR_DOMINIO_NOT_FOUND.getDescription());
		}
		try {
			dominioIdentificadorRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			throw new ConflictException(ApiResponseCode.GENERIC_ERROR.getDescription());
		}
	}

}
