package com.ath.adminefectivo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ath.adminefectivo.dto.MenuDTO;
import com.ath.adminefectivo.entities.Menu;
import com.ath.adminefectivo.repositories.MenuRepository;
import com.querydsl.core.BooleanBuilder;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class MenuServiceImplTest {

	@Mock
	private MenuRepository menuRepository;

	@InjectMocks
	private MenuServiceImpl menuService;

	private List<Menu> listOfMenus;

	private static final String S_USER_CREATION = "user1";

	@BeforeEach
	void setUp() throws Exception {

		listOfMenus = Instancio.ofList(Menu.class).size(11)
				.set(field(Menu::getUsuarioCreacion), S_USER_CREATION)
				.create();

	}

	@Test
	void testGetAllMenus() {
		// given
		BooleanBuilder builder = new BooleanBuilder();
		given(menuRepository.findAll(builder)).willReturn(listOfMenus);

		// when
		List<MenuDTO> listMenuDTO = menuService.getMenuByPredicate(builder);

		// then
		assertThat(listMenuDTO.size()).isEqualTo(listOfMenus.size());
		log.info("List size: {}", listMenuDTO.size());
	}
	
	@Test
	void testGetAllMenusReturnEntitieWithNull() {

		// given
		BooleanBuilder builder = new BooleanBuilder();
		List<Menu> listWithNullValue = Instancio.ofList(Menu.class).size(1)
				.set(field(Menu::getIdMenu), null)
				.create();
		given(menuRepository.findAll(builder)).willReturn(listWithNullValue);

		// when
		List<MenuDTO> itemsMenu = menuService.getMenuByPredicate(builder);
		
		// then 
		assertThat(itemsMenu.get(0).getIdMenu()).isNull();
	}

}
