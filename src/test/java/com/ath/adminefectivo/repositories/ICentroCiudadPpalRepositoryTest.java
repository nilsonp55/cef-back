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
import com.ath.adminefectivo.entities.CentroCiudadPpal;
import com.ath.adminefectivo.entities.Ciudades;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
public class ICentroCiudadPpalRepositoryTest {
	
	@Autowired
	private ICentroCiudadPpalRepository centroCiudadPpalRepository;
	@Autowired
	private IBancosRepository bancosRepository;
	@Autowired
	private ICiudadesRepository ciudadesRepository;
	private CentroCiudadPpal centroCiudadPpal;
	private List<CentroCiudadPpal> listOfCentroCiudadPpal;
	
	@BeforeEach
	void setUp() throws Exception {
		
		Bancos banco = Instancio.of(Bancos.class)
				.create();
		this.bancosRepository.saveAndFlush(banco);
		
		Ciudades ciudad = Instancio.of(Ciudades.class).create();
		this.ciudadesRepository.saveAndFlush(ciudad);
				
		List<CentroCiudadPpal> listSave = Instancio.ofList(CentroCiudadPpal.class).size(11)
				.set(field(CentroCiudadPpal::getBancoAval), banco)
				.set(field(CentroCiudadPpal::getCodigoDane), ciudad)
				.create(); 
		this.listOfCentroCiudadPpal = this.centroCiudadPpalRepository.saveAllAndFlush(listSave);
		
		this.centroCiudadPpal = this.listOfCentroCiudadPpal.get(0);
		log.info("setup id: {}", this.centroCiudadPpal.getIdCentroCiudadPpal());
	}

	@Test
	void testICentroCiudadPpalFindAll() {
		
		List<CentroCiudadPpal> centroCiudadFind = this.centroCiudadPpalRepository.findAll();
		assertThat(centroCiudadFind).isNotEmpty().hasSize(this.listOfCentroCiudadPpal.size());
		
		log.info("testICentroCiudadPpalFindAll size: {}", centroCiudadFind.size());
	}
}
