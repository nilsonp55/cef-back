package com.ath.adminefectivo.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.MenuRol;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
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
		var cuentas = menuRolRepository.findById(idMenuRol);
		if (Objects.isNull(cuentas)) {
			throw new AplicationException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getCode(),
					ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getDescription(),
					ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getHttpStatus());
		} 
		return cuentas.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MenuRol postMenuRol(MenuRol menuRol) {
		if (menuRol.getCodigo() != null && menuRolRepository
				.existsById(menuRol.getCodigo())) {		
			throw new ConflictException(ApiResponseCode.ERROR_CUENTAS_PUC_EXIST.getDescription());		
		}
		MenuRol menuRolResponse = menuRolRepository.save(menuRol);
		return menuRolResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MenuRol putMenuRol(MenuRol menuRol) {
		if (menuRol.getCodigo() == null && !menuRolRepository
				.existsById(menuRol.getCodigo())) {		
			throw new ConflictException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getDescription());		
		}
		MenuRol menuRolResponse = menuRolRepository.save(menuRol);
		return menuRolResponse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteMenuRol(Integer idMenuRol) {
		if (idMenuRol == null && !menuRolRepository
				.existsById(idMenuRol)) {		
			throw new ConflictException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getDescription());		
		}
		menuRolRepository.deleteById(idMenuRol);
	}

}
