package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IDominioMaestroDelegate;
import com.ath.adminefectivo.dto.DominioMaestroDto;
import com.ath.adminefectivo.service.IDominioMaestroService;

@Service
public class DominioMaestroDelegateImpl implements IDominioMaestroDelegate {

	@Autowired
	IDominioMaestroService dominioMaestroService;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DominioMaestroDto> obtenerDominiosMaestro(String estado) {
		return dominioMaestroService.obtenerDominiosMaestro(estado);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DominioMaestroDto obtenerDominioMaestroById(String id) {
		return dominioMaestroService.obtenerDominioMaestroById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DominioMaestroDto persistirDominioMaestro(DominioMaestroDto dominioMaestroDto) {
		return dominioMaestroService.persistirDominioMaestro(dominioMaestroDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DominioMaestroDto actualizarDominioMaestro(DominioMaestroDto dominioMaestroDto) {
		return dominioMaestroService.actualizarDominioMaestro(dominioMaestroDto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean eliminarDominioMaestro(String id) {
		return dominioMaestroService.eliminarDominioMaestro(id);
	}

}
