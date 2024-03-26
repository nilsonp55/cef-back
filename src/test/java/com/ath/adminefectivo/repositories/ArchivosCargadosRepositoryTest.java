/**
 * 
 */
package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.ArchivosCargados;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class ArchivosCargadosRepositoryTest {

	@Autowired
	private ArchivosCargadosRepository archivosCargadosRepository;
	private ArchivosCargados archivosCargados;
	private List<ArchivosCargados> listArchivosCargados;

	@BeforeEach
	void setUp() throws Exception {

		listArchivosCargados = Instancio.ofList(ArchivosCargados.class).size(11)
				.ignore(field(ArchivosCargados::getIdArchivo)).set(field(ArchivosCargados::getFallasArchivos), null)
				.set(field(ArchivosCargados::getRegistrosCargados), null).create();
		archivosCargados = listArchivosCargados.get(0);

		log.info("setup - ArchivosCargados.id: {}", archivosCargados.getIdModeloArchivo());
	}

	@Test
	void testFindByIdModeloArchivoAndIdArchivo() {
		archivosCargadosRepository.saveAll(listArchivosCargados);

		List<ArchivosCargados> archivosCargadosFind = archivosCargadosRepository.findByIdModeloArchivoAndIdArchivo(
				archivosCargados.getIdModeloArchivo(), archivosCargados.getIdArchivo());
		
		assertThat(archivosCargadosFind).isNotEmpty().hasSizeGreaterThan(0);
		log.info("testFindByIdModeloArchivoAndIdArchivo - IdModeloArchivo: {} - IdArchivo: {}",
				archivosCargados.getIdModeloArchivo(), archivosCargados.getIdArchivo());
	}

	@Test
	void testFindByEstadoCargueAndFechaArchivoBetween() {
		archivosCargadosRepository.saveAll(listArchivosCargados);

		List<ArchivosCargados> archivosCargadosFind = archivosCargadosRepository
				.findByEstadoCargueAndFechaArchivoBetween(archivosCargados.getEstadoCargue(),
						archivosCargados.getFechaArchivo(), archivosCargados.getFechaArchivo());

		assertThat(archivosCargadosFind).isNotEmpty().hasSizeGreaterThan(0);
		log.info("testFindByEstadoCargueAndFechaArchivoBetween - EstadoCargue: {} - FechaArchivo: {}",
				archivosCargados.getEstadoCargue(), archivosCargados.getFechaArchivo());
	}

	@Test
	void testFindByEstadoCargue() {
		archivosCargadosRepository.saveAll(listArchivosCargados);

		List<ArchivosCargados> archivosCargadosFind = archivosCargadosRepository
				.findByEstadoCargue(archivosCargados.getEstadoCargue());

		assertThat(archivosCargadosFind).isNotEmpty().hasSizeGreaterThan(0);
		log.info("testFindByEstadoCargue - EstadoCargue: {}", archivosCargados.getEstadoCargue());
	}

	@Test
	void testFindByEstadoCargueAndIdModeloArchivo() {
		archivosCargadosRepository.saveAll(listArchivosCargados);

		List<ArchivosCargados> archivosCargadosFind = archivosCargadosRepository.findByEstadoCargueAndIdModeloArchivo(
				archivosCargados.getEstadoCargue(), archivosCargados.getIdModeloArchivo());

		assertThat(archivosCargadosFind).isNotEmpty().hasSizeGreaterThan(0);
		log.info("testFindByEstadoCargueAndIdModeloArchivo - EstadoCargue: {} - IdModeloArchivo: {}",
				archivosCargados.getEstadoCargue(), archivosCargados.getIdModeloArchivo());
	}

	@Test
	void testGetArchivosByAgrupador() {
		assertTrue(true);
	}

	@Test
	void testGetRegistrosCargadosSinProcesarDeHoy() {
		assertTrue(true);
	}

	@Test
	void testGetIdArchivosCargadosPorAgrupadorFechaEstado() {
		assertTrue(true);
	}

	@Test
	void testGetRegistrosCargadosPorNombreyEstado() {
		archivosCargadosRepository.saveAll(listArchivosCargados);

		List<ArchivosCargados> archivosCargadosFind = archivosCargadosRepository.getRegistrosCargadosPorNombreyEstado(
				archivosCargados.getEstadoCargue(), archivosCargados.getNombreArchivo(),
				archivosCargados.getIdModeloArchivo());

		assertThat(archivosCargadosFind).isNotEmpty().hasSizeGreaterThan(0);
		log.info(
				"testGetRegistrosCargadosPorNombreyEstado - EstadoCargue: {} - NombreArchivo: {} - IdModeloArchivo: {}",
				archivosCargados.getEstadoCargue(), archivosCargados.getNombreArchivo(),
				archivosCargados.getIdModeloArchivo());
	}

	@Test
	void testGetRegistrosCargadosPorEstadoCargueyNombreUpperyModelo() {
		archivosCargadosRepository.saveAll(listArchivosCargados);

		List<ArchivosCargados> archivosCargadosFind = archivosCargadosRepository
				.getRegistrosCargadosPorEstadoCargueyNombreUpperyModelo(archivosCargados.getEstadoCargue(),
						archivosCargados.getNombreArchivoUpper(), archivosCargados.getIdModeloArchivo());

		assertThat(archivosCargadosFind).isNotEmpty().hasSizeGreaterThan(0);
		log.info(
				"testGetRegistrosCargadosPorEstadoCargueyNombreUpperyModelo - EstadoCargue: {} - NombreArchivoUpper: {} - IdModeloArchivo: {}",
				archivosCargados.getEstadoCargue(), archivosCargados.getNombreArchivoUpper(),
				archivosCargados.getIdModeloArchivo());
	}

	@Test
	void testFindByFechaArchivo() {
		archivosCargadosRepository.saveAll(listArchivosCargados);

		List<ArchivosCargados> archivosCargadosFind = archivosCargadosRepository
				.findByFechaArchivo(archivosCargados.getFechaArchivo());

		assertThat(archivosCargadosFind).isNotEmpty().hasSizeGreaterThan(0);
		log.info("testFindByFechaArchivo - archivosCargados.fechaArchivo: {}", archivosCargados.getFechaArchivo());
	}

	@Test
	void testSave() {
		ArchivosCargados archivosCargadosSaved = archivosCargadosRepository.save(archivosCargados);

		assertThat(archivosCargadosSaved).isNotNull();
		log.info("testSave - archivosCargados.id: {}", archivosCargadosSaved.getIdArchivo());
	}

	@Test
	void testFindById() {
		archivosCargados = archivosCargadosRepository.saveAll(listArchivosCargados).get(0);

		Optional<ArchivosCargados> archivosCargadosFind = archivosCargadosRepository
				.findById(archivosCargados.getIdArchivo());

		assertThat(archivosCargadosFind).isNotEmpty();
		archivosCargadosFind.ifPresent(archivo -> {
			assertThat(archivo.getIdArchivo()).isEqualTo(archivosCargados.getIdArchivo());
		});
		log.info("testFindById - archivosCargados.id: {}", archivosCargados.getIdArchivo());
	}

	@Test
	void testFindAll() {
		archivosCargadosRepository.saveAll(listArchivosCargados);

		List<ArchivosCargados> archivosCargadosFind = archivosCargadosRepository.findAll();

		assertThat(archivosCargadosFind).isNotEmpty().hasSize(11);
		log.info("testFindAll list: {}", archivosCargadosFind.size());
	}

	@Test
	void testDelete() {
		archivosCargados = archivosCargadosRepository.saveAll(listArchivosCargados).get(0);
		archivosCargadosRepository.delete(archivosCargados);

		Optional<ArchivosCargados> archivosCargadosFind = archivosCargadosRepository
				.findById(archivosCargados.getIdArchivo());
		assertThat(archivosCargadosFind).isEmpty();
		log.info("testDelete - archivosCargados.id: {}", archivosCargados.getIdArchivo());
	}

}
