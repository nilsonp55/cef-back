package com.ath.adminefectivo.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.DetallesProcesoAutomaticoDTO;
import com.ath.adminefectivo.service.IDetallesProcesoAutomaticoService;
import com.querydsl.core.types.Predicate;

@Service
public class DetallesProcesoAutomaticoServiceImpl implements IDetallesProcesoAutomaticoService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DetallesProcesoAutomaticoDTO> getDetallesProcesoAutomatico(Predicate predicate) {
		return new ArrayList<>();
	}

	
}
