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

import com.ath.adminefectivo.entities.BitacoraAutomaticos;
import com.ath.adminefectivo.entities.DetallesProcesoAutomatico;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class IBitacoraAutomaticosRepositoryTest {
	
	@Autowired
	private IBitacoraAutomaticosRepository bitacoraAutomaticosRepository;
	private BitacoraAutomaticos bitacoraAutomaticos;
	private List<BitacoraAutomaticos> listOfBitacoraAutomaticos;
	private BitacoraAutomaticos searchBitacoraAutomaticos;

	@BeforeEach
	void setUp() throws Exception {
		
		List<DetallesProcesoAutomatico> listDetalles = Instancio.ofList(DetallesProcesoAutomatico.class).size(5)
			.create();
		
		List<BitacoraAutomaticos> list = Instancio.ofList(BitacoraAutomaticos.class).size(11)
			.set(field(BitacoraAutomaticos::getDetallesProcesosAutomaticos), listDetalles)
			.create();
		listOfBitacoraAutomaticos = bitacoraAutomaticosRepository.saveAllAndFlush(list);
		searchBitacoraAutomaticos = listOfBitacoraAutomaticos.get(0);
		
		bitacoraAutomaticos = Instancio.of(BitacoraAutomaticos.class)
				.set(field(BitacoraAutomaticos::getDetallesProcesosAutomaticos), null)
				.create();
		
		log.info("setup - id: {}", bitacoraAutomaticos.getIdRegistro());
	}

	@Test
	void testIBitacoraAutomaticosSave() {
		BitacoraAutomaticos bitacoraAutomaticosSaved = bitacoraAutomaticosRepository.save(bitacoraAutomaticos);
		
		assertThat(bitacoraAutomaticosSaved).isNotNull();
		log.info("testIBitacoraAutomaticosSave - id: {}", bitacoraAutomaticosSaved.getIdRegistro());
	}

	@Test
	void testIBitacoraAutomaticosFindById() {
		Optional<BitacoraAutomaticos> bitacoraAutomaticosFind = bitacoraAutomaticosRepository
				.findById(searchBitacoraAutomaticos.getIdRegistro());

		assertThat(bitacoraAutomaticosFind).isNotEmpty();
		bitacoraAutomaticosFind.ifPresent(bitacora -> {
			assertThat(bitacora.getIdRegistro()).isNotNull();
		});
		log.info("testIBitacoraAutomaticosFindById - id: {}", searchBitacoraAutomaticos.getIdRegistro());
	}

	@Test
	void testIBitacoraAutomaticosFindAll() {
		List<BitacoraAutomaticos> bitacoraAutomaticosFind = bitacoraAutomaticosRepository.findAll();

		assertThat(bitacoraAutomaticosFind).isNotEmpty().hasSize(listOfBitacoraAutomaticos.size());
		log.info("testIBitacoraAutomaticosFindAll - size: {}", bitacoraAutomaticosFind.size());
	}

	@Test
	void testIBitacoraAutomaticosDelete() {
		bitacoraAutomaticosRepository.delete(searchBitacoraAutomaticos);
		Optional<BitacoraAutomaticos> bitacoraAutomaticosFind = bitacoraAutomaticosRepository
				.findById(searchBitacoraAutomaticos.getIdRegistro());

		assertThat(bitacoraAutomaticosFind).isEmpty();
		log.info("testIBitacoraAutomaticosDelete - id: {}", searchBitacoraAutomaticos.getIdRegistro());
	}

}
