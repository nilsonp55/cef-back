package com.ath.adminefectivo.service.impl;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.DetalleOperacionesProgramadasDTO;
import com.ath.adminefectivo.dto.compuestos.DetalleOperacionesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.DetalleOperacionesProgramadas;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IDetalleOperacionesProgramadasRepository;
import com.ath.adminefectivo.repositories.IOperacionesProgramadasRepository;
import com.ath.adminefectivo.service.IDetalleOperacionesProgramadasService;

@Service
public class DetalleOperacionesProgramadasServiceImpl implements IDetalleOperacionesProgramadasService {

	@Autowired
	IDetalleOperacionesProgramadasRepository detalleOperacionesProgramadasRepository;
	
	@Autowired
	IOperacionesProgramadasRepository operacionesProgramadasRepository;
	
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
		var operacionesP = operacionesProgramadasRepository.getById(detalle.getIdOperacion());
		detalleDTO.setOperaciones(operacionesP);
		detalleDTO.setUsuarioCreacion("User1");
		detalleDTO.setUsuarioModificacion("User1");
		detalleDTO.setValorDetalle(detalle.getValorDetalle());
		DetalleOperacionesProgramadas entidadDetalle = DetalleOperacionesProgramadasDTO.CONVERTER_ENTITY.apply(detalleDTO);
		return detalleOperacionesProgramadasRepository.save(entidadDetalle);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double obtenerValorDetalle(Integer idOperacion) {
		Double valorDetalle = detalleOperacionesProgramadasRepository.valorTotal(idOperacion);
		if(Objects.isNull(valorDetalle)) {
			throw new NegocioException(ApiResponseCode.ERROR_DETALLE_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_DETALLE_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_DETALLE_OPERACIONES_PROGRAMADAS_NO_ENCONTRADO.getHttpStatus());
		}else {
			return valorDetalle;
		}
	}

}
