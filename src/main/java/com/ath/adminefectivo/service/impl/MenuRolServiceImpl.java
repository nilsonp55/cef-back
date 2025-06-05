package com.ath.adminefectivo.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.MenuRol;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.exception.NotFoundException;
import com.ath.adminefectivo.repositories.MenuRolRepository;
import com.ath.adminefectivo.service.IMenuRolService;
import com.querydsl.core.types.Predicate;

/**
 * Interfaz de los servicios a la tabla de menuRol
 * @author bayronPerez
 */

@Service
public class MenuRolServiceImpl implements IMenuRolService{

	@Autowired
	MenuRolRepository menuRolRepository;
	
	
	@Override
	public List<MenuRol> getMenuRol(Predicate predicate) {
		var menuRols = menuRolRepository.findAll(predicate);
		return (List<MenuRol>) menuRols;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MenuRol getMenuRolById(Integer idMenuRol) {
		return menuRolRepository.findById(idMenuRol)
				.orElseThrow(() -> new NotFoundException(ApiResponseCode.RECURSO_NO_ENCONTRADO.getCode(),
						"MenuRol no encontrado con el código: " + idMenuRol,
						ApiResponseCode.RECURSO_NO_ENCONTRADO.getHttpStatus()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MenuRol postMenuRol(MenuRol menuRol) {
		if (menuRol.getCodigo() != null && menuRolRepository
				.existsById(menuRol.getCodigo())) {		
			throw new ConflictException(ApiResponseCode.RECURSO_YA_EXISTE.getCode(),
					"Ya existe un MenuRol con el código: " + menuRol.getCodigo(),
					ApiResponseCode.RECURSO_YA_EXISTE.getHttpStatus());
		}
		return menuRolRepository.save(menuRol);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MenuRol putMenuRol(MenuRol menuRol) {
		if (menuRol.getCodigo() == null || !menuRolRepository
				.existsById(menuRol.getCodigo())) {		
			throw new NotFoundException(ApiResponseCode.RECURSO_NO_ENCONTRADO.getCode(),
					"MenuRol no encontrado con el código: " + menuRol.getCodigo(),
					ApiResponseCode.RECURSO_NO_ENCONTRADO.getHttpStatus());
		}
		// Ensure the entity is fetched and then updated (save will act as merge if ID exists)
		return menuRolRepository.save(menuRol);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteMenuRol(Integer idMenuRol) {
		if (idMenuRol == null || !menuRolRepository
				.existsById(idMenuRol)) {		
			throw new NotFoundException(ApiResponseCode.RECURSO_NO_ENCONTRADO.getCode(),
					"MenuRol no encontrado con el código: " + idMenuRol,
					ApiResponseCode.RECURSO_NO_ENCONTRADO.getHttpStatus());
		}
		menuRolRepository.deleteById(idMenuRol);
	}

}
