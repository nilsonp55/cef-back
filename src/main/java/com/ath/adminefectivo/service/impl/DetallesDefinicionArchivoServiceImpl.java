package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.DetallesDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.id.DetallesDefinicionArchivoPK;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.DetallesDefinicionArchivoRepository;
import com.ath.adminefectivo.service.IDetalleDefinicionArchivoService;

@Service
public class DetallesDefinicionArchivoServiceImpl implements IDetalleDefinicionArchivoService {

	@Autowired
	public DetallesDefinicionArchivoRepository detallesDefinicionArchivoRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DetallesDefinicionArchivoDTO consultarDetalleDefinicionArchivoById(
			DetallesDefinicionArchivoPK detallesDefinicionArchivoPK) {
		var detalleDefinicion = detallesDefinicionArchivoRepository.findById(detallesDefinicionArchivoPK);

		if (detalleDefinicion.isPresent()) {
			return DetallesDefinicionArchivoDTO.CONVERTER_DTO.apply(detalleDefinicion.get());
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_DETALLE_DEFINICION_NO_VALIDO.getCode(),
					ApiResponseCode.ERROR_DETALLE_DEFINICION_NO_VALIDO.getDescription(),
					ApiResponseCode.ERROR_DETALLE_DEFINICION_NO_VALIDO.getHttpStatus());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DetallesDefinicionArchivoDTO> consultarDetalleDefinicionArchivoByIdMaestro(String idMaestro) {
		var detalleDefinicion = detallesDefinicionArchivoRepository.findByIdIdArchivoOrderByNumeroCampoAsc(idMaestro);
		
		if (!detalleDefinicion.isEmpty()) {
			List<DetallesDefinicionArchivoDTO> listDetalleDefinicionDto = new ArrayList<>();
			detalleDefinicion.forEach(
					entity -> listDetalleDefinicionDto.add(DetallesDefinicionArchivoDTO.CONVERTER_DTO.apply(entity)));
			return listDetalleDefinicionDto;
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_DETALLE_DEFINICION_NO_VALIDO.getCode(),
					ApiResponseCode.ERROR_DETALLE_DEFINICION_NO_VALIDO.getDescription(),
					ApiResponseCode.ERROR_DETALLE_DEFINICION_NO_VALIDO.getHttpStatus());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DetallesDefinicionArchivoDTO> consultarDetalleDefinicionArchivoByIdArchivoNumeroCampo(String idArchivo,
			Integer numeroCampo) {
		var detalleDefinicion = detallesDefinicionArchivoRepository.findByIdIdArchivoAndIdNumeroCampo(idArchivo, numeroCampo);
		
		if (!detalleDefinicion.isEmpty()) {
			List<DetallesDefinicionArchivoDTO> listDetalleDefinicionDto = new ArrayList<>();
			detalleDefinicion.forEach(
					entity -> listDetalleDefinicionDto.add(DetallesDefinicionArchivoDTO.CONVERTER_DTO.apply(entity)));
			return listDetalleDefinicionDto;
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_DETALLE_DEFINICION_NO_VALIDO.getCode(),
					ApiResponseCode.ERROR_DETALLE_DEFINICION_NO_VALIDO.getDescription(),
					ApiResponseCode.ERROR_DETALLE_DEFINICION_NO_VALIDO.getHttpStatus());
		}
	}

}
