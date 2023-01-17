package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IFondosDelegate;
import com.ath.adminefectivo.dto.FondosDTO;
import com.ath.adminefectivo.service.IFondosService;
import com.querydsl.core.types.Predicate;

@Service
public class FondosDelegateImpl implements IFondosDelegate {

	@Autowired
	IFondosService fondosService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FondosDTO> getFondos(Predicate predicate) {
		return fondosService.getFondos(predicate);
	}
}
