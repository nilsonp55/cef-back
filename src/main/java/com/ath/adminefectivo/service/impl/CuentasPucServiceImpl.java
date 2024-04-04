package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.CuentasPucDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.CuentasPuc;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.repositories.ICuentasPucRepository;
import com.ath.adminefectivo.service.ICuentasPucService;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de los servicios a la tabla de CuentasPucService
 * @author bayronPerez
 */

@Service
public class CuentasPucServiceImpl implements ICuentasPucService {

	@Autowired
	ICuentasPucRepository iCuentasPucRepository;
	
	
	@Override
	public List<CuentasPucDTO> getCuentasPuc(Predicate predicate) {
		var cuentas = iCuentasPucRepository.findAll(predicate);
		List<CuentasPucDTO> listcuentasDto = new ArrayList<>();
		cuentas.forEach(entity -> listcuentasDto.add(CuentasPucDTO.CONVERTER_DTO.apply(entity)));
		return listcuentasDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CuentasPucDTO getCuentasPucById(Long idCuentasPuc) {
		var cuentas = iCuentasPucRepository.findById(idCuentasPuc);
		if (Objects.isNull(cuentas)) {
			throw new AplicationException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getCode(),
					ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getDescription(),
					ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getHttpStatus());
		} 
		return CuentasPucDTO.CONVERTER_DTO.apply(cuentas.get());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CuentasPucDTO postCuentasPuc(CuentasPucDTO cuentasPucDTO) {
		if (cuentasPucDTO.getIdCuentasPuc() != null && iCuentasPucRepository
				.existsById(cuentasPucDTO.getIdCuentasPuc())) {		
			throw new ConflictException(ApiResponseCode.ERROR_CUENTAS_PUC_EXIST.getDescription());		
		}
		CuentasPuc cuentasPucre = CuentasPucDTO.CONVERTER_ENTITY
				.apply(cuentasPucDTO); 
		CuentasPuc cuentasPuc = iCuentasPucRepository.save(cuentasPucre);
		
		return CuentasPucDTO.CONVERTER_DTO.apply(cuentasPuc);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CuentasPucDTO putCuentasPuc(CuentasPucDTO cuentasPucDTO) {
		if (cuentasPucDTO.getIdCuentasPuc() == null && !iCuentasPucRepository
				.existsById(cuentasPucDTO.getIdCuentasPuc())) {		
			throw new ConflictException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getDescription());		
		}
		CuentasPuc cuentasPuc = iCuentasPucRepository.save(CuentasPucDTO.CONVERTER_ENTITY
				.apply(cuentasPucDTO));
		
		return CuentasPucDTO.CONVERTER_DTO.apply(cuentasPuc);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteCuentasPuc(CuentasPucDTO cuentasPucDTO) {
		if (cuentasPucDTO.getIdCuentasPuc() == null && !iCuentasPucRepository
				.existsById(cuentasPucDTO.getIdCuentasPuc())) {		
			throw new ConflictException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getDescription());		
		}
		iCuentasPucRepository.delete(CuentasPucDTO.CONVERTER_ENTITY.apply(cuentasPucDTO));
	}

}
