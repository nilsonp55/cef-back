package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IBancosDelegate;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.service.IBancosService;
import com.querydsl.core.types.Predicate;

@Service
public class BancosDelegateImpl implements IBancosDelegate{

	@Autowired
	IBancosService bancosService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BancosDTO> getBancos(Predicate predicate) {

		return bancosService.getBancos(predicate);
	}
	
}
