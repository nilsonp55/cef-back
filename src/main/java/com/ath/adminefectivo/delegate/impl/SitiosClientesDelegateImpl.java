package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.ISitiosClientesDelegate;
import com.ath.adminefectivo.dto.SitiosClientesDTO;
import com.ath.adminefectivo.service.ISitiosClientesService;
import com.querydsl.core.types.Predicate;

@Service
public class SitiosClientesDelegateImpl implements ISitiosClientesDelegate{

	@Autowired
	ISitiosClientesService sitiosClientesService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SitiosClientesDTO> getSitiosClientes(Predicate predicate) {
		
		return sitiosClientesService.getSitiosClientes(predicate);
	}

}
