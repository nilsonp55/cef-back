package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.Dominio;
import com.ath.adminefectivo.entities.id.DominioPK;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class DominioTest {
	
	@Autowired
	private DominioRepository dominioRepository;
	
	private Dominio dominio;
	private Dominio dominio2;
	private Dominio dominio3;
	DominioPK dominioPK;
	
	@BeforeEach
	void setup() {
		
		dominioPK = DominioPK.builder()
				.codigo("F1")
				.dominio("FORMATO_FECHA")
				.build();
		
		DominioPK dominioPK2 = DominioPK.builder()
				.codigo("F4")
				.dominio("FORMATO_FECHA")
				.build();
		
		DominioPK dominioPK3 = DominioPK.builder()
				.codigo("AD")
				.dominio("FAMILIAS")
				.build();
		
		dominio = Dominio.builder()
				.dominioPK(dominioPK)
				.descripcion("Formato fecha formato 1")
				.estado("1")
				.tipo("T")
				.valorFecha(null)
				.valorNumero(null)
				.valorTexto("dd/MM/yyyy")
				.fechaCreacion(Date.from(Instant.now()))
				.fechaModificacion(Date.from(Instant.now()))
				.usuarioCreacion("user09")
				.usuarioModificacion("user09")
				.build();
		
		dominio2 = Dominio.builder()
				.dominioPK(dominioPK2)
				.descripcion("Formato fecha formato 4")
				.estado("1")
				.tipo("T")
				.valorFecha(null)
				.valorNumero(null)
				.valorTexto("yyyy-MM-dd")
				.fechaCreacion(Date.from(Instant.now()))
				.fechaModificacion(Date.from(Instant.now()))
				.usuarioCreacion("user08")
				.usuarioModificacion("user08")
				.build();
		
		dominio3 = Dominio.builder()
				.dominioPK(dominioPK3)
				.descripcion("Antigua Denominación")
				.estado("1")
				.tipo("T")
				.valorFecha(null)
				.valorNumero(null)
				.valorTexto("AD")
				.fechaCreacion(Date.from(Instant.now()))
				.fechaModificacion(Date.from(Instant.now()))
				.usuarioCreacion("user07")
				.usuarioModificacion("user07")
				.build();
		
		log.info("setup - dominioPK: {} - dominio: {}", dominioPK, dominio);
	}
	
	@Test
	void testDominioSave() {
		
		Dominio dominioSaved = dominioRepository.save(dominio);
		
		assertThat(dominioSaved).isNotNull();
		assertThat(dominioSaved.getDominioPK().getCodigo()).isNotNull();
		
		log.info("testDominioSave dominio save: {}", dominioSaved);
	}

	@Test
	void testDominioFindAll() {
		dominioRepository.save(dominio);
		dominioRepository.save(dominio2);
		dominioRepository.save(dominio3);
		
		List<Dominio> dominioList = dominioRepository.findAll();
		
		assertThat(dominioList).isNotEmpty().hasSize(3);
		
		log.info("testDominioFindAll dominioList size: {}", dominioList.size());
	}
	
	@Test
	void testDominioFindById() {
		dominioRepository.save(dominio);
				
		Optional<Dominio> dominioFind = dominioRepository.findById(dominioPK);
		
		assertThat(dominioFind).isNotEmpty();
		assertThat(dominioFind.get().getDominioPK().getCodigo()).isEqualTo(dominioPK.getCodigo());
		assertThat(dominioFind.get().getDominioPK().getDominio()).isEqualTo(dominioPK.getDominio());
		
		log.info("testDominioFindById dominio {}", dominioPK);
	}
	
	@Test
	void testDominioFindById_returnNotFound() {
		DominioPK dominioPK3 = DominioPK.builder()
				.codigo("AD")
				.dominio("FAMILIAS")
				.build();
		
		Optional<Dominio> dominioFind = dominioRepository.findById(dominioPK3);
		
		assertThat(dominioFind).isEmpty();
		
		log.info("testDominioFindById_returnNotFound dominio empty: {}", dominioFind.isEmpty());
	}
	
	@Test
	void testDominioFindByDominioPkDominio() {
		String pk_dominio = "FAMILIAS";
		String pk_codigo = "AD";
		dominioRepository.save(dominio3);
		
		List<Dominio> listDominio = dominioRepository.findByDominioPKDominio(pk_dominio);
		
		assertThat(listDominio).isNotEmpty().hasSize(1);
		assertThat(listDominio.get(0).getDominioPK().getDominio()).isEqualTo(pk_dominio);
		assertThat(listDominio.get(0).getDominioPK().getCodigo()).isEqualTo(pk_codigo);
		
		log.info("testDominioFindByDominioPkDominio listDominio {}", listDominio.size());
	}
	
	@Test
	void testDominioFindByDominioPkDominio_returnNotFound() {
		String pk_dominio = "NOT_EXISTS";
		
		List<Dominio> listDominio = dominioRepository.findByDominioPKDominio(pk_dominio);
		
		assertThat(listDominio).isEmpty();
		log.info("testDominioFindByDominioPkDominio_returnNotFound listDominio {}", listDominio.size());
	}
	
	@Test
	void testDominioUpdate() {
		dominioRepository.save(dominio);				
		Dominio dominioFind = dominioRepository.findById(dominioPK).get();
		
		dominioFind.setDescripcion("Descripcion test");
		dominioFind.setEstado("0");
		dominioFind.setTipo("M");
		dominioFind.setValorFecha(Date.from(Instant.now()));
		dominioFind.setValorNumero(Double.valueOf("20"));
		dominioFind.setValorTexto("Valor Texto Test");;
		dominioFind.setFechaCreacion(Date.from(Instant.now()));
		dominioFind.setFechaModificacion(Date.from(Instant.now()));
		dominioFind.setUsuarioCreacion("user01");
		dominioFind.setUsuarioModificacion("user01");
		
		Dominio dominioSaved = dominioRepository.save(dominioFind);
		
		assertThat(dominioSaved).isNotNull();
		assertThat(dominioSaved.getDominioPK().getCodigo()).isEqualTo(dominioFind.getDominioPK().getCodigo());
		assertThat(dominioSaved.getDominioPK().getDominio()).isEqualTo(dominioFind.getDominioPK().getDominio());
		assertThat(dominioSaved.getDescripcion()).isEqualTo(dominioFind.getDescripcion());
		assertThat(dominioSaved.getEstado()).isEqualTo(dominioFind.getEstado());
		assertThat(dominioSaved.getTipo()).isEqualTo(dominioFind.getTipo());
		assertThat(dominioSaved.getValorFecha()).isEqualTo(dominioFind.getValorFecha());
		assertThat(dominioSaved.getValorNumero()).isEqualTo(dominioFind.getValorNumero());
		assertThat(dominioSaved.getValorTexto()).isEqualTo(dominioFind.getValorTexto());
		assertThat(dominioSaved.getFechaCreacion()).isEqualTo(dominioFind.getFechaCreacion());
		assertThat(dominioSaved.getFechaModificacion()).isEqualTo(dominioFind.getFechaModificacion());
		assertThat(dominioSaved.getUsuarioCreacion()).isEqualTo(dominioFind.getUsuarioCreacion());
		assertThat(dominioSaved.getUsuarioModificacion()).isEqualTo(dominioFind.getUsuarioModificacion());
		
		log.info("testDominioUpdate dominioSaved {}", dominioSaved);
		;
	}
	
	@Test
	void testDeleteDominio() {
		Dominio dominioSaved = dominioRepository.save(dominio);
		
		dominioRepository.delete(dominio);
		Optional<Dominio> dominioFind = dominioRepository.findById(dominioSaved.getDominioPK());
		
		assertThat(dominioFind).isEmpty();
		
		log.info("testDominioUpdate dominioFind {}", dominioFind);
	}
}
