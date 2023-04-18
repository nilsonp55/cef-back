package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.AuditoriaLoginDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.AuditoriaLogin;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.repositories.AuditoriaLoginRepository;
import com.ath.adminefectivo.service.AuditoriaLoginService;
import com.querydsl.core.types.Predicate;

@Service
public class AuditoriaLoginServiceImpl implements AuditoriaLoginService {

	@Autowired
	private AuditoriaLoginRepository auditoriaLoginRepository;
	
	@Override
	public List<AuditoriaLoginDTO> getAllAuditoriaLogin(Predicate predicate) {
		var auditorias = auditoriaLoginRepository.findAll(predicate);
		List<AuditoriaLoginDTO> auditoriasDto = new ArrayList<>();
		auditorias.forEach(entity -> {
			auditoriasDto.add(AuditoriaLoginDTO.CONVERTER_DTO.apply(entity));
		});
		return auditoriasDto;
	}

	@Override
	public AuditoriaLoginDTO saveAuditoriaLogin(AuditoriaLogin auditoriaLogin) {
		if (auditoriaLogin.getId() != null && auditoriaLoginRepository
				.existsById(auditoriaLogin.getId())) {		
			throw new ConflictException(ApiResponseCode.ERROR_AUDITORIA_LOGIN_EXIST.getDescription());		
		}
		AuditoriaLogin auditoriaLoginResp = auditoriaLoginRepository.save(auditoriaLogin);
		
		return AuditoriaLoginDTO.CONVERTER_DTO.apply(auditoriaLoginResp);
	}

}
