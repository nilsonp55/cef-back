package com.ath.adminefectivo.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.DetalleOperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.compuestos.DetalleOperacionesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.DetalleOperacionesProgramadas;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IDetalleOperacionesProgramadasRepository;
import com.ath.adminefectivo.service.IDetalleOperacionesProgramadasService;

@Service
public class DetalleOperacionesProgramadasServiceImpl implements IDetalleOperacionesProgramadasService {

	@Autowired
	IDetalleOperacionesProgramadasRepository detalleOperacionesProgramadasRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DetalleOperacionesProgramadas crearRegistroDetalle(DetalleOperacionesDTO detalle) {
		var detalleDTO = new DetalleOperacionesProgramadasDTO();
		detalleDTO.setCalidad(detalle.getCalidad());
		detalleDTO.setDenominacion(detalle.getDenominacion());
		detalleDTO.setFamilia(detalle.getFamilia());
		detalleDTO.setFechaCreacion(new Date());
		detalleDTO.setFechaModificacion(new Date());
		detalleDTO.setIdOperacion(detalle.getIdOperacion());
		detalleDTO.setUsuarioCreacion("User1");
		detalleDTO.setUsuarioModificacion("User1");
		detalleDTO.setValorDetalle(detalle.getValorDetalle());
		return detalleOperacionesProgramadasRepository.save(
				DetalleOperacionesProgramadasDTO.CONVERTER_ENTITY.apply(detalleDTO));
	}

	@Override
	public Double obtenerValorDetalle(Integer idOpreacion) {
		Double valorDetalle = detalleOperacionesProgramadasRepository.valorTotal(idOpreacion);
		if(valorDetalle == null) {
			throw new NegocioException(ApiResponseCode.ERROR_DETALLE_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_DETALLE_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_DETALLE_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
		}else {
			return valorDetalle;
		}
	}

}
