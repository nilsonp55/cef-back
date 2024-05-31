package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.SitiosClientesDTO;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.repositories.ISitiosClientesRepository;
import com.ath.adminefectivo.service.ISitiosClientesService;
import com.querydsl.core.types.Predicate;

@Service
public class SitiosClientesServiceImpl implements ISitiosClientesService{

	@Autowired
	ISitiosClientesRepository sitiosClientesRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<SitiosClientesDTO> getSitiosClientes(Predicate predicate) {
		var sitiosClientes = sitiosClientesRepository.findAll(predicate);
		List<SitiosClientesDTO> listSitiosClientesDto = new ArrayList<>();
		sitiosClientes.forEach(entity -> listSitiosClientesDto.add(SitiosClientesDTO.CONVERTER_DTO.apply(entity)));
		return listSitiosClientesDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SitiosClientes getCodigoPuntoSitio(Integer codigoPunto) {
		
		return sitiosClientesRepository.findByCodigoPunto(codigoPunto);

	}

}
