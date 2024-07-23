package com.ath.adminefectivo.controller;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.ath.adminefectivo.dto.ClientesCorporativosDTO;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;

@WebMvcTest(ClientesCorporativosController.class)
@TestPropertySource("classpath:endpoint.properties")
class ClientesCorporativosControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IClientesCorporativosService clientesCorporativosService;
	
	@Autowired
    private ObjectMapper objectMapper;

	private ClientesCorporativosDTO clientesCorporativosDTO;
	
	private List<ClientesCorporativosDTO> listClientes;

	@BeforeEach
	void setUp() throws Exception {
		clientesCorporativosDTO = Instancio.of(ClientesCorporativosDTO.class)
				.generate(field(ClientesCorporativosDTO::getCodigoBancoAval), gen -> gen.oneOf(297, 298, 299, 300))
				.create();

		listClientes = Instancio.ofList(ClientesCorporativosDTO.class)
				.generate(field(ClientesCorporativosDTO::getCodigoBancoAval), gen -> gen.oneOf(297, 298, 299, 300))
				.create();
	}

	@Test
	void testGetListClientesCorporativos() throws Exception {

		when(clientesCorporativosService.getClientesCorporativos(any(Predicate.class))).thenReturn(listClientes);

		mockMvc.perform(get("/v1.0.1/ade/clientes-corporativos/consultar")
				.param("page", "0")
				.param("size", "10"))
				.andExpect(status().isOk());
	}
	
	@Test
	void testGetListClientesCorporativosCrud() throws Exception {
		Page<ClientesCorporativosDTO> pageClientesCorporativos = new PageImpl<>(listClientes);
		when(clientesCorporativosService.listarClientesCorporativos(any(), any(Pageable.class), anyString()))
				.thenReturn(pageClientesCorporativos);

		mockMvc.perform(
				get("/v1.0.1/ade/clientes-corporativos/").param("page", "0").param("size", "20").param("busqueda", ""))
				.andExpect(status().isOk()).andExpect(jsonPath("data").exists())
				.andExpect(jsonPath("response").exists());
	}
	
	@Test
	void testGetOneClientesCorporativosCrud() throws Exception {

		when(clientesCorporativosService.getClientesCorporativos(any(Integer.class)))
				.thenReturn(clientesCorporativosDTO);

		mockMvc.perform(get("/v1.0.1/ade/clientes-corporativos/" + clientesCorporativosDTO.getCodigoCliente())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("response").exists());

	}
	
	@Test
	void testCreateClientesCorporativosCrud() throws Exception {

		when(clientesCorporativosService.guardarClientesCorporativos(any(ClientesCorporativosDTO.class)))
				.thenReturn(clientesCorporativosDTO);
		
		mockMvc.perform(post("/v1.0.1/ade/clientes-corporativos/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(clientesCorporativosDTO)))
		.andExpect(status().isOk());
	}
	
}
