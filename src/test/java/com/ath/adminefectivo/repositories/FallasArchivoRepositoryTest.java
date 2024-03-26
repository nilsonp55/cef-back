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

import com.ath.adminefectivo.entities.FallasArchivo;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class FallasArchivoRepositoryTest {

	@Autowired
	private FallasArchivoRepository fallasArchivoRepository;
	private FallasArchivo fallasArchivo;
	private List<FallasArchivo> listOfFallasArchivo;
	
	@BeforeEach
	void setUp() throws Exception {
		List<FallasArchivo> listSave = Instancio.ofList(FallasArchivo.class)
				.size(11)
				.set(field(FallasArchivo::getIdFallasArchivo), null)
				.set(field(FallasArchivo::getArchivosCargados), null)
				.create();
		listOfFallasArchivo = fallasArchivoRepository.saveAll(listSave);
		
		fallasArchivo = Instancio.of(FallasArchivo.class)
				.set(field(FallasArchivo::getIdFallasArchivo), null)
				.set(field(FallasArchivo::getArchivosCargados), null)
				.create();
		
		log.info("setUp: fechaCreacion: {}", fallasArchivo.getFechaCreacion());
		
	}

	@Test
	void testFallasArchivoSave() {
		FallasArchivo fallasArchivoSaved = fallasArchivoRepository.save(fallasArchivo);

		assertThat(fallasArchivoSaved).isNotNull();
		assertThat(fallasArchivoSaved.getIdFallasArchivo()).isNotNull();
		assertThat(fallasArchivoSaved.getDescripcionError()).isEqualTo(fallasArchivo.getDescripcionError());
		log.info("testFallasArchivoSave: id: {}", fallasArchivoSaved.getIdFallasArchivo());
	}

	@Test
	void testFallasArchivoFindById() {
		FallasArchivo fallasArchivoSearch = listOfFallasArchivo.get(0);
		Optional<FallasArchivo> fallasArchivoFind = fallasArchivoRepository
				.findById(fallasArchivoSearch.getIdFallasArchivo());

		assertThat(fallasArchivoFind).isNotEmpty();
		fallasArchivoFind.ifPresent(fallas -> {
			assertThat(fallas.getIdFallasArchivo()).isNotNull();
			assertThat(fallas.getFechaCreacion()).isEqualTo(fallasArchivoSearch.getFechaCreacion());
		});
		log.info("testFallasArchivoFindById: id: {}", fallasArchivoSearch.getIdFallasArchivo());
	}

	@Test
	void testFallasArchivoFindAll() {
		List<FallasArchivo> fallasArchivoFind = fallasArchivoRepository.findAll();
		
		assertThat(fallasArchivoFind).isNotEmpty().hasSize(listOfFallasArchivo.size());
		log.info("testFallasArchivoFindAll: size: {}", fallasArchivoFind.size());
	}

	@Test
	void tesFallasArchivoDelete() {
		FallasArchivo fallasArchivoSearch = listOfFallasArchivo.get(0);

		fallasArchivoRepository.delete(fallasArchivoSearch);
		Optional<FallasArchivo> fallasArchivoFind = fallasArchivoRepository
				.findById(fallasArchivoSearch.getIdFallasArchivo());

		assertThat(fallasArchivoFind).isEmpty();
		log.info("tesFallasArchivoDelete: id: {}", fallasArchivoSearch.getIdFallasArchivo());
	}

}
