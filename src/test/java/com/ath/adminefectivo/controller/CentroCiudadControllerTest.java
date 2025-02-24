package com.ath.adminefectivo.controller;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.sql.SQLException;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.CentroCiudadDTO;
import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.service.ICentroCiudadPpalService;
import com.ath.adminefectivo.service.ICentroCiudadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import lombok.extern.log4j.Log4j2;

@Log4j2
@WebMvcTest(CentroCiudadController.class)
@TestPropertySource("classpath:endpoint.properties")
public class CentroCiudadControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ICentroCiudadPpalService centroCiudadPpalService;

  @MockBean
  private ICentroCiudadService centroCiudadService;

  private CentroCiudadDTO centroCiudadDTO;

  private List<CentroCiudadDTO> listCentroCiudad;

  @BeforeEach
  void setUp() throws Exception {

    BancosDTO banco = Instancio.of(BancosDTO.class).create();
    CiudadesDTO ciudad = Instancio.of(CiudadesDTO.class).create();
    this.listCentroCiudad = Instancio.ofList(CentroCiudadDTO.class).size(11)
        .set(field(CentroCiudadDTO::getBancoAval), banco)
        .set(field(CentroCiudadDTO::getCiudadDane), ciudad).create();
    this.centroCiudadDTO = this.listCentroCiudad.get(0);
  }

  @Test
  void testListGetCentroCiudadPpal() throws Exception {

    when(this.centroCiudadPpalService.listCentroCiudad(any(Predicate.class)))
        .thenReturn(this.listCentroCiudad);
    MvcResult result = mockMvc
        .perform(get("/v1.0.1/ade/centro-ciudad/ppal").param("page", "0").param("size", "10"))
        .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data").exists()).andExpect(jsonPath("response").exists())
        .andExpect(jsonPath("response.code").exists())
        .andExpect(jsonPath("response.description").exists())
        .andReturn();

    log.info("testListGetCentroCiudadPpal status: {}", result.getResponse().getStatus());
  }

  @Test
  void testPostCreateCentroCiudadPpal() throws Exception {
    when(centroCiudadPpalService.create(any(CentroCiudadDTO.class)))
        .thenReturn(this.centroCiudadDTO);
    ResultActions resultAction = mockMvc
        .perform(post("/v1.0.1/ade/centro-ciudad/ppal").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString((this.centroCiudadDTO))));

    MvcResult result = resultAction.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data").exists()).andExpect(jsonPath("response").exists())
        .andExpect(jsonPath("data.idCentroCiudad").exists())
        .andExpect(jsonPath("data.ciudadDane.codigoDANE").exists())
        .andExpect(jsonPath("data.ciudadDane.nombreCiudad").exists())
        .andExpect(jsonPath("data.ciudadDane.nombreCiudadFiserv").exists())
        .andExpect(jsonPath("data.ciudadDane.codigoBrinks").exists())
        .andExpect(jsonPath("data.ciudadDane.cobroIva").exists())
        .andExpect(jsonPath("data.bancoAval.codigoCompensacion").exists())
        .andExpect(jsonPath("data.bancoAval.numeroNit").exists())
        .andExpect(jsonPath("data.bancoAval.abreviatura").exists())
        .andExpect(jsonPath("data.bancoAval.esAVAL").exists())
        .andExpect(jsonPath("data.bancoAval.codigoPunto").exists())
        .andExpect(jsonPath("data.bancoAval.nombreBanco").exists())
        .andExpect(jsonPath("data.codigoCentro").exists())
        .andExpect(jsonPath("response.code").exists())
        .andExpect(jsonPath("response.description").exists()).andReturn();

    log.info("testPostCreateCentroCiudadPpal status: {}", result.getResponse().getStatus());
  }
  
  @Test
  void testPostCreateCentroCiudadPpalDataIntegrityException() throws Exception {
    when(centroCiudadPpalService.create(any(CentroCiudadDTO.class))).thenThrow(
        new DataIntegrityViolationException("test created centroCiudadPpal DataIntegrityException")
            .initCause(new SQLException("SQL Exception test DataIntegrityException")));

    MvcResult result = mockMvc
        .perform(post("/v1.0.1/ade/centro-ciudad/ppal").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString((this.centroCiudadDTO))))
        .andExpect(status().is4xxClientError()).andReturn();

    log.info("testPostCreateCentroCiudadPpalDataIntegrityException status: {}",
        result.getResponse().getStatus());
  }

  @Test
  void testPostCreateCentroCiudadPpalDataAccessException() throws Exception {
    when(centroCiudadPpalService.create(any(CentroCiudadDTO.class))).thenThrow(
        new PermissionDeniedDataAccessException("test created centroCiudadPpal DataAccessException",
            new SQLException("SQL Exception test DataAccessException")));

    MvcResult result = mockMvc
        .perform(post("/v1.0.1/ade/centro-ciudad/ppal").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString((this.centroCiudadDTO))))
        .andExpect(status().is4xxClientError()).andReturn();

    log.info("testPostCreateCentroCiudadPpalDataAccessException status: {}",
        result.getResponse().getStatus());
  }
  
  @Test
  void testPutUpdateCentroCiudadPpal() throws Exception {
    when(centroCiudadPpalService.update(any(CentroCiudadDTO.class)))
        .thenReturn(this.centroCiudadDTO);
    ResultActions resultAction = mockMvc
        .perform(put("/v1.0.1/ade/centro-ciudad/ppal").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString((this.centroCiudadDTO))));

    MvcResult result = resultAction.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("data").exists()).andExpect(jsonPath("response").exists())
        .andExpect(jsonPath("data.idCentroCiudad").exists())
        .andExpect(jsonPath("data.ciudadDane.codigoDANE").exists())
        .andExpect(jsonPath("data.ciudadDane.nombreCiudad").exists())
        .andExpect(jsonPath("data.ciudadDane.nombreCiudadFiserv").exists())
        .andExpect(jsonPath("data.ciudadDane.codigoBrinks").exists())
        .andExpect(jsonPath("data.ciudadDane.cobroIva").exists())
        .andExpect(jsonPath("data.bancoAval.codigoCompensacion").exists())
        .andExpect(jsonPath("data.bancoAval.numeroNit").exists())
        .andExpect(jsonPath("data.bancoAval.abreviatura").exists())
        .andExpect(jsonPath("data.bancoAval.esAVAL").exists())
        .andExpect(jsonPath("data.bancoAval.codigoPunto").exists())
        .andExpect(jsonPath("data.bancoAval.nombreBanco").exists())
        .andExpect(jsonPath("data.codigoCentro").exists())
        .andExpect(jsonPath("response.code").exists())
        .andExpect(jsonPath("response.description").exists()).andReturn();

    log.info("testPutUpdateCentroCiudadPpal status: {}", result.getResponse().getStatus());
  }
  
  @Test
  void testPutUpdateCentroCiudadPpalDataIntegrityException() throws Exception {
    when(centroCiudadPpalService.update(any(CentroCiudadDTO.class))).thenThrow(
        new DataIntegrityViolationException("test updated centroCiudadPpal DataIntegrityException")
            .initCause(new SQLException("SQL Exception test DataIntegrityException")));

    MvcResult result = mockMvc
        .perform(put("/v1.0.1/ade/centro-ciudad/ppal").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString((this.centroCiudadDTO))))
        .andExpect(status().is4xxClientError()).andReturn();

    log.info("testPutUpdateCentroCiudadPpalDataIntegrityException status: {}",
        result.getResponse().getStatus());
  }

  @Test
  void testPutUpdateCentroCiudadPpalDataAccessException() throws Exception {
    when(centroCiudadPpalService.update(any(CentroCiudadDTO.class))).thenThrow(
        new PermissionDeniedDataAccessException("test updated centroCiudadPpal DataAccessException",
            new SQLException("SQL Exception test DataAccessException")));

    MvcResult result = mockMvc
        .perform(put("/v1.0.1/ade/centro-ciudad/ppal").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString((this.centroCiudadDTO))))
        .andExpect(status().is4xxClientError()).andReturn();

    log.info("testPutUpdateCentroCiudadPpalDataAccessException status: {}",
        result.getResponse().getStatus());
  }
  
  @Test
  void testDeleteCentroCiudadPpal() throws Exception {
    doNothing().when(centroCiudadPpalService).delete(anyInt());

    MvcResult result = mockMvc
        .perform(delete("/v1.0.1/ade/centro-ciudad/ppal").param("idCentroCiudad", "1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful()).andReturn();

    log.info("testPutUpdateCentroCiudadPpalDataAccessException status: {}",
        result.getResponse().getStatus());
  }
  
  @Test
  void testDeleteCentroCiudadPpalDataException() throws Exception {
    doThrow(
        new DataIntegrityViolationException("test deleted centroCiudadPpal DataIntegrityException",
            new SQLException("SQL Exception test DataIntegrityException")))
                .when(centroCiudadPpalService).delete(anyInt());

    MvcResult result = mockMvc
        .perform(delete("/v1.0.1/ade/centro-ciudad/ppal").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError()).andReturn();

    log.info("testDeleteCentroCiudadPpalDataException status: {}",
        result.getResponse().getStatus());
  }
}
