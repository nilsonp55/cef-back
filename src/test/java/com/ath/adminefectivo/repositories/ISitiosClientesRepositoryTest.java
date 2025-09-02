package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import java.util.List;
import java.util.stream.Collectors;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.ath.adminefectivo.entities.ClientesCorporativos;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.SitiosClientes;
import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class ISitiosClientesRepositoryTest {

  @Autowired
  private ISitiosClientesRepository sitiosClientesRepository;
  @Autowired
  private IPuntosRepository puntosRepository;
  @Autowired
  private IClientesCorporativosRepository clientesCorporativos;
  
  private SitiosClientes sitiosClientes;
  private List<SitiosClientes> listOfSitiosClientes;
  private SitiosClientes searchSitiosClientes;

  @BeforeEach
  void setUp() throws Exception {

    List<Puntos> puntosSaved = this.puntosRepository.saveAllAndFlush(Instancio.ofList(Puntos.class)
        .size(11).set(field(Puntos::getCodigoPunto), null).set(field(Puntos::getBancos), null)
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
        .create());
    
    ClientesCorporativos cliente = clientesCorporativos.save(Instancio.of(ClientesCorporativos.class).create());

    List<SitiosClientes> sitiosClientesSaved = puntosSaved.stream()
        .map(punto -> Instancio.of(SitiosClientes.class)
            .set(field(SitiosClientes::getCodigoPunto), punto.getCodigoPunto())
            .set(field(SitiosClientes::getPunto), null)
            .set(field(SitiosClientes::getCodigoCliente), cliente)
            .create())
        .collect(Collectors.toList());
    
    this.listOfSitiosClientes = this.sitiosClientesRepository.saveAllAndFlush(sitiosClientesSaved);
    this.searchSitiosClientes = this.listOfSitiosClientes.get(0);

    this.sitiosClientes = Instancio.of(SitiosClientes.class)
        .set(field(SitiosClientes::getCodigoPunto), puntosSaved.get(0).getCodigoPunto())
        .set(field(SitiosClientes::getCodigoCliente), cliente)
        .create();

    log.info("setup - size: {}", this.listOfSitiosClientes.size());
  }
  
  @Test
  void testFindAll() {
    List<SitiosClientes> sitiosClientesFound = this.sitiosClientesRepository.findAll();
    assertThat(sitiosClientesFound).isNotEmpty().hasSize(this.listOfSitiosClientes.size());

    log.info("testFindAll - size: {}", sitiosClientesFound.size());
  }

  @Test
  void testFindByCodigoPunto() {
    SitiosClientes sitiosClientesFound =
        this.sitiosClientesRepository.findByCodigoPunto(this.searchSitiosClientes.getCodigoPunto());
    assertThat(sitiosClientesFound.getCodigoPunto()).isNotNull()
        .isEqualTo(this.searchSitiosClientes.getCodigoPunto());

    log.info("testFindByCodigoPunto find Id: {}", sitiosClientesFound.getCodigoPunto());
  }

  @Test
  void testCreateSitiosClientes() {
    SitiosClientes sitiosClienteSaved = this.sitiosClientesRepository.save(this.sitiosClientes);
    assertThat(sitiosClienteSaved).isNotNull();
    assertThat(sitiosClienteSaved.getCodigoPunto()).isEqualTo(this.sitiosClientes.getCodigoPunto());
    assertThat(sitiosClienteSaved.getCodigoCliente()).isEqualTo(this.sitiosClientes.getCodigoCliente());
    assertThat(sitiosClienteSaved.getFajado()).isEqualTo(this.sitiosClientes.getFajado());
    
    log.info("testCreateSitiosClientes - Id {}", sitiosClienteSaved.getCodigoPunto());
  }
}
