package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.FallasRegistro;
import com.ath.adminefectivo.entities.RegistrosCargados;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
@Disabled("Integrar con tests de ArchivosCargados")
class FallasRegistroRepositoryTest {

	@Autowired
	private FallasRegistroRepository fallasRegistroRepository;
	@Autowired
	private IRegistrosCargadosRepository registrosCargadosRepository;
	private FallasRegistro fallasRegistro;
	private List<FallasRegistro> listOfFallasRegistro;
	
	@BeforeEach
	void setUp() throws Exception {
		RegistrosCargados registrosCargados = Instancio.of(RegistrosCargados.class)
				.create();
		registrosCargadosRepository.save(registrosCargados);
		
		List<FallasRegistro> listSave = Instancio.ofList(FallasRegistro.class)
				.size(11)
				.set(field(FallasRegistro::getId), registrosCargados)
				.set(field(FallasRegistro::getRegistrosCargados), null)
				.create();
		listOfFallasRegistro = fallasRegistroRepository.saveAll(listSave);

		fallasRegistro = Instancio.of(FallasRegistro.class)
				.set(field(FallasRegistro::getRegistrosCargados), null)
				.create();
		fallasRegistroRepository.findAll();
		log.info("setUp - id: {}", fallasRegistro.getId());
	}

	@Test
	void testFallasRegistroSave() {
		FallasRegistro fallasRegistroSaved = fallasRegistroRepository.save(fallasRegistro);
		
		assertThat(fallasRegistroSaved).isNotNull();
		log.info("testFallasRegistroSave - id: {}", fallasRegistroSaved.getId());
	}

	@Test
	void testFallasRegistroFindById() {
		FallasRegistro fallasRegistroSearch = listOfFallasRegistro.get(0);

		Optional<FallasRegistro> fallasRegistroFind = fallasRegistroRepository.findById(fallasRegistroSearch.getId());

		assertThat(fallasRegistroFind).isNotEmpty();
		fallasRegistroFind.ifPresent(fallas -> {
			assertThat(fallas.getId()).isEqualTo(fallasRegistroSearch.getId());
		});
		log.info("testFallasRegistroFindById - id: {}", fallasRegistroSearch.getId());
	}

	@Test
	void testFallasRegistroFindAll() {
		List<FallasRegistro> fallasRegistroFind = fallasRegistroRepository.findAll();

		assertThat(fallasRegistroFind).isNotEmpty().hasSize(listOfFallasRegistro.size());
		log.info("testFallasRegistroFindAll - size: {}", fallasRegistroFind.size());
	}

	@Test
	void testFallasRegistroDelete() {
		FallasRegistro fallasRegistroSearch = listOfFallasRegistro.get(0);
		fallasRegistroRepository.delete(fallasRegistroSearch);
		Optional<FallasRegistro> fallasRegistroFind = fallasRegistroRepository.findById(fallasRegistroSearch.getId());

		assertThat(fallasRegistroFind).isEmpty();
		log.info("testFallasRegistroDelete - id: {}", fallasRegistroSearch.getId());
	}

}
