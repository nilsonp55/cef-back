package com.ath.adminefectivo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ath.adminefectivo.dto.ClientesCorporativosDTO;
import com.ath.adminefectivo.entities.ClientesCorporativos;
import com.ath.adminefectivo.repositories.IClientesCorporativosRepository;
import com.ath.adminefectivo.service.ISitiosClientesService;
import com.querydsl.core.BooleanBuilder;

@ExtendWith(MockitoExtension.class)
class ClientesCorporativosServiceImplTest {
	
	@Mock
	private IClientesCorporativosRepository clientesCorporativosRepository;
	@Mock
	private ISitiosClientesService sitiosClientesService;
	@InjectMocks
	private ClientesCorporativosServiceImpl clientesCorporativosService;
	
	private List<ClientesCorporativos> listClientesCorporativos;

	@BeforeEach
	void setUp() throws Exception {
		
		listClientesCorporativos = Instancio.ofList(ClientesCorporativos.class).size(10)
				.generate(field(ClientesCorporativos::getCodigoBancoAval), gen -> gen.oneOf(297, 298, 299, 300))
				.create();
	}

	@Test
	void testGetClientesCorporativos() {
		// given
		BooleanBuilder builder = new BooleanBuilder();
		BDDMockito.given(clientesCorporativosRepository.findAll(builder)).willReturn(listClientesCorporativos);

		// when
		List<ClientesCorporativosDTO> listClientes = clientesCorporativosService.getClientesCorporativos(builder);

		// then
		assertThat(listClientes).isNotEmpty();
	}

	@Test
	@Disabled
	void testGetCodigoCliente() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled
	void testGetCodigoPuntoCliente() {
		fail("Not yet implemented");
	}

}
