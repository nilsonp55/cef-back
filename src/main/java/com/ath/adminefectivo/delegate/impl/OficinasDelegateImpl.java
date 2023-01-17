package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IOficinasDelegate;
import com.ath.adminefectivo.dto.OficinasDTO;
import com.ath.adminefectivo.service.IOficinasService;
import com.querydsl.core.types.Predicate;

@Service
public class OficinasDelegateImpl implements IOficinasDelegate {

	@Autowired
	IOficinasService oficinasService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OficinasDTO> getOficinas(Predicate predicate) {
		
		return oficinasService.getOficinas(predicate);
	}
}
