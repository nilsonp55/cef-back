package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.ath.adminefectivo.entities.AuditoriaLogin;

@DataJpaTest
class AuditoriaLoginRepositoryTest {
	
	@Autowired
	private AuditoriaLoginRepository auditoriaLoginRepository;
	
	private AuditoriaLogin auditoriaLogin;

	@BeforeEach
	void setUp() throws Exception {
		
		auditoriaLogin = AuditoriaLogin.builder()
				.usuario("User1")
				.fechaIngreso(Date.from(Instant.now()))
				.build();
	}

	@DisplayName("Dado un objeto Entity AuditoriaLogin al guardar retornar el entity AuditoriaLogin creado.")
	@Test
	void testSave() {
		// given
		
		// when
		AuditoriaLogin auditoriaLoginSaved = auditoriaLoginRepository.save(auditoriaLogin);
		
		// then
		assertThat(auditoriaLoginSaved).isNotNull()
			.isInstanceOf(AuditoriaLogin.class);
		
	}
	
	@Test
	void testSavedException() {
		// given
		AuditoriaLogin auditoriaLoginNull = null;
		
		// when		
		assertThrows(InvalidDataAccessApiUsageException.class, () -> 
			auditoriaLoginRepository.save(auditoriaLoginNull));
		
		// then
		
	}

	@DisplayName("Dado un Id AuditoriaLogin, cuando se realice FindById, retornar entity AuditoriaLogin")
	@Test
	void testFindById() {
		// given
		AuditoriaLogin auditoriaLoginSaved = auditoriaLoginRepository.save(auditoriaLogin);
		
		// when 
		Optional<AuditoriaLogin> auditoriaLoginFind = auditoriaLoginRepository.findById(auditoriaLoginSaved.getId());
		
		// then
		assertThat(auditoriaLoginFind).isNotEmpty();
		assertThat(auditoriaLoginFind.get().getId()).isEqualTo(auditoriaLoginSaved.getId());
	}

	@DisplayName("Dado un List de AuditoriaLogin, cuando se realice un FinaAll, retornar un List de Entities AuditoriaLogin")
	@Test
	void testFindAll() {
		// given
		AuditoriaLogin auditoriaLogin2 = AuditoriaLogin.builder()
				.usuario("User2")
				.fechaIngreso(Date.from(Instant.now()))
				.build();
		auditoriaLoginRepository.save(auditoriaLogin);
		auditoriaLoginRepository.save(auditoriaLogin2);
		
		// when
		List<AuditoriaLogin> auditoriaLoginFindAll = auditoriaLoginRepository.findAll();
		
		// then
		assertThat(auditoriaLoginFindAll).isNotEmpty().hasSize(2);
	}
	
	@Test
	void testUpdateAuditoriaLogin() {
		// given
		auditoriaLoginRepository.save(auditoriaLogin);
		Date fechaIngreso = Date.from(Instant.now());
		
		// when 
		AuditoriaLogin auditoriaLoginFind = auditoriaLoginRepository.findById(auditoriaLogin.getId()).get();
		auditoriaLoginFind.setFechaIngreso(fechaIngreso);
		auditoriaLoginFind.setUsuario("User123");
		
		AuditoriaLogin auditoriaLoginSaved = auditoriaLoginRepository.save(auditoriaLoginFind);
		
		// then
		assertThat(auditoriaLoginSaved).isNotNull();
		assertThat(auditoriaLoginSaved.getUsuario()).isEqualTo("User123");
		assertThat(auditoriaLoginSaved.getFechaIngreso()).isEqualTo(fechaIngreso);
		
	}
	
	@DisplayName("Dado un Id de AuditoriaLogin, cuando realice delete, no retorna entity AuditoriaLogin")
	@Test
	void testDeleteById() {
		// given
		AuditoriaLogin auditoriaLoginSaved = auditoriaLoginRepository.save(auditoriaLogin);
		
		// when 
		auditoriaLoginRepository.delete(auditoriaLogin);
		Optional<AuditoriaLogin> auditoriaLoginFind = auditoriaLoginRepository.findById(auditoriaLoginSaved.getId());
		
		// then
		assertThat(auditoriaLoginFind).isEmpty();
	}

}
