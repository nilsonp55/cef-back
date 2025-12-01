package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.AuditoriaLogDTO;
import com.ath.adminefectivo.dto.AuditoriaLoginDTO;
import com.ath.adminefectivo.entities.AuditoriaLogin;
import com.querydsl.core.types.Predicate;

public interface AuditoriaLoginService {

	List<AuditoriaLoginDTO> getAllAuditoriaLogin(Predicate predicate);
	
	AuditoriaLoginDTO saveAuditoriaLogin(AuditoriaLogin auditoriaLogin); 
}
