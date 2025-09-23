package com.ath.adminefectivo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import java.sql.SQLException;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import com.ath.adminefectivo.dto.CentroCiudadDTO;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CentroCiudad;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.repositories.ICentroCiudadRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class CentroCiudadServiceImplTest {

  @InjectMocks
  private CentroCiudadServiceImpl centroCiudadService;

  @Mock
  private ICentroCiudadRepository centroCiudadRepository;

  private List<CentroCiudad> listOfCentroCiudad;

  @BeforeEach
  void setUp() throws Exception {

    Bancos banco = Instancio.of(Bancos.class).create();
    Ciudades ciudad = Instancio.of(Ciudades.class).create();

    this.listOfCentroCiudad = Instancio.ofList(CentroCiudad.class).size(11)
        .set(field(CentroCiudad::getBancoAval), banco)
        .set(field(CentroCiudad::getCiudadDane), ciudad).create();

    log.info("setup size: {}", this.listOfCentroCiudad.size());
  }
  
  @Test
  void testGetCentrosCiudades() {
    given(this.centroCiudadRepository.findAll(new BooleanBuilder()))
        .willReturn(this.listOfCentroCiudad);
    List<CentroCiudadDTO> returnList =
        this.centroCiudadService.getCentrosCiudades(new BooleanBuilder());
    assertThat(returnList).isNotEmpty().hasSize(this.listOfCentroCiudad.size());

    log.info("testGetCentrosCiudades size: {}", returnList.size());
  }
  
  @Test
  void testDeleteCentroCiudad() {
    doNothing().when(centroCiudadRepository).deleteById(anyInt());
    this.centroCiudadService.deleteCentroCiudad(anyInt());
    
    log.info("testDeleteCentroCiudad");
  }
  
  @Test
  void testDeleteCentroCiudadException() {
    doThrow(new DataIntegrityViolationException("test deleted DataIntegrityException",
        new SQLException("SQL Exception test DataIntegrityException"))).when(centroCiudadRepository)
            .deleteById(anyInt());
    Exception exception = assertThrows(RuntimeException.class, () -> {
      this.centroCiudadService.deleteCentroCiudad(anyInt());
    });
    assertNotNull(exception.getMessage());

    log.info("testDeleteCentroCiudadException: msg exception: {}", exception.getMessage());
  }

}
