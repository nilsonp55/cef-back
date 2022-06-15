package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IDominioIdentificadorDelegate;
import com.ath.adminefectivo.dto.DominioIdentificadorDTO;
import com.ath.adminefectivo.service.IDominioIdentificadorService;

@Service
public class DominioIdentificadorDelegateImpl implements IDominioIdentificadorDelegate {

	@Autowired
	IDominioIdentificadorService dominioIdentificadorService;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DominioIdentificadorDTO> obtenerDominiosIdentificador(String estado) {
		return dominioIdentificadorService.obtenerDominiosIdentificador(estado);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DominioIdentificadorDTO obtenerDominioIdentificadorById(Integer id) {
		return dominioIdentificadorService.obtenerDominioIdentificadorById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DominioIdentificadorDTO persistirDominioIdentificador(DominioIdentificadorDTO dominioIdentificadorDto) {
		return dominioIdentificadorService.persistirDominioIdentificador(dominioIdentificadorDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DominioIdentificadorDTO actualizarDominioIdentificador(DominioIdentificadorDTO dominioIdentificadorDto) {
		return dominioIdentificadorService.actualizarDominioIdentificador(dominioIdentificadorDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean eliminarDominioIdentificador(Integer id) {
		return dominioIdentificadorService.eliminarDominioIdentificador(id);
	}

}
