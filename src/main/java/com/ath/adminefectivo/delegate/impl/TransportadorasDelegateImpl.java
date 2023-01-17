package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.ITransportadorasDelegate;
import com.ath.adminefectivo.dto.TransportadorasDTO;
import com.ath.adminefectivo.service.ITransportadorasService;
import com.querydsl.core.types.Predicate;

@Service
public class TransportadorasDelegateImpl implements ITransportadorasDelegate{

	@Autowired
	ITransportadorasService transportadorasService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransportadorasDTO> getTransportadoras(Predicate predicate) {
		
		return transportadorasService.getTransportadoras(predicate);
		
	}

}
