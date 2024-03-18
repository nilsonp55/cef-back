package com.ath.adminefectivo.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ath.adminefectivo.entities.OperacionesCertificadas;

@DataJpaTest
@Disabled
class IOperacionesCertificadasRepositoryTest {
	
	@Autowired
	private IOperacionesCertificadasRepository operacionesCertificadasRepository;
	
	private OperacionesCertificadas operacionesCertificadas;

	@BeforeEach
	void setUp() throws Exception {
		
		operacionesCertificadas = OperacionesCertificadas.builder()
				.bancoAVAL(null)
				.codigoFondoTDV(null)
				.codigoOperacion(null)
				.codigoPropioTDV(null)
				.codigoPuntoCodigotdv(null)
				.codigoPuntoDestino(null)
				.codigoPuntoOrigen(null)
				.codigoServicioTdv(null)
				.conciliable(null)
				.consecutivoRegistro(null)
				.descripcionPuntoCodigotdv(null)
				.entradaSalida(null)
				.estadoConciliacion(null)
				.fallidaOficina(null)
				.fechaEjecucion(null)
				.idArchivoCargado(null)
				.idCertificacion(null)
				.moneda(null)
				.nombreFondoTDV(null)
				.nombrePuntoDestino(null)
				.nombrePuntoOrigen(null)
				.oficina(null)
				.tdv(null)
				.tipoOperacion(null)
				.tipoPuntoDestino(null)
				.tipoServicio(null)
				.valorFaltante(null)
				.valorSobrante(null)
				.valorTotal(null)
				.fechaCreacion(null)
				.fechaModificacion(null)
				.usuarioCreacion(null)
				.usuarioModificacion(null)
				.build();
	}

	@Test
	void testFindByEstadoConciliacionNotAndConciliable() {
		fail("Not yet implemented");
	}

	@Test
	void testCountByEstadoConciliacionAndFechaEjecucionBetween() {
		fail("Not yet implemented");
	}

	@Test
	void testCountByEstadoConciliacionAndFechaEjecucionBetweenAndConciliable() {
		fail("Not yet implemented");
	}

	@Test
	void testFindByCodigoFondoTDVAndEstadoConciliacion() {
		fail("Not yet implemented");
	}

	@Test
	void testFindByCodigoServicioTdv() {
		fail("Not yet implemented");
	}

	@Test
	void testFindByEstadoConciliacion() {
		fail("Not yet implemented");
	}

	@Test
	void testFindByCodigoPuntoOrigenAndCodigoServicioTdvAndEntradaSalidaAndFechaEjecucion() {
		fail("Not yet implemented");
	}

	@Test
	void testFindByCodigoFondoTDVAndCodigoPuntoOrigenAndCodigoPuntoDestinoAndCodigoServicioTdvAndEntradaSalidaAndFechaEjecucionAndCodigoPropioTDVAndIdArchivoCargado() {
		fail("Not yet implemented");
	}

	@Test
	void testConciliacionAutomatica() {
		fail("Not yet implemented");
	}

	@Test
	void testFindByCodigoPuntoDestinoAndEntradaSalidaAndFechaEjecucion() {
		fail("Not yet implemented");
	}

	@Test
	void testFindByCodigoPuntoOrigenAndEntradaSalidaAndFechaEjecucion() {
		fail("Not yet implemented");
	}

	@Test
	void testValidarnoconciliables() {
		fail("Not yet implemented");
	}

	@Test
	void testProcesarArchivosAlcance() {
		fail("Not yet implemented");
	}

	@Test
	void testSave() {
		fail("Not yet implemented");
	}

	@Test
	void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	void testFindAll() {
		fail("Not yet implemented");
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

}
