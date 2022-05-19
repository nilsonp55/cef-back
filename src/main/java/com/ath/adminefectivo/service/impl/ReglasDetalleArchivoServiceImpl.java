package com.ath.adminefectivo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.ReglasDetalleArchivoDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.ReglasDetalleArchivoRepository;
import com.ath.adminefectivo.service.IReglasDetalleArchivoService;
import com.ath.adminefectivo.utils.UtilsObjects;

@Service
public class ReglasDetalleArchivoServiceImpl implements IReglasDetalleArchivoService {

	@Autowired
	ReglasDetalleArchivoRepository reglasDetalleArchivoRepository;
	
	/**
	 * 
	 * @param regla
	 * @return
	 */
	@Override
	public ReglasDetalleArchivoDTO  buscarRegla(Integer regla) {

		var reglas = reglasDetalleArchivoRepository.findById(regla);
		if(reglas.isPresent()) {
			ReglasDetalleArchivoDTO reglasDetalleArchivoDTO = new ReglasDetalleArchivoDTO();
			UtilsObjects.copiarPropiedades(reglas.get(), reglasDetalleArchivoDTO);	
			return ReglasDetalleArchivoDTO.CONVERTER_DTO.apply(reglas.get());
		}else {
			throw new AplicationException(ApiResponseCode.ERROR_REGLA_NOT_FOUND.getCode(),
					ApiResponseCode.ERROR_REGLA_NOT_FOUND.getDescription(),
					ApiResponseCode.ERROR_REGLA_NOT_FOUND.getHttpStatus());
		}
	}
	
}
