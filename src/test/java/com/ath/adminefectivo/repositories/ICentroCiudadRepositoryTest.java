package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CentroCiudad;
import com.ath.adminefectivo.entities.Ciudades;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class ICentroCiudadRepositoryTest {

	@Autowired
	private ICentroCiudadRepository centroCiudadRepository;
	@Autowired
	private IBancosRepository bancosRepository;
	@Autowired
	private ICiudadesRepository ciudadesRepository;
	private CentroCiudad centroCiudad;
	private List<CentroCiudad> listOfCentroCiudad;
	
	@BeforeEach
	void setUp() throws Exception {
		
		Bancos banco = Instancio.of(Bancos.class)
				.set(field(Bancos::getPuntos), null)
				.create();
		this.bancosRepository.saveAndFlush(banco);
		
		Ciudades ciudad = Instancio.of(Ciudades.class).create();
		this.ciudadesRepository.saveAndFlush(ciudad);
		
		List<CentroCiudad> listSave = Instancio.ofList(CentroCiudad.class).size(11)
				.set(field(CentroCiudad::getBancoAval), banco)
				.set(field(CentroCiudad::getCiudadDane), ciudad)
				.create();
		this.listOfCentroCiudad = this.centroCiudadRepository.saveAllAndFlush(listSave);
		this.centroCiudad = this.listOfCentroCiudad.get(0);
		
		log.info("setup - id: {}", this.centroCiudad.getIdCentroCiudad());
	}
	
	@Test
	void testICentroCiudadFindAll() {
		List<CentroCiudad> centroCiudadFind = this.centroCiudadRepository.findAll();
		assertThat(centroCiudadFind).isNotEmpty().hasSize(this.listOfCentroCiudad.size());
		
		log.info("testICentroCiudadFindAll size: {}", centroCiudadFind.size());
	}

	@Test
	void testICentroCiudadSave() {
		assertThat("Not yet implemented");
	}

	@Test
	void testICentroCiudadFindById() {
		assertThat("Not yet implemented");
	}

	@Test
	void testICentroCiudadDelete() {
		assertThat("Not yet implemented");
	}

}
