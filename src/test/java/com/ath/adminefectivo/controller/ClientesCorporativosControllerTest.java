package com.ath.adminefectivo.controller;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.ath.adminefectivo.dto.ClientesCorporativosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.service.IClientesCorporativosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ClientesCorporativosControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private IClientesCorporativosService clientesCorporativosService;

	@Autowired
	private ClientesCorporativosController clientesCorporativosController;

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
	void testGetClientesCorporativos() {
		when(clientesCorporativosService.getClientesCorporativos(any(Predicate.class))).thenReturn(listClientes);

		ResponseEntity<ApiResponseADE<List<ClientesCorporativosDTO>>> returnClientes = clientesCorporativosController
				.getClientesCorporativos(any(Predicate.class));
	}

}
