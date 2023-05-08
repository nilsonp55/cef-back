package com.ath.adminefectivo.delegate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IEncriptarDelegate;
import com.ath.adminefectivo.service.IEncriptarService;

@Service
public class EncriptarDelegateImpl implements IEncriptarDelegate{

	@Autowired
	IEncriptarService encriptarService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String encriptarArchivo(String path, String nombreArchivo) {
		return encriptarService.encriptarArchivo(path, nombreArchivo);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String desencriptarArchivo(String path, String nombreArchivo) {
		return encriptarService.desencriptarArchivo(path, nombreArchivo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generarLlaves() {
		return encriptarService.generarLlaves();
	}
	

	
}
