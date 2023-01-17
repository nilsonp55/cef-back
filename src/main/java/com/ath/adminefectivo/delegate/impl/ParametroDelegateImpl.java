package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IParametroDelegate;
import com.ath.adminefectivo.dto.ParametroDTO;
import com.ath.adminefectivo.service.IParametroService;
import com.querydsl.core.types.Predicate;

@Service
public class ParametroDelegateImpl implements IParametroDelegate{
	
	@Autowired
	IParametroService parametroService;
	

	@Override
	public List<ParametroDTO> getParametros(Predicate predicate) {
		return parametroService.getParametros(predicate);
	}

}
