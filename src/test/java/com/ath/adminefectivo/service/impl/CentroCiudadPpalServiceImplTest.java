package com.ath.adminefectivo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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

		Bancos banco = Instancio.of(Bancos.class).create();

		Ciudades ciudad = Instancio.of(Ciudades.class).create();

		this.listOfCentroCiudadPpal = Instancio.ofList(CentroCiudadPpal.class).size(11)
				.set(field(CentroCiudadPpal::getBancoAval), banco).set(field(CentroCiudadPpal::getCodigoDane), ciudad)
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
	void testListCentroCiudadDataAccessException() {
		BooleanBuilder builder = new BooleanBuilder();
		given(centroCiudadPpalRepository.findAll(builder)).willThrow(
				new NegocioException("", "Error al listar los centros ciudad principal", HttpStatus.BAD_REQUEST));

		Exception exception = assertThrows(NegocioException.class, () -> {
			centroCiudadPpalService.listCentroCiudad(builder);
		});

		String expectedMessage = "Error al listar los centros ciudad principal";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
		log.info("testListCentroCiudadDataAccessException - exception message : {}", actualMessage);
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
		doThrow(new NegocioException("", "Error al crear el centro ciudad principal", HttpStatus.BAD_REQUEST))
				.when(centroCiudadPpalRepository).save(any());

		Exception exception = assertThrows(NegocioException.class, () -> {
			centroCiudadPpalService
					.create(CentroCiudadDTO.CONVERTER_DTO_PPAL.apply(this.listOfCentroCiudadPpal.get(0)));
		});

		String expectedMessage = "Error al crear el centro ciudad principal";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
		log.info("testCreateCentroCiudadException - exception message : {}", actualMessage);
	}
	
	@Test
	void testUpdateCentroCiudad() {

		given(centroCiudadPpalRepository.findById(any())).willReturn(Optional.of(this.listOfCentroCiudadPpal.get(0)));
		given(centroCiudadPpalRepository.save(any())).willReturn(this.listOfCentroCiudadPpal.get(0));
		CentroCiudadDTO updatedCentroCiudadDTO = centroCiudadPpalService
				.update(CentroCiudadDTO.CONVERTER_DTO_PPAL.apply(this.listOfCentroCiudadPpal.get(0)));
		assertNotNull(updatedCentroCiudadDTO);
		log.info("testCreateCentroCiudad - created : {}", updatedCentroCiudadDTO);
	}
	
	@Test
	void testUpdateCentroCiudadFindByIdThrowException() {
		doThrow(new RuntimeException("Test")).when(centroCiudadPpalRepository).findById(anyInt());
		Exception exception = assertThrows(RuntimeException.class, () -> {
			centroCiudadPpalService
					.update(CentroCiudadDTO.CONVERTER_DTO_PPAL.apply(this.listOfCentroCiudadPpal.get(0)));
		});

		assertNotNull(exception.getMessage());
		log.info("testUpdateCentroCiudadFindByIdThrowDataAccessException - exceptioMessage : {}",
				exception.getMessage());
	}

	@Test
	void testDeleteCentroCiudad() {
		doNothing().when(centroCiudadPpalRepository).deleteById(anyInt());
		centroCiudadPpalService.delete(anyInt());
		log.info("testDeleteCentroCiudadDataAccessException - exception message : {}");
	}
	
	
}
