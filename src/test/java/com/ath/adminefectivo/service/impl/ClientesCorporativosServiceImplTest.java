package com.ath.adminefectivo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ath.adminefectivo.dto.ClientesCorporativosDTO;
import com.ath.adminefectivo.entities.ClientesCorporativos;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.repositories.IClientesCorporativosRepository;
import com.ath.adminefectivo.service.ISitiosClientesService;
import com.querydsl.core.BooleanBuilder;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(MockitoExtension.class)
class ClientesCorporativosServiceImplTest {
	
	@Mock
	private IClientesCorporativosRepository clientesCorporativosRepository;
	@Mock
	private ISitiosClientesService sitiosClientesService;
	@InjectMocks
	private ClientesCorporativosServiceImpl clientesCorporativosService;
	
	private List<ClientesCorporativos> listClientesCorporativos;
	private ClientesCorporativos clientesCorporativos;
	private SitiosClientes sitiosClientes;

	@BeforeEach
	void setUp() throws Exception {
		
		listClientesCorporativos = Instancio.ofList(ClientesCorporativos.class).size(10)
				.generate(field(ClientesCorporativos::getCodigoBancoAval), gen -> gen.oneOf(297, 298, 299, 300))
				.create();
		clientesCorporativos = listClientesCorporativos.get(0);
		sitiosClientes = Instancio.of(SitiosClientes.class)
				.set(field(SitiosClientes::getCodigoCliente), clientesCorporativos.getCodigoCliente())
				.create();
		
		log.info("setup - cliente: {}", clientesCorporativos.getCodigoCliente());
	}

	@Test
	void testGetClientesCorporativos() {
		// given
		BooleanBuilder builder = new BooleanBuilder();
		given(clientesCorporativosRepository.findAll(builder)).willReturn(listClientesCorporativos);

		// when
		List<ClientesCorporativosDTO> listClientes = clientesCorporativosService.getClientesCorporativos(builder);

		// then
		assertThat(listClientes).isNotEmpty().hasSize(listClientesCorporativos.size());
		log.info("testGetClientesCorporativos - predicate: {}", builder.toString());
	}

	/** 
	 * Escenario: Codigo banco y nit cliente existe un solo registro -- Ideal ---
	 * Escenario: Codigo banco y nit cliente existe mas de un registro -- Excepcion no controlada
	 * Escenario: Codigo banco NO existe y nit cliente existe 
	 * Escenario: Codigo banco NO existe y nit cliente NO existe 
	 * Escenario: Codigo banco existe y nit cliente NO existe 
	 */
	
	@Test
	void testGetCodigoClienteWhenReturnOnlyOneResult() {
		// given
		Integer codigoBanco = clientesCorporativos.getCodigoBancoAval();
		String nitCliente = clientesCorporativos.getIdentificacion();
		given(clientesCorporativosRepository.findByCodigoBancoAvalAndIdentificacion(codigoBanco, nitCliente))
				.willReturn(clientesCorporativos);

		// when
		Integer codigoClienteFind = clientesCorporativosService.getCodigoCliente(codigoBanco, nitCliente);

		// then
		assertThat(codigoClienteFind).isNotNull().isEqualTo(clientesCorporativos.getCodigoCliente());
	}
	

	@Test
	void testGetCodigoPuntoClienteWhenClientesCorporativosAndSitiosClientesExists() {
		// given
		Integer codigoPunto = sitiosClientes.getCodigoPunto();
		given(sitiosClientesService.getCodigoPuntoSitio(codigoPunto)).willReturn(sitiosClientes);
		given(clientesCorporativosRepository.findByCodigoCliente(clientesCorporativos.getCodigoCliente()))
				.willReturn(clientesCorporativos);

		// when - then
		assertTrue(clientesCorporativosService.getCodigoPuntoCliente(codigoPunto).booleanValue());
	}
	
	@Test
	void testGetCodigoPuntoClienteWhenClientesCorporativosNotExists() {
		// given
		given(sitiosClientesService.getCodigoPuntoSitio(0)).willReturn(null);
		
		// when - then
		assertFalse(clientesCorporativosService.getCodigoPuntoCliente(0).booleanValue());
	}

	@Test
	void testGetCodigoPuntoClienteWhenClientesCorporativosExistsAndSitiosClientesNotExists() {
		// given
		Integer codigoPunto = sitiosClientes.getCodigoPunto();
		given(sitiosClientesService.getCodigoPuntoSitio(codigoPunto)).willReturn(sitiosClientes);
		given(clientesCorporativosRepository.findByCodigoCliente(clientesCorporativos.getCodigoCliente()))
				.willReturn(null);

		// when - then
		assertFalse(clientesCorporativosService.getCodigoPuntoCliente(codigoPunto).booleanValue());
	}
}
