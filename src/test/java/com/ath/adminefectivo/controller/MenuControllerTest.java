package com.ath.adminefectivo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ath.adminefectivo.dto.MenuDTO;
import com.ath.adminefectivo.service.IMenuService;
import com.querydsl.core.types.Predicate;

@WebMvcTest(MenuController.class)
@Disabled
public class MenuControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IMenuService menuService;

	List<MenuDTO> listOfMenuDTO;

	@BeforeEach
	void setUp() throws Exception {

		listOfMenuDTO = Instancio.ofList(MenuDTO.class).size(10).create();

	}

	@Test
	void testGetAllMenus() throws Exception {
		
		when(menuService.getMenuByPredicate(any(Predicate.class))).thenReturn(listOfMenuDTO);

		mockMvc.perform(get("/v1.0.1/ade/menu/"));
	}
}
