package com.ath.adminefectivo.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.DetallesProcesoAutomatico;

import lombok.extern.log4j.Log4j2;

@Log4j2
@DataJpaTest
class DetallesProcesoAutomaticoRepositoryTest {
	
	@Autowired
	private DetallesProcesoAutomaticoRepository detallesProcesoAutomaticoRepository;
	
	private DetallesProcesoAutomatico detallesProcesoAutomatico;
	
	@BeforeEach
	void setup() {
		
		detallesProcesoAutomatico = DetallesProcesoAutomatico.builder()
			.idArchivo(1)
			.mensajeError("Error en archivo")
			.nombreArchivo(Date.from(Instant.now()))
			.resultado("Proceso exitoso")
			.build();
		log.info("setup: ", detallesProcesoAutomatico);
		
	}
	
	@Test
	void testDetalleProcesoAutomatico() {
		
		// validar metodo save del repository
		DetallesProcesoAutomatico detallesProcesoAutomaticoSaved = detallesProcesoAutomaticoRepository.save(detallesProcesoAutomatico);
		assertThat(detallesProcesoAutomaticoSaved).isNotNull();
		assertThat(detallesProcesoAutomaticoSaved.getIdArchivo()).isPositive();
		log.info("Repository save: ", detallesProcesoAutomaticoSaved.getIdDetalleProceso());
		
		// validar save cuando es Update object
		String mensajeError = "Segundo mensaje Error";
		Date nombreArchivoDate = Date.from(Instant.now());
		String resultado = "Proceso esitoso segundo";
		long idArchivo = 876L;
		detallesProcesoAutomaticoSaved.setMensajeError(mensajeError);
		detallesProcesoAutomaticoSaved.setNombreArchivo(nombreArchivoDate);
		detallesProcesoAutomaticoSaved.setResultado(resultado);
		detallesProcesoAutomaticoSaved.setIdArchivo(idArchivo);
		
		DetallesProcesoAutomatico validarDetallesProcesoAutomatico = detallesProcesoAutomaticoRepository.save(detallesProcesoAutomaticoSaved);
		
		assertThat(validarDetallesProcesoAutomatico.getIdDetalleProceso()).isEqualTo(detallesProcesoAutomatico.getIdDetalleProceso());
		assertThat(validarDetallesProcesoAutomatico.getIdArchivo()).isEqualTo(idArchivo);
		assertThat(validarDetallesProcesoAutomatico.getResultado()).isEqualTo(resultado);
		assertThat(validarDetallesProcesoAutomatico.getNombreArchivo()).isEqualTo(nombreArchivoDate);
		assertThat(validarDetallesProcesoAutomatico.getMensajeError()).isEqualTo(mensajeError);
		log.info("Repository save - Update object:", validarDetallesProcesoAutomatico.getIdDetalleProceso());
		
		// validar metodo FindAll
		DetallesProcesoAutomatico detallesProcesoAutomatico2 = DetallesProcesoAutomatico.builder()
				.idArchivo(3)
				.mensajeError("Mensaje en archivo")
				.nombreArchivo(Date.from(Instant.now()))
				.resultado("Proceso exitoso tercero")
				.build();
		detallesProcesoAutomaticoRepository.save(detallesProcesoAutomatico2);
		
		List<DetallesProcesoAutomatico> listDetallesProcesoAutomatico = detallesProcesoAutomaticoRepository.findAll();
		assertThat(listDetallesProcesoAutomatico).isNotEmpty().hasSize(2);
		
		// Validar metodo FindById
		long idDetalleProcesoAutomaticoFind = detallesProcesoAutomatico2.getIdDetalleProceso();
		Optional<DetallesProcesoAutomatico> detalleProcesoAutomaticoFind = detallesProcesoAutomaticoRepository.findById(idDetalleProcesoAutomaticoFind);
		assertThat(detalleProcesoAutomaticoFind).isNotEmpty();
		assertThat(detalleProcesoAutomaticoFind.get()).isInstanceOf(DetallesProcesoAutomatico.class);
		
	}

}
