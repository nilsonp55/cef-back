package com.ath.adminefectivo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.sql.SQLException;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import com.ath.adminefectivo.dto.MenuDTO;
import com.ath.adminefectivo.service.IMenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import lombok.extern.log4j.Log4j2;

@Log4j2
@WebMvcTest(MenuController.class)
@TestPropertySource("classpath:endpoint.properties")
public class MenuControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IMenuService menuService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Value("${endpoints.Menu}")
	private String endpointMenu;
	
	@Value("${endpoints.Menu.crud}")
	private String crudMapping;
	
	private List<MenuDTO> listOfMenuDTO;

    @BeforeEach
    void setUp() throws Exception {

      this.listOfMenuDTO = Instancio.ofList(MenuDTO.class).size(10).create();

      log.info("setUp - size: {}", this.listOfMenuDTO.size());
    }

    @Test
    void testGetAllMenus() throws Exception {

      when(this.menuService.getMenuByPredicate(any(Predicate.class))).thenReturn(this.listOfMenuDTO);

      ResultActions resultAction = mockMvc.perform(
          get(this.endpointMenu.concat(this.crudMapping)).contentType(MediaType.APPLICATION_JSON));
      MvcResult result =
          resultAction.andExpect(status().isOk()).andExpect(jsonPath("response").exists())
              .andExpect(jsonPath("data").exists()).andReturn();

      log.info("testGetAllMenus - status: {}", result.getResponse().getStatus());
    }

    @Test
    void testCreateMenu() throws Exception {
      when(this.menuService.createMenu(any(MenuDTO.class))).thenReturn(this.listOfMenuDTO.get(0));

      ResultActions resultAction = mockMvc.perform(
          post(this.endpointMenu.concat(this.crudMapping)).contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(this.listOfMenuDTO.get(0))));
      MvcResult result =
          resultAction.andExpect(status().isCreated()).andExpect(jsonPath("response").exists())
              .andExpect(jsonPath("data").exists()).andExpect(jsonPath("data.idMenu").exists())
              .andExpect(jsonPath("data.idMenuPadre").exists())
              .andExpect(jsonPath("data.nombre").exists()).andExpect(jsonPath("data.tipo").exists())
              .andExpect(jsonPath("data.icono").exists()).andExpect(jsonPath("data.url").exists())
              .andExpect(jsonPath("data.estado").exists())
              .andExpect(jsonPath("data.fechaCreacion").exists())
              .andExpect(jsonPath("data.usuarioCreacion").exists())
              .andExpect(jsonPath("data.fechaModificacion").exists())
              .andExpect(jsonPath("data.usuarioModificacion").exists())
              .andExpect(jsonPath("response.code").exists())
              .andExpect(jsonPath("response.description").exists()).andReturn();

      log.info("testCreateMenu - status: {}", result.getResponse().getStatus());

    }

    @Test
    void testUpdateMenu() throws Exception {
      when(this.menuService.updateMenu(any(MenuDTO.class))).thenReturn(this.listOfMenuDTO.get(0));

      ResultActions resultAction = mockMvc.perform(
          put(this.endpointMenu.concat(this.crudMapping)).contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(this.listOfMenuDTO.get(0))));
      MvcResult result =
          resultAction.andExpect(status().isOk()).andExpect(jsonPath("response").exists())
              .andExpect(jsonPath("data").exists()).andExpect(jsonPath("data.idMenu").exists())
              .andExpect(jsonPath("data.idMenuPadre").exists())
              .andExpect(jsonPath("data.nombre").exists()).andExpect(jsonPath("data.tipo").exists())
              .andExpect(jsonPath("data.icono").exists()).andExpect(jsonPath("data.url").exists())
              .andExpect(jsonPath("data.estado").exists())
              .andExpect(jsonPath("data.fechaCreacion").exists())
              .andExpect(jsonPath("data.usuarioCreacion").exists())
              .andExpect(jsonPath("data.fechaModificacion").exists())
              .andExpect(jsonPath("data.usuarioModificacion").exists())
              .andExpect(jsonPath("response.code").exists())
              .andExpect(jsonPath("response.description").exists()).andReturn();

      log.info("testUpdateMenu - status: {}", result.getResponse().getStatus());
    }
    
    @Test
    void testDeleteMenu() throws Exception {
      doNothing().when(this.menuService).deleteMenu("0");

      ResultActions resultAction =
          mockMvc.perform(delete(this.endpointMenu.concat(this.crudMapping.concat("1"))));
      MvcResult result = resultAction.andExpect(status().isNoContent()).andReturn();

      log.info("testDeleteMenu - status: {}", result.getResponse().getStatus());
    }
    
    @Test
    void testDeleteMenuException() throws Exception {
      doThrow(new DataIntegrityViolationException("test deleted Menu DataIntegrityException",
          new SQLException("SQL Exception Menu test DataIntegrityException")))
              .when(this.menuService).deleteMenu(any());

      ResultActions resultAction =
          mockMvc.perform(delete(this.endpointMenu.concat(this.crudMapping.concat("1"))));
      MvcResult result = resultAction.andExpect(status().is4xxClientError()).andReturn();

      log.info("testDeleteMenuException - status: {}", result.getResponse().getStatus());
    }
}
