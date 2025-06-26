package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import java.util.List;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.Puntos;
import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class IBancosRepositoryTest {

	@Autowired
	private IBancosRepository bancosRepository;
	@Autowired
	private IPuntosRepository puntosRepository;
	private Bancos bancos;
	private List<Bancos> listOfBancos;
	private Bancos bancosSearch;

	@BeforeEach
	void setUp() throws Exception {
		Puntos punto = Instancio.of(Puntos.class)
				.set(field(Puntos::getBancos), null)
				.set(field(Puntos::getCajeroATM), null)
				.set(field(Puntos::getFondos), null)
				.set(field(Puntos::getOficinas), null)
				.set(field(Puntos::getSitiosClientes), null)
				.set(field(Puntos::getPuntosCodigoTDV), null)
				.create();
		punto = puntosRepository.saveAndFlush(punto);
		
		List<Bancos> listSave = Instancio.ofList(Bancos.class)
				.size(11)
				.set(field(Bancos::getCodigoPunto), punto.getCodigoPunto())
				.create();		
		listOfBancos = bancosRepository.saveAllAndFlush(listSave);
		bancosSearch = listOfBancos.get(0);
		
		bancos = Instancio.of(Bancos.class)
				.set(field(Bancos::getCodigoPunto), punto.getCodigoPunto())
				.create();
		
		log.info("setup - id: {}", punto.getCodigoPunto());
	}

    @Test
    void testFindBancoByCodigoPunto() {
      Bancos bancoFind = bancosRepository.findBancoByCodigoPunto(bancosSearch.getCodigoPunto());
      assertThat(bancoFind).isNotNull();
      log.info("testFindBancoByCodigoPunto - id: {}", bancoFind.getCodigoPunto());
    }

    @Test
    void testFindBancoByAbreviatura() {
      Bancos bancoFind = bancosRepository.findBancoByAbreviatura(bancosSearch.getAbreviatura());
      assertThat(bancoFind).isNotNull();
      log.info("testFindBancoByAbreviatura - id: {}", bancoFind.getAbreviatura());
    }

    @Test
    void testFindByCodigoCompensacion() {
      Bancos bancoFind =
          bancosRepository.findByCodigoCompensacion(bancosSearch.getCodigoCompensacion());
      assertThat(bancoFind).isNotNull();
      log.info("testFindByCodigoCompensacion - id: {}", bancoFind.getCodigoCompensacion());
    }

    @Test
    void testFindByCodigoPunto() {
      Bancos bancoFind = bancosRepository.findByCodigoPunto(bancosSearch.getCodigoPunto());
      assertThat(bancoFind).isNotNull();
      log.info("testFindByCodigoPunto - id: {}", bancoFind.getCodigoPunto());
    }

    @Test
    void testFindByEsAVAL() {
      List<Bancos> bancosFind = bancosRepository.findByEsAVAL(bancosSearch.getEsAVAL());
      assertThat(bancosFind).isNotEmpty();
      log.info("testFindByEsAVAL - size: {}", bancosFind.size());
    }

	@Test
	void testIBancosSave() {
		Bancos bancosSaved = bancosRepository.save(bancos);
		
		assertThat(bancosSaved).isNotNull();
		log.info("testIBancosSave - id: {}", bancosSaved.getCodigoPunto());
	}

	@Test
	void testIBancosFindById() {
		Optional<Bancos> bancosFind = bancosRepository.findById(bancosSearch.getCodigoPunto());
		
		assertThat(bancosFind).isNotEmpty();
		bancosFind.ifPresent(punto -> {
			assertThat(punto.getCodigoPunto()).isNotNull();
			assertThat(punto.getEsAVAL()).isInstanceOf(Boolean.class);
		});
		log.info("testIBancosFindById - id: {}", bancosSearch.getCodigoPunto());
	}

	@Test
	@Disabled
	void testIBancosFindAll() {
		List<Bancos> bancosFind = bancosRepository.findAll();
		
		assertThat(bancosFind).isNotEmpty().hasSize(listOfBancos.size());
		log.info("testIBancosFindAll - size: {}", bancosFind.size());
	}

	@Test
	void testIBancosDelete() {
		bancosRepository.delete(bancosSearch);
		Optional<Bancos> bancosFind = bancosRepository.findById(bancosSearch.getCodigoPunto());
		
		assertThat(bancosFind).isEmpty();
		log.info("testIBancosDelete - id: {}", bancosSearch.getCodigoPunto());
	}

}
