package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IFuncionesDinamicasDelegate;
import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;
import com.ath.adminefectivo.service.IFuncionesDinamicasService;

@Service
public class FuncionesDinamicasDelegateImpl implements IFuncionesDinamicasDelegate{

	@Autowired
	IFuncionesDinamicasService funcionesDinamicasService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<FuncionesDinamicasDTO> obtenerFuncionesDinamicasActivas() {
		return funcionesDinamicasService.obtenerFuncionesDinamicasActivas();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> ejecutarFuncionDinamica(int idFuncion, List<String> parametros) {
		return funcionesDinamicasService.ejecutarFuncionDinamica(idFuncion, parametros);
	}
	
}
