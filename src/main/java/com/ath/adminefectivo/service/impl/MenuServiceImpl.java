package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.MenuDTO;
import com.ath.adminefectivo.entities.Menu;
import com.ath.adminefectivo.repositories.MenuRepository;
import com.ath.adminefectivo.service.IMenuService;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class MenuServiceImpl implements IMenuService {

	private final MenuRepository menuRepository;

	public MenuServiceImpl(@Autowired MenuRepository menuRepository) {
		this.menuRepository = menuRepository;
	}

	@Override
	public List<MenuDTO> getMenuByPredicate(Predicate predicate) {

		Iterable<Menu> itemsMenu = menuRepository.findAll(predicate);
		List<MenuDTO> listMenuDTO = new ArrayList<>();
		itemsMenu.iterator().forEachRemaining(t -> listMenuDTO.add(MenuDTO.CONVERTER_DTO.apply(t)));
		log.debug("List Menu size: ", listMenuDTO.size());
		return listMenuDTO;
	}

}
