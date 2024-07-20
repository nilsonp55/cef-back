package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.joda.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.FestivosNacionales;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class FestivosNacionalesRepositoryTest {

	@Autowired
	private FestivosNacionalesRepository festivosNacionalesRepository;
	private FestivosNacionales festivosNacionales;
	private List<FestivosNacionales> listOfFestivosNacionales;
	private FestivosNacionales festivosNacionalesSearch;

	@BeforeEach
	void setUp() throws Exception {
		List<FestivosNacionales> listSave = Instancio.ofList(FestivosNacionales.class).create();
		listOfFestivosNacionales = festivosNacionalesRepository.saveAll(listSave);
		festivosNacionalesSearch = listOfFestivosNacionales.get(0);

		festivosNacionales = Instancio.of(FestivosNacionales.class).create();
		log.info("setup - id: {}", festivosNacionales.getFecha());
	}

	@Test
	void testConsultarFestivosVigentes() {
		String fechaFormateada = festivosNacionalesSearch.getFecha().toInstant().atOffset(ZoneOffset.UTC)
				.format(DateTimeFormatter.ISO_LOCAL_DATE);
		List<Date> festivosNacionalesFind = festivosNacionalesRepository
				.consultarFestivosVigentes(festivosNacionalesSearch.getFecha());

		assertThat(festivosNacionalesFind).isNotEmpty().hasSizeGreaterThan(0);
		festivosNacionalesFind.forEach(date -> {
			assertThat(date).isAfterOrEqualTo(Instant.parse(fechaFormateada).toDate());
		});
		log.info("testConsultarFestivosVigentes - id: {}", festivosNacionalesSearch.getFecha());
	}

	@Test
	void testFestivosNacionalesSave() {
		FestivosNacionales festivosNacionalesSaved = festivosNacionalesRepository.save(festivosNacionales);

		assertThat(festivosNacionalesSaved).isNotNull();
		assertThat(festivosNacionalesSaved.getFecha()).isEqualTo(festivosNacionales.getFecha());
		log.info("testFestivosNacionalesSave - id: {}", festivosNacionalesSaved.getFecha());
	}

	@Test
	void testFestivosNacionalesFindById() {
		Optional<FestivosNacionales> festivosNacionalesFind = festivosNacionalesRepository
				.findById(festivosNacionalesSearch.getFecha());

		assertThat(festivosNacionalesFind).isNotEmpty();
		festivosNacionalesFind.ifPresent(festivosNacionales -> {
			assertThat(festivosNacionales.getFecha()).isInstanceOf(Date.class);
			assertThat(festivosNacionales.getDescripcion()).isInstanceOf(String.class);
		});
		log.info("testFestivosNacionalesFindById - id: {}", festivosNacionalesSearch.getFecha());
	}

	@Test
	void testFestivosNacionalesFindAll() {
		List<FestivosNacionales> festivosNacionalesFind = festivosNacionalesRepository.findAll();

		assertThat(festivosNacionalesFind).isNotEmpty().hasSize(listOfFestivosNacionales.size());
		log.info("testFestivosNacionalesFindAll - id: {}", festivosNacionales.getFecha());
	}

	@Test
	void testFestivosNacionalesDelete() {
		festivosNacionalesRepository.delete(festivosNacionalesSearch);
		Optional<FestivosNacionales> festivosNacionalesFind = festivosNacionalesRepository
				.findById(festivosNacionalesSearch.getFecha());
		assertThat(festivosNacionalesFind).isEmpty();
		log.info("testFestivosNacionalesDelete - id: {}", festivosNacionalesSearch.getFecha());
	}

}
