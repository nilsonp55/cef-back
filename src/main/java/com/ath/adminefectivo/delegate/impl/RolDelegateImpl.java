package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IRolDelegate;
import com.ath.adminefectivo.dto.RolDTO;
import com.ath.adminefectivo.service.IRolService;

@Service
public class RolDelegateImpl implements IRolDelegate{
	
	@Autowired
	IRolService rolService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RolDTO> getRoles() {
		return rolService.getRoles();
	}

}
