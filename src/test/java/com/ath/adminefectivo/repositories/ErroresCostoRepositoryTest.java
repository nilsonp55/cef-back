package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.ErroresCostos;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class ErroresCostoRepositoryTest {

	@Autowired
	private ErroresCostosRepository erroresCostosRepository;
	
	private ErroresCostos erroresCostos;
	
	@BeforeEach
	void setup() throws Exception {
		
		erroresCostos = ErroresCostos.builder()
				.estado("A")
				.fecha(Date.from(Instant.now()))
				.mensajeError("Mensaje error proceso generar costos")
				.seqGrupo(123)
				.build();
		log.info("setup - {}", erroresCostos);
	};
	
	@Test
	void testErroresCostosFindBySeqGrupo() {
		
		Integer seqGrupo_find = 123;
		ErroresCostos erroresCostos2 = ErroresCostos.builder()
				.estado("A")
				.fecha(Date.from(Instant.now()))
				.mensajeError("Mensaje error 2")
				.seqGrupo(123)
				.build();
		// este reg no debe salir en el resultado dle FindBySeqGrupo
		ErroresCostos erroresCostos3 = ErroresCostos.builder()
				.estado("A")
				.fecha(Date.from(Instant.now()))
				.mensajeError("Mensaje error 3")
				.seqGrupo(1343)
				.build();
		
		List<ErroresCostos> listErroresCostosNew =  new ArrayList<>();
		listErroresCostosNew.add(erroresCostos);
		listErroresCostosNew.add(erroresCostos2);
		listErroresCostosNew.add(erroresCostos3);		
		erroresCostosRepository.saveAll(listErroresCostosNew);
		
		List<ErroresCostos> listErroresCostosFind = erroresCostosRepository.findBySeqGrupo(seqGrupo_find);
		
		assertThat(listErroresCostosFind).isNotEmpty()
			.hasSize(2)
			.doesNotContain(erroresCostos3);
		
		log.info("testErroresCostosFindBySeqGrupo - listErroresCostosFind size: {}", listErroresCostosFind.size());
	}

	@Test
	void testErroresCostosSave() {
		
		ErroresCostos erroresCostosSaved = erroresCostosRepository.save(erroresCostos);
		
		assertThat(erroresCostosSaved).isNotNull();
		assertThat(erroresCostosSaved.getIdErroresCostos()).isPositive();
		log.info("testErroresCostosSave - id: {}", erroresCostosSaved.getIdErroresCostos());
	}
	
	@Test
	void testErroresCostosFindAll() {
		
		ErroresCostos erroresCostos2 = ErroresCostos.builder()
				.estado("A")
				.fecha(Date.from(Instant.now()))
				.mensajeError("Error proceso generar costos")
				.seqGrupo(987)
				.build();
		erroresCostosRepository.save(erroresCostos2);
		erroresCostosRepository.save(erroresCostos);
		
		List<ErroresCostos> listErroresCostos = erroresCostosRepository.findAll();
		
		assertThat(listErroresCostos).isNotEmpty().hasSize(2);
		log.info("testErroresCostosFindAll - listErroresCostos size: {}", listErroresCostos.size());
	}
	
	@Test
	void testErroresCostosFindById() {
		
		ErroresCostos erroresCostosSaved = erroresCostosRepository.save(erroresCostos);
		log.info("testErroresCostosFindById - Id saved: {}", erroresCostosSaved.getIdErroresCostos());
		
		Optional<ErroresCostos> erroresCostosFind = erroresCostosRepository.findById(erroresCostosSaved.getIdErroresCostos());
		
		assertThat(erroresCostosFind).isNotEmpty();
		assertThat(erroresCostosFind.get()).isInstanceOf(ErroresCostos.class);
		
	}
	
	@Test
	void testErroresCostosUpdate() {
		ErroresCostos erroresCostosSaved = erroresCostosRepository.save(erroresCostos);
		log.info("testErroresCostosFindById - Id saved: {}", erroresCostosSaved.getIdErroresCostos());
		
		ErroresCostos erroresCostosFind = erroresCostosRepository.findById(erroresCostosSaved.getIdErroresCostos()).get();
		erroresCostosFind.setEstado("E");
		erroresCostosFind.setFecha(Date.from(Instant.now()));
		erroresCostosFind.setMensajeError("Mensaje test");
		erroresCostosFind.setSeqGrupo(916);
		
		erroresCostosSaved = erroresCostosRepository.save(erroresCostosFind);
		
		assertThat(erroresCostosSaved).isNotNull();
		assertThat(erroresCostosSaved.getEstado()).isEqualTo(erroresCostosFind.getEstado());
		assertThat(erroresCostosSaved.getFecha()).isEqualTo(erroresCostosFind.getFecha());
		assertThat(erroresCostosSaved.getMensajeError()).isEqualTo(erroresCostosFind.getMensajeError());
		assertThat(erroresCostosSaved.getSeqGrupo()).isEqualTo(erroresCostosFind.getSeqGrupo());
	}
	
	@Test
	void testErroresCostosDelete() {
		erroresCostosRepository.save(erroresCostos);
		
		erroresCostosRepository.delete(erroresCostos);
		Optional<ErroresCostos> erroresCostosFind = erroresCostosRepository.findById(erroresCostos.getIdErroresCostos());
		
		assertThat(erroresCostosFind).isEmpty();
		
	}
}
