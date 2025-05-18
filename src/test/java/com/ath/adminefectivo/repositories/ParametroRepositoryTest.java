package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.Parametro;

@DataJpaTest
class ParametroRepositoryTest {
	
	@Autowired
	private ParametroRepository parametroRepository;
	
	private Parametro parametro;

	@BeforeEach
	void setUp() throws Exception {
		parametro = Parametro.builder()
				.areaAplicativo("Modulo 1")
				.codigo("PARAM1")
				.descripcion("Descripcion parametro")
				.valor("ValorParam")
				.estado("A")
				.fechaCreacion(Date.from(Instant.now()))
				.fechaModificacion(Date.from(Instant.now()))
				.usuarioCreacion("User123")
				.usuarioModificacion("User123")
				.build();
				
	}

	@DisplayName("Dado un objeto Entity Parametro al guardar retornar el entity Parametro creado.")
	@Test
	void testSave() {
		// given
		
		// when
		Parametro parametroSaved = parametroRepository.save(parametro);
		
		// then
		assertThat(parametroSaved).isNotNull()
			.isInstanceOf(Parametro.class);
	}

	@DisplayName("Dado un List de Parametros, cuando se realice un FinaAll, retornar un List de Entities Parametro")
	@Test
	void testFindAll() {
		// given
		parametroRepository.save(parametro);
		Parametro parametro2 = Parametro.builder()
				.areaAplicativo("Modulo 2")
				.codigo("1_PARAM")
				.descripcion("Descripcion parametro 2")
				.valor("Codigo Parametro 2")
				.estado("A")
				.fechaCreacion(Date.from(Instant.now()))
				.fechaModificacion(Date.from(Instant.now()))
				.usuarioCreacion("User123")
				.usuarioModificacion("User123")
				.build();
		parametroRepository.save(parametro2);
		
		// when 
		List<Parametro> listParametros = parametroRepository.findAll();
		
		// then
		assertThat(listParametros).isNotNull()
			.hasSize(2);
		
	}
	
	@DisplayName("Dado un codigo_param, cuando se realice FindById, retornar entity Parametro")
	@Test
	void testFindById() {
		// given
		parametroRepository.save(parametro);
		String codigoParametroFind = "PARAM1";
		
		// when 
		Optional<Parametro> parametroFind = parametroRepository.findById(codigoParametroFind);
		
		// then
		assertThat(parametroFind).isNotEmpty();
		assertThat(parametroFind.get()).isInstanceOf(Parametro.class);
		assertThat(parametroFind.get().getValor()).isEqualTo(parametro.getValor());
	}
	

	@DisplayName("Dado un codigo_param, cuando realice delete, no retorna entity Parametro")
	@Test
	void testDelete() {
		// given
		parametroRepository.save(parametro);
		String codigoParametroFind = "PARAM1";
		
		// when
		parametroRepository.deleteById(codigoParametroFind);
		Optional<Parametro> parametroFind = parametroRepository.findById(codigoParametroFind);
		
		// then
		assertThat(parametroFind).isEmpty();
		
	}

}
