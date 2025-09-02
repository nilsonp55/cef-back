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

import com.ath.adminefectivo.entities.CajerosATM;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class CajerosATMRepositoryTest {

	@Autowired
	private CajerosATMRepository cajerosATMRepository;
	private CajerosATM cajerosATM;
	private List<CajerosATM> listOfCajerosATM;

	@BeforeEach
	void setUpCajerosATMRepository() throws Exception {
		listOfCajerosATM = Instancio.ofList(CajerosATM.class).size(11)
				.set(field(CajerosATM::getPunto), null)
				.create();

		cajerosATMRepository.saveAll(listOfCajerosATM);

		cajerosATM = Instancio.of(CajerosATM.class)
				.set(field(CajerosATM::getPunto), null)
				.create();

		log.info("setUpCajerosATMRepository - id: {}", cajerosATM.getCodigoPunto());
	}

	@Test
	void testSaveCajerosATMRepository() {
		CajerosATM cajerosATMSave = cajerosATMRepository.save(cajerosATM);
		assertThat(cajerosATMSave).isNotNull();
		log.info("testSaveCajerosATMRepository - id: {}", cajerosATMSave.getCodigoPunto());
	}

	@Test
	void testFindByIdCajerosATMRepository() {
		CajerosATM cajerosATMSearch = listOfCajerosATM.get(0);
		Optional<CajerosATM> cajerosATMFind = cajerosATMRepository.findById(cajerosATMSearch.getCodigoPunto());

		assertThat(cajerosATMFind).isNotEmpty();
		log.info("testFindByIdCajerosATMRepository = id: {}", cajerosATMSearch.getCodigoPunto());
	}

	@Test
	void testFindAllCajerosATMRepository() {
		List<CajerosATM> cajerosATMFind = cajerosATMRepository.findAll();

		assertThat(cajerosATMFind).isNotEmpty().hasSize(11);
		log.info("testFindAllCajerosATMRepository - size: {}", cajerosATMFind.size());
	}

	@Test
	void testDeleteCajerosATMRepository() {
		CajerosATM cajerosATMSearch = listOfCajerosATM.get(0);

		cajerosATMRepository.delete(cajerosATMSearch);
		Optional<CajerosATM> cajerosATMFind = cajerosATMRepository.findById(cajerosATMSearch.getCodigoPunto());

		assertThat(cajerosATMFind).isEmpty();
		log.info("testDeleteCajerosATMRepository - size: {}", cajerosATMSearch.getCodigoPunto());
	}

}
