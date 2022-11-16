package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IBancosDelegate;
import com.ath.adminefectivo.delegate.IEncriptarDelegate;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.IEncriptarService;
import com.querydsl.core.types.Predicate;

@Service
public class EncriptarDelegateImpl implements IEncriptarDelegate{

	@Autowired
	IEncriptarService encriptarService;

	@Override
	public String encriptarArchivo(String path, String nombreArchivo) {
		return encriptarService.encriptarArchivo(path, nombreArchivo);
	}
	
	@Override
	public String desencriptarArchivo(String path, String nombreArchivo) {
		return encriptarService.desencriptarArchivo(path, nombreArchivo);
	}
	

	
}
