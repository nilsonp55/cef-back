package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.TiposCentrosCostosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.TiposCentrosCostos;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.repositories.ITiposCentrosCostosRepository;
import com.ath.adminefectivo.service.ITiposCentrosCostosService;
import com.querydsl.core.types.Predicate;

/**
 * Servicios para gestionar los TiposCentrosCostosService
 * @author Bayron Perez
 */

@Service
public class TiposCentrosCostosServiceImpl implements ITiposCentrosCostosService {

	@Autowired
	ITiposCentrosCostosRepository tiposCentrosCostosRepository;
	
	
	@Override
	public List<TiposCentrosCostosDTO> getAllTiposCentrosCostos(Predicate predicate) {
		var cuentas = tiposCentrosCostosRepository.findAll(predicate);
		List<TiposCentrosCostosDTO> listcuentasDto = new ArrayList<>();
		cuentas.forEach(entity -> listcuentasDto.add(TiposCentrosCostosDTO.CONVERTER_DTO.apply(entity)));
		return listcuentasDto;
	}

	@Override
	public TiposCentrosCostosDTO getTiposCentrosCostosById(String idTiposCentrosCostos) {
		var centros = tiposCentrosCostosRepository.findById(idTiposCentrosCostos);
		if (Objects.isNull(centros)) {
			throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription()+ "no encontrado para tipos centros costos con id = "+idTiposCentrosCostos,
					ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus());
		} 
		return TiposCentrosCostosDTO.CONVERTER_DTO.apply(centros.get());
	}

	@Override
	public TiposCentrosCostosDTO saveTiposCentrosCostos(TiposCentrosCostosDTO tiposCentrosCostosDTO) {
		if (tiposCentrosCostosDTO.getTipoCentro() != null && tiposCentrosCostosRepository
				.existsById(tiposCentrosCostosDTO.getTipoCentro())) {		
			throw new ConflictException(ApiResponseCode.ERROR_TIPOS_CUENTAS_EXIST.getDescription());		
		}
		TiposCentrosCostos tiposCentrosCostosParm = TiposCentrosCostosDTO.CONVERTER_ENTITY.apply(tiposCentrosCostosDTO);
		TiposCentrosCostos tiposCentrosCostos = tiposCentrosCostosRepository.save(tiposCentrosCostosParm);
		
		return TiposCentrosCostosDTO.CONVERTER_DTO.apply(tiposCentrosCostos);
	}

	@Override
	public TiposCentrosCostosDTO putTiposCentrosCostos(TiposCentrosCostosDTO tiposCentrosCostosDTO) {
		if (tiposCentrosCostosDTO.getTipoCentro() == null && !tiposCentrosCostosRepository
				.existsById(tiposCentrosCostosDTO.getTipoCentro())) {		
			throw new ConflictException(ApiResponseCode.ERROR_TIPOS_CUENTAS_NO_EXIST.getDescription());		
		}
		TiposCentrosCostos tiposCentrosCostos = tiposCentrosCostosRepository.save(TiposCentrosCostosDTO.CONVERTER_ENTITY
				.apply(tiposCentrosCostosDTO));
		
		return TiposCentrosCostosDTO.CONVERTER_DTO.apply(tiposCentrosCostos);
	}

	@Override
	public void deleteTiposCentrosCostosById(TiposCentrosCostosDTO tiposCentrosCostosDTO) {
		if (tiposCentrosCostosDTO.getTipoCentro() == null && !tiposCentrosCostosRepository
				.existsById(tiposCentrosCostosDTO.getTipoCentro())) {		
			throw new ConflictException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getDescription());		
		}
		tiposCentrosCostosRepository.delete(TiposCentrosCostosDTO.CONVERTER_ENTITY.apply(tiposCentrosCostosDTO));
	}

}
