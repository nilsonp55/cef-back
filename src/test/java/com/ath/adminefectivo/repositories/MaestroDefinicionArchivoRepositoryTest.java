package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.util.List;
import java.util.Optional;

import org.instancio.Gen;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.MaestroDefinicionArchivo;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class MaestroDefinicionArchivoRepositoryTest {

	@Autowired
	private MaestroDefinicionArchivoRepository maestroDefinicionArchivoRepository;
	private MaestroDefinicionArchivo maestroDefinicionArchivo;
	List<MaestroDefinicionArchivo> listOfMaestroDefinicionArchivo;

	@BeforeEach
	void setUp() throws Exception {

		listOfMaestroDefinicionArchivo = Instancio.ofList(MaestroDefinicionArchivo.class).size(11)
				.generate(field(MaestroDefinicionArchivo::getDelimitadorBase), gen -> gen.oneOf("C", "D", "F"))
				.set(field(MaestroDefinicionArchivo::getDelimitadorOtro), Gen.oneOf("A", "Z", null).get())
				.set(field(MaestroDefinicionArchivo::getObjetivo), Gen.oneOf("|", "L").get())
				.set(field(MaestroDefinicionArchivo::getExtension), Gen.oneOf("TXT", "csv", "txt").get())
				.generate(field(MaestroDefinicionArchivo::getDelimitadorBase), gen -> gen.string().maxLength(1))
				.generate(field(MaestroDefinicionArchivo::getDelimitadorOtro),
						gen -> gen.string().maxLength(1).allowEmpty().nullable())
				.generate(field(MaestroDefinicionArchivo::getPeriodicidad), gen -> gen.string().maxLength(1))
				.generate(field(MaestroDefinicionArchivo::getMascaraArch), gen -> gen.string().maxLength(25))
				.generate(field(MaestroDefinicionArchivo::getMetodo), gen -> gen.string().maxLength(2))
				.generate(field(MaestroDefinicionArchivo::getNumeroCantidadMinima), gen -> gen.ints().max(10)).create();
		maestroDefinicionArchivo = listOfMaestroDefinicionArchivo.get(0);
		log.info("setup - maestroDefinicionArchivo.id: {}", maestroDefinicionArchivo.getIdMaestroDefinicionArchivo());
	}

	@Test
	void testSaveMaestroDefinicionArchivoRepository() {

		MaestroDefinicionArchivo maestroDefinicionArchivoSaved = maestroDefinicionArchivoRepository
				.save(maestroDefinicionArchivo);

		assertThat(maestroDefinicionArchivoSaved).isNotNull();
		assertThat(maestroDefinicionArchivoSaved.getIdMaestroDefinicionArchivo())
				.isEqualTo(maestroDefinicionArchivo.getIdMaestroDefinicionArchivo());
		log.info("maestroDefinicionArchivo.id: {}", maestroDefinicionArchivoSaved.getIdMaestroDefinicionArchivo());
	}

	@Test
	void testFindAll() {

		maestroDefinicionArchivoRepository.saveAll(listOfMaestroDefinicionArchivo);

		List<MaestroDefinicionArchivo> listFind = maestroDefinicionArchivoRepository.findAll();

		assertThat(listFind).isNotNull().isNotEmpty().hasSize(listOfMaestroDefinicionArchivo.size());
		log.info("maestroDefinicionArchivo.id: {}", listFind.get(0).getIdMaestroDefinicionArchivo());
	}

	@Test
	void testFindById() {
		maestroDefinicionArchivoRepository.saveAll(listOfMaestroDefinicionArchivo);
		MaestroDefinicionArchivo maestroDefinicionArchivoSearch = listOfMaestroDefinicionArchivo.get(3);

		Optional<MaestroDefinicionArchivo> maestroDefinicionArchivoFind = maestroDefinicionArchivoRepository
				.findById(maestroDefinicionArchivoSearch.getIdMaestroDefinicionArchivo());

		assertThat(maestroDefinicionArchivoFind).isNotEmpty();
		assertThat(maestroDefinicionArchivoFind.get().getIdMaestroDefinicionArchivo())
				.isEqualTo(maestroDefinicionArchivoSearch.getIdMaestroDefinicionArchivo());
		log.info("maestroDefinicionArchivo.id: {}", maestroDefinicionArchivoSearch.getIdMaestroDefinicionArchivo());
	}

	@Test
	void testDelete() {
		maestroDefinicionArchivoRepository.saveAll(listOfMaestroDefinicionArchivo);
		MaestroDefinicionArchivo maestroDefinicionArchivoDelete = listOfMaestroDefinicionArchivo.get(7);
		maestroDefinicionArchivoRepository.delete(maestroDefinicionArchivoDelete);

		Optional<MaestroDefinicionArchivo> maestroDefinicionArchivoFind = maestroDefinicionArchivoRepository
				.findById(maestroDefinicionArchivoDelete.getIdMaestroDefinicionArchivo());

		assertThat(maestroDefinicionArchivoFind).isEmpty();
		log.info("maestroDefinicionArchivo.id: {}", maestroDefinicionArchivoDelete.getIdMaestroDefinicionArchivo());
	}

	@Test
	void testFindByAgrupadorAndEstado() {
		maestroDefinicionArchivoRepository.saveAll(listOfMaestroDefinicionArchivo);
		MaestroDefinicionArchivo maestroDefinicionArchivoSearch = listOfMaestroDefinicionArchivo.get(5);

		List<MaestroDefinicionArchivo> maestroDefinicionArchivoFind = maestroDefinicionArchivoRepository
				.findByAgrupadorAndEstado(maestroDefinicionArchivoSearch.getAgrupador(),
						maestroDefinicionArchivoSearch.getEstado());

		assertThat(maestroDefinicionArchivoFind).isNotEmpty().hasSizeGreaterThan(0);
		log.info("maestroDefinicionArchivo.id search: {} - size result: {}",
				maestroDefinicionArchivoSearch.getIdMaestroDefinicionArchivo(), maestroDefinicionArchivoFind.size());
	}

	@Test
	void testFindByMascaraArchLike() {
		String inicialMascara = maestroDefinicionArchivo.getMascaraArch().substring(0, 2);
		maestroDefinicionArchivoRepository.saveAll(listOfMaestroDefinicionArchivo);
		
		MaestroDefinicionArchivo maestroDefinicionArchivoFind = maestroDefinicionArchivoRepository
				.findByMascaraArchLike(inicialMascara);

		assertThat(maestroDefinicionArchivoFind).isNotNull();
		log.info("inicialMascara: {}", maestroDefinicionArchivoFind.getMascaraArch().substring(0, 2));
	}

}
