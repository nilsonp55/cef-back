package com.ath.adminefectivo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import java.util.Date;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ath.adminefectivo.dto.AuditoriaProcesosDTO;
import com.ath.adminefectivo.entities.AuditoriaProcesos;
import com.ath.adminefectivo.repositories.IAuditoriaProcesosRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(MockitoExtension.class)
class AuditoriaProcesosServiceTest {
  
  @Mock
  private IAuditoriaProcesosRepository auditoriaProcesosRepository;
  
  @InjectMocks
  private AuditoriaProcesosServiceImpl auditoriaProcesosService;
  
  List<AuditoriaProcesos> listAuditoriaProcesos;
  List<Date> listFechasProcesadas;
  
  @BeforeEach
  void setUp() throws Exception {
    
    this.listAuditoriaProcesos = Instancio.ofList(AuditoriaProcesos.class).create();
    
    this.listFechasProcesadas = Instancio.ofList(Date.class).create();
    
    log.info("setup - size: {}", listAuditoriaProcesos.size());
  }
  
  @Test
  void testConsultarFechasProcesadas() {
    given(auditoriaProcesosRepository.auditoriaProcesosFechasProcesadas()).willReturn(this.listFechasProcesadas);
    
    List<Date> listFechasProcesadas = auditoriaProcesosService.consultarFechasProcesadas();
    assertThat(listFechasProcesadas).isNotEmpty().hasSizeGreaterThan(0);
    
    log.info("testConsultarPorFechaProceso - size : {}", listFechasProcesadas.size());
  }
  
  @Test
  void testCrearAuditoriaProceso() {
    given(auditoriaProcesosRepository.save(new AuditoriaProcesos())).willReturn(this.listAuditoriaProcesos.get(0));
    
    AuditoriaProcesosDTO auditoriaProcesoDTO = auditoriaProcesosService.crearAuditoriaProceso(new AuditoriaProcesosDTO());
    assertThat(auditoriaProcesoDTO).isInstanceOf(AuditoriaProcesosDTO.class);
    
    log.info("testCrearAuditoriaProceso - id: {}", auditoriaProcesoDTO.getId().toString());
  }
  
  @Test
  void testConsultarPorFechaProceso() {
    given(auditoriaProcesosRepository.findByFechaProceso(null)).willReturn(listAuditoriaProcesos);

    List<AuditoriaProcesosDTO> resultAuditoriaProcesos =
        auditoriaProcesosService.consultarPorFechaProceso(null);
    assertThat(resultAuditoriaProcesos).isNotEmpty();

    log.info("testConsultarPorFechaProceso - size : {}", resultAuditoriaProcesos.size());
  }

}
