package com.ath.adminefectivo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import com.ath.adminefectivo.dto.SitiosClientesDTO;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.repositories.ISitiosClientesRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class SitiosClientesServiceImplTest {

  @Mock
  private ISitiosClientesRepository sitiosClientesRepository;
  
  @InjectMocks
  private SitiosClientesServiceImpl sitiosClientesService;
  
  private List<SitiosClientes> listOfSitiosClientes; 
  
  @BeforeEach
  void setUp() throws Exception {
    
    List<Puntos> puntos = Instancio.ofList(Puntos.class)
        .size(11).set(field(Puntos::getBancos), null)
        .set(field(Puntos::getCajeroATM), null).set(field(Puntos::getFondos), null)
        .set(field(Puntos::getOficinas), null).set(field(Puntos::getSitiosClientes), null)
        .set(field(Puntos::getPuntosCodigoTDV), null)
        .generate(field(Puntos::getTipoPunto),
            gen -> gen.oneOf("BANCO", "BAN_REP", "CAJERO", "CLIENTE", "FONDO", "OFICINA", null))
        .generate(field(Puntos::getCodigoCiudad),
            gen -> gen.oneOf("8001", "11001", "76001", "17001", "52001", null))
        .generate(field(Puntos::getNombrePunto),
            gen -> gen.oneOf("9099|85VIRREY SOLIS  UAB PALMIRA", "CB CELTEL 20 DE JULIO",
                "BANCO REPUBLICA-BARRANQUILLA", "CENTRO COMERCIAL BUENAVISTA 1",
                "BPOP-VALLEDUPAR-PROSEGUR", "CB CELTEL 20 DE JULIO",
                "9099|85VIRREY SOLIS  UAB PALMIRA", null))
        .create();

    this.listOfSitiosClientes = puntos.stream()
        .map(punto -> Instancio.of(SitiosClientes.class)
            .set(field(SitiosClientes::getCodigoPunto), punto.getCodigoPunto())
            .set(field(SitiosClientes::getPuntos), punto).create())
        .collect(Collectors.toList());
    
    
  }
  
  @Test
  void testGetSitiosClientes() {
    given(this.sitiosClientesRepository.findAll(new BooleanBuilder())).willReturn(this.listOfSitiosClientes);
    List<SitiosClientesDTO> resultGetSitiosClientes =
        this.sitiosClientesService.getSitiosClientes(new BooleanBuilder());

    assertThat(resultGetSitiosClientes).isNotNull().hasSize(this.listOfSitiosClientes.size());
    SitiosClientes entity = this.listOfSitiosClientes.get(0);
    SitiosClientesDTO sitiosClientesDto = resultGetSitiosClientes.stream()
        .filter(dtoSitios -> dtoSitios.getCodigoPunto().equals(entity.getCodigoPunto()))
        .findAny().orElseGet(null);
     
    assertThat(sitiosClientesDto.getCodigoCliente()).isEqualTo(entity.getCodigoCliente());
    assertThat(sitiosClientesDto.getFajado()).isEqualTo(entity.getFajado());
         
    log.info("testGetSitiosClientes - size: {}", resultGetSitiosClientes.size());
  }
  
  @Test
  void testGetCodigoPuntoSitio() {
    SitiosClientes entity = this.listOfSitiosClientes.get(0);
    given(this.sitiosClientesRepository.findByCodigoPunto(anyInt())).willReturn(entity);
    SitiosClientes sitioClienteFound = this.sitiosClientesService.getCodigoPuntoSitio(anyInt());
    assertThat(sitioClienteFound).isNotNull().isEqualTo(entity);

    log.info("testGetCodigoPuntoSitio - Id: {}", sitioClienteFound.getCodigoPunto());
  }

  @Test
  void testCreateSitioCliente() {
    given(this.sitiosClientesRepository.save(any())).willReturn(listOfSitiosClientes.get(0));
    SitiosClientesDTO sitioClienteSaved =
        this.sitiosClientesService.createSitioCliente(new SitiosClientesDTO());
    assertThat(sitioClienteSaved).isNotNull();
    log.info("testCreateSitioCliente - Id: {}", sitioClienteSaved.getCodigoPunto());
  }
  
  @Test
  void testCreateSitioClienteException() {
    var throwedExceptionData = new DataIntegrityViolationException(
        "test Created DataIntegrityException", new SQLException("test Created SQLException"));
    
    given(this.sitiosClientesRepository.save(any())).willThrow(throwedExceptionData);
    Exception exception = assertThrows(DataAccessException.class, () -> {
      this.sitiosClientesService.createSitioCliente(new SitiosClientesDTO());
    });
    assertThat(exception.getMessage()).contains(throwedExceptionData.getMessage());
    assertThat(exception.getCause().getMessage())
        .contains(throwedExceptionData.getCause().getMessage());

    log.info("testCreateSitioClienteException - msg Exception: {}", exception.getMessage());
  }
  
  @Test
  void testUpdateSitioCliente() {
    given(this.sitiosClientesRepository.findById(any()))
        .willReturn(Optional.of(listOfSitiosClientes.get(0)));
    given(this.sitiosClientesRepository.save(any())).willReturn(listOfSitiosClientes.get(0));
    SitiosClientesDTO sitioClienteUpdated = this.sitiosClientesService
        .updateSitioCliente(SitiosClientesDTO.CONVERTER_DTO.apply(listOfSitiosClientes.get(0)));
    assertThat(sitioClienteUpdated).isNotNull();
    log.info("testUpdateSitioCliente - Id: {}", sitioClienteUpdated.getCodigoPunto());
  }
  
  @Test
  void testUpdateSitioClienteException() {
    SitiosClientes entity = listOfSitiosClientes.get(0);
    SitiosClientesDTO dto = SitiosClientesDTO.CONVERTER_DTO.apply(entity);
    var throwedExceptionData = new DataIntegrityViolationException(
        "test Updated DataIntegrityException", new SQLException("test Updated SQLException"));
    
    given(this.sitiosClientesRepository.findById(any())).willReturn(Optional.of(entity));
    given(this.sitiosClientesRepository.save(any())).willThrow(throwedExceptionData);
    
    Exception exception = assertThrows(DataAccessException.class, () -> {
      this.sitiosClientesService.updateSitioCliente(dto);
    });

    assertThat(exception.getMessage()).contains(throwedExceptionData.getMessage());
    assertThat(exception.getCause().getMessage())
        .contains(throwedExceptionData.getCause().getMessage());

    log.info("testUpdateSitioClienteException - msg Exception: {}", exception.getMessage());
  }

  @Test
  void testDeteleSitioCliente() {
    doNothing().when(this.sitiosClientesRepository).deleteById(any());
    this.sitiosClientesService.deteleSitioCliente(0);

    log.info("testDeteleSitioCliente");
  }

  @Test
  void testDeleteSitioClienteException() {
    var throwedExceptionData = new DataIntegrityViolationException(
        "test deleted DataIntegrityException", new SQLException("test deleted SQLException"));
    doThrow(throwedExceptionData).when(this.sitiosClientesRepository).deleteById(any());
    Exception exception = assertThrows(DataAccessException.class, () -> {
      this.sitiosClientesService.deteleSitioCliente(0);
    });

    assertThat(exception.getMessage()).contains(throwedExceptionData.getMessage());
    assertThat(exception.getCause().getMessage())
        .contains(throwedExceptionData.getCause().getMessage());

    log.info("testDeleteSitioClienteException");
  }

}
