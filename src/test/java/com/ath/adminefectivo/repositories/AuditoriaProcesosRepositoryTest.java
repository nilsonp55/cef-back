package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Date;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.ath.adminefectivo.entities.AuditoriaProcesos;
import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class AuditoriaProcesosRepositoryTest {

  @Autowired
  private IAuditoriaProcesosRepository auditoriaProcesosRepository;
  
  List<AuditoriaProcesos> listAuditoriaProcesos;
  
  @BeforeEach
  void setUp() throws Exception { 
    
    listAuditoriaProcesos = Instancio.ofList(AuditoriaProcesos.class).create();
    listAuditoriaProcesos = auditoriaProcesosRepository.saveAllAndFlush(listAuditoriaProcesos);
    
    log.info("setup - size: {}", listAuditoriaProcesos.size());
  }
  
  @Test
  void testAuditoriaProcesosFechasProcesadas() {

    List<Date> fechasProcesadas = auditoriaProcesosRepository.auditoriaProcesosFechasProcesadas();

    assertThat(fechasProcesadas).isNotEmpty();

    log.info("testAuditoriaProcesosFechasProcesadas - size result: {}", fechasProcesadas.size());
  }
  
  @Test
  void testFindByFechaProceso() {
    AuditoriaProcesos auditoriaProcesos = listAuditoriaProcesos.get(0);

    List<AuditoriaProcesos> findAuditoriaProcesos =
        auditoriaProcesosRepository.findByFechaProceso(auditoriaProcesos.getId().getFechaProceso());

    assertThat(findAuditoriaProcesos).isNotEmpty().hasSizeGreaterThan(0);

    log.info("testFindByFechaProceso - size result: {}", findAuditoriaProcesos.size());
  }
}
