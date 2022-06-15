package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.ICajerosDelegate;
import com.ath.adminefectivo.dto.CajerosDTO;
import com.ath.adminefectivo.service.ICajerosService;
import com.querydsl.core.types.Predicate;

@Service
public class CajerosDelegateImpl implements ICajerosDelegate {

	@Autowired
	ICajerosService cajerosService;
	
	@Override
	public List<CajerosDTO> getCajeros(Predicate predicate) {
		
		return cajerosService.getCajeros(predicate);
		
	}

}
