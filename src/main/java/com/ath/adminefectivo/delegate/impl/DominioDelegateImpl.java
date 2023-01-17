package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IDominioDelegate;
import com.ath.adminefectivo.dto.DominioDTO;
import com.ath.adminefectivo.service.IDominioService;
import com.querydsl.core.types.Predicate;

@Service
public class DominioDelegateImpl implements IDominioDelegate {

	@Autowired
	IDominioService dominioService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DominioDTO> getDominios(Predicate predicate) {
		return dominioService.getDominios(predicate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String valorTextoDominio(String dominio, String codigo) {
		return dominioService.valorTextoDominio(dominio, codigo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Double valorNumericoDominio(String dominio, String codigo) {
		return dominioService.valorNumericoDominio(dominio, codigo);
	}

}
