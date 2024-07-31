package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.AuditoriaProcesos;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class IAuditoriaProcesosRepositoryTest {

	@Autowired
	private IAuditoriaProcesosRepository auditoriaProcesosRepository;
	private AuditoriaProcesos auditoriaProcesos;
	private List<AuditoriaProcesos> listOfAuditoriaProcesos;
	private AuditoriaProcesos auditoriaProcesosSearch;

	@BeforeEach
	void setUp() throws Exception {
		auditoriaProcesos = Instancio.of(AuditoriaProcesos.class).create();
		List<AuditoriaProcesos> listSave = Instancio.ofList(AuditoriaProcesos.class).size(11).create();
		listOfAuditoriaProcesos = auditoriaProcesosRepository.saveAll(listSave);
		auditoriaProcesosSearch = listOfAuditoriaProcesos.get(0);

		log.info("setup - id: {}", auditoriaProcesos.getId());
	}

	@Test
	void testIAuditoriaProcesosSave() {
		AuditoriaProcesos auditoriaProcesosSaved = auditoriaProcesosRepository.save(auditoriaProcesos);

		assertThat(auditoriaProcesosSaved).isNotNull();
		assertThat(auditoriaProcesosSaved.getId()).isEqualTo(auditoriaProcesos.getId());
		log.info("testIAuditoriaProcesosSave - id: {}", auditoriaProcesosSaved.getId());
	}

	@Test
	void testIAuditoriaProcesosFindById() {
		Optional<AuditoriaProcesos> auditoriaProcesosFind = auditoriaProcesosRepository
				.findById(auditoriaProcesosSearch.getId());

		assertThat(auditoriaProcesosFind).isNotEmpty();
		auditoriaProcesosFind.ifPresent(auditoriaProcesos -> {
			assertThat(auditoriaProcesos.getId()).isEqualTo(auditoriaProcesosSearch.getId());
			assertThat(auditoriaProcesos.getFechaCreacion()).isEqualTo(auditoriaProcesosSearch.getFechaCreacion());
		});
		log.info("testIAuditoriaProcesosFindById - id: {}", auditoriaProcesosSearch.getId());
	}

	@Test
	void testIAuditoriaProcesosFindAll() {
		List<AuditoriaProcesos> auditoriaProcesosFind = auditoriaProcesosRepository.findAll();

		assertThat(auditoriaProcesosFind).isNotEmpty().hasSize(listOfAuditoriaProcesos.size());
		log.info("testIAuditoriaProcesosFindAll - size: {}", auditoriaProcesosFind.size());
	}

	@Test
	void testIAuditoriaProcesosDelete() {
		auditoriaProcesosRepository.delete(auditoriaProcesosSearch);
		Optional<AuditoriaProcesos> auditoriaProcesosFind = auditoriaProcesosRepository
				.findById(auditoriaProcesosSearch.getId());

		assertThat(auditoriaProcesosFind).isEmpty();
		log.info("testIAuditoriaProcesosDelete - id: {}", auditoriaProcesosSearch.getId());
	}

}
