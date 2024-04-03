package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.Puntos;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class IPuntosRepositoryTest {
	
	@Autowired
	private IPuntosRepository puntosRepository;
	private Puntos puntos;
	private List<Puntos> listOfPuntos;
	private Puntos puntosSearch;

	@BeforeEach
	void setUp() throws Exception {
		List<Puntos> listSave = Instancio.ofList(Puntos.class).size(11)
				.set(field(Puntos::getCodigoPunto), null)
				.set(field(Puntos::getBancos), null)
				.set(field(Puntos::getCajeroATM), null)
				.set(field(Puntos::getFondos), null)
				.set(field(Puntos::getOficinas), null)
				.set(field(Puntos::getSitiosClientes), null)
				.set(field(Puntos::getPuntosCodigoTDV), null)
				.generate(field(Puntos::getTipoPunto), gen -> 
					gen.oneOf("BANCO", "BAN_REP", "CAJERO", "CLIENTE", "FONDO", "OFICINA", null))
				.generate(field(Puntos::getCodigoCiudad), gen -> 
					gen.oneOf("8001", "11001", "76001", "17001", "52001", null))
				.generate(field(Puntos::getNombrePunto), gen -> gen.oneOf("9099|85VIRREY SOLIS  UAB PALMIRA",
						"CB CELTEL 20 DE JULIO",
						"BANCO REPUBLICA-BARRANQUILLA",
						"CENTRO COMERCIAL BUENAVISTA 1",
						"BPOP-VALLEDUPAR-PROSEGUR",
						"CB CELTEL 20 DE JULIO",
						"9099|85VIRREY SOLIS  UAB PALMIRA", null))
				.create();
		listOfPuntos = puntosRepository.saveAllAndFlush(listSave);
		puntosSearch = listOfPuntos.get(0);
		puntos = Instancio.of(Puntos.class)
				.set(field(Puntos::getCodigoPunto), null)
				.set(field(Puntos::getBancos), null)
				.set(field(Puntos::getCajeroATM), null)
				.set(field(Puntos::getFondos), null)
				.set(field(Puntos::getOficinas), null)
				.set(field(Puntos::getSitiosClientes), null)
				.set(field(Puntos::getPuntosCodigoTDV), null)
				.generate(field(Puntos::getTipoPunto), gen -> 
					gen.oneOf("BANCO", "BAN_REP", "CAJERO", "CLIENTE", "FONDO", "OFICINA", null))
				.generate(field(Puntos::getCodigoCiudad), gen -> 
					gen.oneOf("8001", "11001", "76001", "17001", "52001", null))
				.generate(field(Puntos::getNombrePunto), gen -> gen.oneOf("9099|85VIRREY SOLIS  UAB PALMIRA",
						"CB CELTEL 20 DE JULIO",
						"BANCO REPUBLICA-BARRANQUILLA",
						"CENTRO COMERCIAL BUENAVISTA 1",
						"BPOP-VALLEDUPAR-PROSEGUR",
						"CB CELTEL 20 DE JULIO",
						"9099|85VIRREY SOLIS  UAB PALMIRA", null))
				.create();

		log.info("setup - id: {}", puntos.getNombrePunto());
	}

	@Test
	void testFindByCodigoPuntoAndTipoPunto() {
		Optional<Puntos> puntosFind = Optional.of(puntosRepository
				.findByCodigoPuntoAndTipoPunto(puntosSearch.getCodigoPunto(), puntosSearch.getTipoPunto()));

		assertThat(puntosFind).isNotNull().isNotEmpty();
		puntosFind.ifPresent(punto -> assertThat(punto).isInstanceOf(Puntos.class));
		log.info("testFindByCodigoPuntoAndTipoPunto - id: {}", puntosSearch.getCodigoPunto());
	}

	@Test
	void testFindByCodigoPuntoAndTipoPuntoWhenTipoPuntoIsNull() {
		Optional<Puntos> puntosFind = Optional
				.ofNullable(puntosRepository.findByCodigoPuntoAndTipoPunto(puntosSearch.getCodigoPunto(), null));

		puntosFind.ifPresent(punto -> {
			assertThat(punto).isInstanceOf(Puntos.class);
		});
		log.info("testFindByCodigoPuntoAndTipoPuntoWhenTipoPuntoIsNull - id: {}", puntosSearch.getCodigoPunto());
	}

	@Test
	void testFindByCodigoPunto() {
		Puntos puntoFind = puntosRepository.findByCodigoPunto(puntosSearch.getCodigoPunto());

		assertThat(puntoFind).isNotNull().isInstanceOf(Puntos.class);
		log.info("testFindByCodigoPunto - id: {}", puntoFind.getCodigoPunto());
	}
	
	@Test
	void testFindByCodigoPuntoWhenCodigoPuntoIsNull() {
		Puntos puntoFind = puntosRepository.findByCodigoPunto(null);

		assertThat(puntoFind).isNull();
		log.info("testFindByCodigoPuntoWhenCodigoPuntoIsNull - id is Null");
	}

	@Test
	@Disabled("Se lanza excepcion cuando retorna mas de un registro")
	void testFindByNombrePunto() {
		Puntos puntoFind = puntosRepository.findByNombrePunto(puntosSearch.getNombrePunto());

		assertThat(puntoFind).isNotNull();
		log.info("testFindByNombrePunto - id: {}", puntoFind.getCodigoPunto());
	}

	@Test
	@Disabled("Se lanza excepcion cuando retorna mas de un registro")
	void testFindByTipoPuntoAndCodigoCiudad() {
		Puntos puntoFind = puntosRepository.findByTipoPuntoAndCodigoCiudad(puntosSearch.getTipoPunto(),
				puntosSearch.getCodigoCiudad());

		assertThat(puntoFind).isNotNull();
		log.info("testFindByTipoPuntoAndCodigoCiudad - id: {}", puntoFind.getCodigoPunto());
	}

	@Test
	void testFindByTipoPuntoAndNombrePunto() {
		Puntos puntoFind = puntosRepository.findByTipoPuntoAndNombrePunto(puntosSearch.getTipoPunto(),
				puntosSearch.getNombrePunto());

		assertThat(puntoFind).isNotNull();
		log.info("testFindByTipoPuntoAndNombrePunto - id: {}", puntoFind.getCodigoPunto());
	}

	@Test
	@Disabled("Se debe crear registros de SitiosClientes y ClientesCorporativos")
	void testObtenerCodigoPunto() {
		fail("Not yet implemented");
	}

	@Test
	void testFindByTipoPunto() {
		List<Puntos> puntosFind = puntosRepository.findByTipoPunto(puntosSearch.getTipoPunto());
		
		assertThat(puntosFind).isNotNull().isNotEmpty().hasSizeGreaterThan(0);
		log.info("testFindByTipoPunto - size: {}", puntosFind.size());
	}

	@Test
	void testIPuntosSave() {
		Puntos puntosSaved = puntosRepository.save(puntos);
		
		assertThat(puntosSaved).isNotNull();
		assertThat(puntosSaved.getCodigoPunto()).isNotNull();
		log.info("testIPuntosSave - id: {}", puntosSaved.getCodigoPunto());
	}

	@Test
	void testIPuntosFindById() {
		Optional<Puntos> puntosFind = puntosRepository.findById(puntosSearch.getCodigoPunto());
		
		assertThat(puntosFind).isNotEmpty();
		puntosFind.ifPresent(punto -> {
			assertThat(punto.getCodigoPunto()).isEqualTo(puntosSearch.getCodigoPunto());
			assertThat(punto.getCodigoCiudad()).isEqualTo(puntosSearch.getCodigoCiudad());
			assertThat(punto.getEstado()).isEqualTo(puntosSearch.getEstado());
			assertThat(punto.getNombrePunto()).isEqualTo(puntosSearch.getNombrePunto());
			assertThat(punto.getTipoPunto()).isEqualTo(puntosSearch.getTipoPunto());
		});
		log.info("testIPuntosFindById - id: {}", puntosSearch.getCodigoPunto());
	}

	@Test
	void testIPuntosFindAll() {
		List<Puntos> puntosFind = puntosRepository.findAll();
		
		assertThat(puntosFind).isNotEmpty().hasSize(listOfPuntos.size());
		log.info("testIPuntosFindAll - size: {}", puntosFind.size());
	}

	@Test
	void testIPuntosDelete() {
		puntosRepository.delete(puntosSearch);
		Optional<Puntos> puntosFind = puntosRepository.findById(puntosSearch.getCodigoPunto());
		
		assertThat(puntosFind).isEmpty();
		log.info("testIPuntosDelete - id: {}", puntosSearch.getCodigoPunto());
	}

}
