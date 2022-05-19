package com.ath.adminefectivo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.RolDTO;
import com.ath.adminefectivo.repositories.RolRepository;
import com.ath.adminefectivo.service.IRolService;
import com.ath.adminefectivo.utils.UtilsObjects;

@Service
public class RolServiceImpl implements IRolService {

	@Autowired
	RolRepository rolRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RolDTO> getRoles() {
		var roles = rolRepository.findAll();

		return roles.stream().map(x -> {
			RolDTO dto = new RolDTO();
			UtilsObjects.copiarPropiedades(x, dto, false);
			return dto;
		}).toList();

	}

}
