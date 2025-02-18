package com.ath.adminefectivo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ath.adminefectivo.dto.CentroCiudadDTO;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CentroCiudadPpal;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ICentroCiudadPpalRepository;
import com.querydsl.core.BooleanBuilder;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(MockitoExtension.class)
class CentroCiudadPpalServiceImplTest {
	
	@InjectMocks
	private CentroCiudadPpalServiceImpl centroCiudadPpalService;
	@Mock
	private ICentroCiudadPpalRepository centroCiudadPpalRepository;

	private List<CentroCiudadPpal> listOfCentroCiudadPpal;
	
	@BeforeEach
	void setUp() throws Exception {
		
		Bancos banco = Instancio.of(Bancos.class)
				.set(field(Bancos::getPuntos), null)
				.create();
		
		Ciudades ciudad = Instancio.of(Ciudades.class).create();
		
		this.listOfCentroCiudadPpal = Instancio.ofList(CentroCiudadPpal.class).size(11)
				.set(field(CentroCiudadPpal::getBancoAval), banco)
				.set(field(CentroCiudadPpal::getCodigoDane), ciudad)
				.create();
		
		log.info("setup size: {}", this.listOfCentroCiudadPpal.size());
	}
	
	@Test
	void testListCentroCiudad() {
		BooleanBuilder builder = new BooleanBuilder();
		given(centroCiudadPpalRepository.findAll(builder)).willReturn(this.listOfCentroCiudadPpal);
		
		List<CentroCiudadDTO> listDTO = centroCiudadPpalService.listCentroCiudad(builder);
		
		assertThat(listDTO).isNotEmpty().hasSize(this.listOfCentroCiudadPpal.size());
		log.info("testListCentroCiudad - size : {}", listDTO.size());
	}

	@Test
	void testListCentroCiudadException() {
		BooleanBuilder builder = new BooleanBuilder();
		given(centroCiudadPpalRepository.findAll(builder)).willThrow(new RuntimeException("Test exception"));

		Exception exception = assertThrows(NegocioException.class, () -> {
			centroCiudadPpalService.listCentroCiudad(builder);
		});

		String expectedMessage = "Error al listar los centros ciudad principal";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
		log.info("testListCentroCiudadException - exception message : {}", actualMessage);
	}

	@Test
	void testCreateCentroCiudad() {

		given(centroCiudadPpalRepository.save(any())).willReturn(this.listOfCentroCiudadPpal.get(0));

		CentroCiudadDTO createdCentroCiudadDTO = centroCiudadPpalService
				.create(CentroCiudadDTO.CONVERTER_DTO_PPAL.apply(this.listOfCentroCiudadPpal.get(0)));

		assertNotNull(createdCentroCiudadDTO);
		log.info("testCreateCentroCiudad - created : {}", createdCentroCiudadDTO);
	}

	@Test
	void testCreateCentroCiudadException() {
		CentroCiudadDTO centroCiudadDTO = new CentroCiudadDTO();
		given(centroCiudadPpalRepository.save(any())).willThrow(new RuntimeException("Test exception"));

		Exception exception = assertThrows(NegocioException.class, () -> {
			centroCiudadPpalService.create(centroCiudadDTO);
		});

		String expectedMessage = "Error al crear el centro ciudad principal";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
		log.info("testCreateCentroCiudadException - exception message : {}", actualMessage);
	}
}
