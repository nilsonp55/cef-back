package com.ath.adminefectivo.service;

import java.util.List;
import com.querydsl.core.types.Predicate;
import com.ath.adminefectivo.dto.AuditoriaLoginDTO;
import com.ath.adminefectivo.entities.AuditoriaLogin;

public interface AuditoriaLoginService {

	List<AuditoriaLoginDTO> getAllAuditoriaLogin(Predicate predicate);
	
	AuditoriaLoginDTO saveAuditoriaLogin(AuditoriaLogin auditoriaLogin); 
}
