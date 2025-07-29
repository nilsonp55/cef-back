package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.dto.ConfContableEntidadesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ConfContableEntidades;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.repositories.IConfContableEntidadesRepository;
import com.ath.adminefectivo.service.IConfContableEntidadesService;
import com.querydsl.core.types.Predicate;

/**
 * Servicios para gestionar los tipos de cuentas
 * @author Bayron Perez
 */

@Service
public class ConfContableEntidadesServiceImpl implements IConfContableEntidadesService {

	@Autowired
	IConfContableEntidadesRepository iConfContableEntidadesRepository;
		
	@Override
	public List<ConfContableEntidadesDTO> getAllConfContableEntidades(Predicate predicate) {
		var cuentas = iConfContableEntidadesRepository.findAll(predicate);
		List<ConfContableEntidadesDTO> listcuentasDto = new ArrayList<>();
		cuentas.forEach(entity -> listcuentasDto.add(ConfContableEntidadesDTO.CONVERTER_DTO.apply(entity)));
		return listcuentasDto;
	}

	@Override
	public ConfContableEntidadesDTO getConfContableEntidadesById(Long idConfContableEntidades) {	
		Optional<ConfContableEntidades> cuentas = iConfContableEntidadesRepository.findById(idConfContableEntidades);
		
		if(cuentas.isEmpty()) {
			throw new AplicationException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getCode(),
					ApiResponseCode.ERROR_CONF_CONTABLE_ENTIDAD_EXIST.getDescription(),
					ApiResponseCode.ERROR_CONF_CONTABLE_ENTIDAD_NO_EXIST.getHttpStatus());
		}
		return ConfContableEntidadesDTO.CONVERTER_DTO.apply(cuentas.get());
	}

	@Override
	public ConfContableEntidadesDTO saveConfContableEntidades(ConfContableEntidadesDTO confContableEntidadesDTO) {
		if (confContableEntidadesDTO.getConsecutivo() != null && iConfContableEntidadesRepository
				.existsById(confContableEntidadesDTO.getConsecutivo())) {		
			throw new ConflictException(ApiResponseCode.ERROR_CONF_CONTABLE_ENTIDAD_EXIST.getDescription());		
		}
		ConfContableEntidades confContableEntidades = iConfContableEntidadesRepository.save(ConfContableEntidadesDTO.CONVERTER_ENTITY
				.apply(confContableEntidadesDTO));
		
		return ConfContableEntidadesDTO.CONVERTER_DTO.apply(confContableEntidades);
	}
	
	@Override
	public ConfContableEntidadesDTO putConfContableEntidades(ConfContableEntidadesDTO confContableEntidadesDTO) {
		if (confContableEntidadesDTO.getConsecutivo() == null && !iConfContableEntidadesRepository
				.existsById(confContableEntidadesDTO.getConsecutivo())) {		
			throw new ConflictException(ApiResponseCode.ERROR_CONF_CONTABLE_ENTIDAD_NO_EXIST.getDescription());		
		}
		ConfContableEntidades confContableEntidades = iConfContableEntidadesRepository.save(ConfContableEntidadesDTO.CONVERTER_ENTITY
				.apply(confContableEntidadesDTO));
		
		return ConfContableEntidadesDTO.CONVERTER_DTO.apply(confContableEntidades);

	}

    @Override
    public void deleteConfContableEntidadesById(Long idContableEntidades) {
      Optional<ConfContableEntidades> confContableFind =
          iConfContableEntidadesRepository.findById(idContableEntidades);
      if (confContableFind.isEmpty()) {
        throw new ConflictException(
            ApiResponseCode.ERROR_CONF_CONTABLE_ENTIDAD_NO_EXIST.getDescription());
      }
      iConfContableEntidadesRepository.delete(confContableFind.get());
    }

}
