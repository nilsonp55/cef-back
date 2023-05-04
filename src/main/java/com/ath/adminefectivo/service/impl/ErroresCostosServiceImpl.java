package com.ath.adminefectivo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ErroresCostos;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.ErroresCostosRepository;
import com.ath.adminefectivo.service.ErroresCostosService;

@Service
public class ErroresCostosServiceImpl implements ErroresCostosService {

	@Autowired
	ErroresCostosRepository erroresCostosRepository;
	
	@Override
	public List<ErroresCostos> obtenerErroresCostosByIdSeqGrupo(Integer idSeqGrupo) {
		try {
			return erroresCostosRepository.findBySeqGrupo(idSeqGrupo);
		} catch (Exception e) {
			throw new AplicationException(ApiResponseCode.ERROR_LIQUIDACION_COSTOS.getCode(),
					ApiResponseCode.ERROR_LIQUIDACION_COSTOS.getDescription(),
					ApiResponseCode.ERROR_LIQUIDACION_COSTOS.getHttpStatus());		}
	}

}
