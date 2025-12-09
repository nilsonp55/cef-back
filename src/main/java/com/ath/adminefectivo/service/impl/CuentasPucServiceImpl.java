package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import lombok.extern.log4j.Log4j2;

/**
 * Interfaz de los servicios a la tabla de CuentasPucService
 * @author bayronPerez
 */

@Log4j2
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
		Optional<CuentasPuc> cuentas = iCuentasPucRepository.findById(idCuentasPuc);
		if (cuentas.isEmpty()) {
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
		if (cuentasPucDTO.getIdCuentasPuc() == null
				&& !iCuentasPucRepository.existsById(cuentasPucDTO.getIdCuentasPuc())) {
			log.error("Throw exception, Id cuentasPUC no existe: {}", cuentasPucDTO.getIdCuentasPuc());
			throw new ConflictException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getDescription());
		}
		log.debug("Eliminar id cuentasPUC: {}", cuentasPucDTO.getIdCuentasPuc());
		iCuentasPucRepository.deleteById(cuentasPucDTO.getIdCuentasPuc());
		log.debug("Se elimino Id cuentasPUC: {}", cuentasPucDTO.getIdCuentasPuc());
	}

}
