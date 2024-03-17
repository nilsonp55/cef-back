package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.DominioMaestro;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class DominioMaestroRepositoryTest {
	
	@Autowired
	private DominioMaestroRepository dominioMaestroRepository;
	
	private DominioMaestro dominioMaestro;
	
	@BeforeEach
	void setup() {
		
		dominioMaestro = DominioMaestro.builder()
				.descripcion("Dominio Maestro Uno")
				.dominio("DominioUno")
				.estado("A")
				.tipoContenido('S')
				.build();
		log.info("setup: ", dominioMaestro);
		
	}
	
	@Test
	void testDominioMaestroRepository() {
		
		DominioMaestro dominioMaestroSaved = dominioMaestroRepository.save(dominioMaestro);
		
		assertThat(dominioMaestroSaved).isNotNull();
		log.info("metodo repository save: ", dominioMaestroSaved);
		
		DominioMaestro dominioMaestro2 = DominioMaestro.builder()
				.descripcion("Dominio Maestro Dos")
				.dominio("DominioDos")
				.estado("A")
				.tipoContenido('M')
				.build();
		
		dominioMaestroSaved = dominioMaestroRepository.save(dominioMaestro2);
		List<DominioMaestro> listDominioMaestro = dominioMaestroRepository.findAll();
		assertThat(listDominioMaestro).isNotEmpty().hasSize(2);
		
		Optional<DominioMaestro> dominioMaestroFind = dominioMaestroRepository.findById("DominioDos");
		assertThat(dominioMaestroFind).isNotEmpty();
		assertThat(dominioMaestroFind.get().getDescripcion()).isEqualTo(dominioMaestro2.getDescripcion());
		assertThat(dominioMaestroFind.get().getTipoContenido()).isEqualTo(dominioMaestro2.getTipoContenido());
		assertThat(dominioMaestroFind.get().getEstado()).isEqualTo(dominioMaestro2.getEstado());
		
		String descripcion = "nueva descripcion";
		String estado = "I";
		char tipoContenido = 'X';
		dominioMaestroFind.get().setDescripcion(descripcion);
		dominioMaestroFind.get().setEstado(estado);
		dominioMaestroFind.get().setTipoContenido(tipoContenido);
		
		dominioMaestroSaved = dominioMaestroRepository.save(dominioMaestroFind.get());
		assertThat(dominioMaestroSaved).isNotNull();
		assertThat(dominioMaestroSaved.getDescripcion()).isEqualTo(descripcion);
		assertThat(dominioMaestroSaved.getEstado()).isEqualTo(estado);
		assertThat(dominioMaestroSaved.getTipoContenido()).isEqualTo(tipoContenido);
	}
	
	@Test
	void testFindByEstado() {
		dominioMaestroRepository.save(dominioMaestro);
		
		List<DominioMaestro> listDominioMaestro = dominioMaestroRepository.findByEstado("A");
		assertThat(listDominioMaestro).isNotEmpty().hasSize(1);
	}

}
