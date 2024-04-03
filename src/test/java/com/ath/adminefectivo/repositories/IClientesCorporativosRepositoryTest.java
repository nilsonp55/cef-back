package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.ath.adminefectivo.entities.ClientesCorporativos;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class IClientesCorporativosRepositoryTest {
	
	@Autowired
	private IClientesCorporativosRepository clientesCorporativosRepository;
	private ClientesCorporativos clientesCorporativos;
	private List<ClientesCorporativos> listOfClientesCorporativos;
	private ClientesCorporativos searchClientesCorporativos;

	@BeforeEach
	void setUp() throws Exception {
		
		List<ClientesCorporativos> listSave = Instancio.ofList(ClientesCorporativos.class).size(11)
			.generate(field(ClientesCorporativos::getCodigoBancoAval), gen -> gen.oneOf(297,298,299,300))
			.create();
		
		listOfClientesCorporativos = clientesCorporativosRepository.saveAllAndFlush(listSave);
		searchClientesCorporativos = listOfClientesCorporativos.get(0);
		
		clientesCorporativos = Instancio.of(ClientesCorporativos.class)
				.generate(field(ClientesCorporativos::getCodigoBancoAval), gen -> gen.oneOf(297,298,299,300))
				.create();
		log.info("setup - id: {}", clientesCorporativos.getCodigoCliente());
	}
	
	/** Puede generar excepcion debido a la consulta sql que puede retornar mas de 1 registro **/
	@Test
	void testFindByCodigoBancoAvalAndIdentificacion() throws IncorrectResultSizeDataAccessException {
		
		ClientesCorporativos clientesCorporativosFind = clientesCorporativosRepository
				.findByCodigoBancoAvalAndIdentificacion(searchClientesCorporativos.getCodigoBancoAval(),
						searchClientesCorporativos.getIdentificacion());

		assertThat(clientesCorporativosFind).isNotNull();
		log.info("testFindByCodigoBancoAvalAndIdentificacion - id: {}", searchClientesCorporativos.getCodigoCliente());
	}

	@Test
	void testFindByCodigoCliente() {

		ClientesCorporativos clientesCorporativosFind = clientesCorporativosRepository
				.findByCodigoCliente(searchClientesCorporativos.getCodigoCliente());
		assertThat(clientesCorporativosFind).isNotNull();
		log.info("testFindByCodigoCliente - id: {}", clientesCorporativosFind.getCodigoCliente());
	}

	@Test
	void testIClientesCorporativosSave() {

		ClientesCorporativos clientesCorporativosSaved = clientesCorporativosRepository
				.saveAndFlush(clientesCorporativos);

		assertThat(clientesCorporativosSaved).isNotNull();
		assertThat(clientesCorporativosSaved.getCodigoCliente())
				.isEqualTo(clientesCorporativosSaved.getCodigoCliente());
		log.info("testIClientesCorporativosSave - id: {}", clientesCorporativosSaved.getCodigoCliente());
	}

	@Test
	void testIClientesCorporativosFindById() {

		Optional<ClientesCorporativos> clientesCorporativosFind = clientesCorporativosRepository
				.findById(searchClientesCorporativos.getCodigoCliente());

		assertThat(clientesCorporativosFind).isNotEmpty();
		clientesCorporativosFind.ifPresent(cliente -> {
			assertThat(cliente.getCodigoCliente()).isEqualTo(searchClientesCorporativos.getCodigoCliente());
		});
		log.info("testIClientesCorporativosFindById - id: {}", searchClientesCorporativos.getCodigoCliente());
	}

	@Test
	void testIClientesCorporativosFindAll() {

		List<ClientesCorporativos> clientesCorporativosFind = clientesCorporativosRepository.findAll();

		assertThat(clientesCorporativosFind).isNotEmpty().hasSize(listOfClientesCorporativos.size());
		log.info("testIClientesCorporativosFindAll - size: {}", clientesCorporativosFind.size());
	}

	@Test
	void testIClientesCorporativosDelete() {

		clientesCorporativosRepository.delete(searchClientesCorporativos);

		Optional<ClientesCorporativos> clientesCorporativosFind = clientesCorporativosRepository
				.findById(searchClientesCorporativos.getCodigoCliente());

		assertThat(clientesCorporativosFind).isEmpty();
		log.info("testIClientesCorporativosDelete - id: {}", searchClientesCorporativos.getCodigoCliente());
	}

}
