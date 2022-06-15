package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IClientesCorporativosDelegate;
import com.ath.adminefectivo.dto.ClientesCorporativosDTO;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.querydsl.core.types.Predicate;

@Service
public class ClientesCorporativosDelegateImpl implements IClientesCorporativosDelegate{

	@Autowired
	IClientesCorporativosService clientesCorporativosService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ClientesCorporativosDTO> getClientesCorporativos(Predicate predicate) {
		
		return clientesCorporativosService.getClientesCorporativos(predicate);
	}
}
