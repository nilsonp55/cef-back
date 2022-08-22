package com.ath.adminefectivo.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IDetalleOperacionesProgramadasRepository;
import com.ath.adminefectivo.service.IDetalleOperacionesProgramadasService;

@Service
public class DetalleOperacionesProgramadasServiceImpl implements IDetalleOperacionesProgramadasService {

	@Autowired
	IDetalleOperacionesProgramadasRepository detalleOperacionesProgramadasRepository;

	@Override
	public Double obtenerValorDetalle(Integer idOpreacion) {
		Double valorDetalle = detalleOperacionesProgramadasRepository.valorTotal(idOpreacion);
		if(Objects.isNull(valorDetalle)) {
			throw new NegocioException(ApiResponseCode.ERROR_DETALLE_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_DETALLE_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_DETALLE_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
		}else {
			return valorDetalle;
		}
	}
}
