package com.ath.adminefectivo.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ath.adminefectivo.dto.UpdateEstadoOperacionesDTO;
import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.repositories.IOperacionesProgramadasRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ExtendWith(MockitoExtension.class)
public class ConciliacionOperacionesUpdateServiceTest {

	@Mock
	private IOperacionesProgramadasRepository operacionesProgramadasRepository;;

	@InjectMocks
	private ConciliacionOperacionesUpdateService conciliacionOperacionesUpdateService;

	private OperacionesProgramadas operacionProgramada;

	private List<UpdateEstadoOperacionesDTO> estadoProgramadaDTOList;

	@BeforeEach
	void setUp() throws Exception {

		operacionProgramada = Instancio.of(OperacionesProgramadas.class)
				.generate(field(OperacionesProgramadas::getEstadoConciliacion),
						gen -> gen.oneOf("CANCELADA", "NO_CONCILIADA", "FALLIDA", "POSPUESTA", "FUERA_DE_CONCILIACION"))
				.create();

		estadoProgramadaDTOList = Instancio.ofList(UpdateEstadoOperacionesDTO.class).size(12)
				.generate(field(UpdateEstadoOperacionesDTO::getEstado),
						gen -> gen.oneOf("POSPUESTA"))
				.set(field(UpdateEstadoOperacionesDTO::isUpdateExitoso), null)
				.set(field(UpdateEstadoOperacionesDTO::getResultadoFallido), null).create();

		log.info("Setup - operaciones programadas");
	}

	@Test
	void updateEstadoProgramadasTest() {
		log.info("inicia updateEstadoProgramadasTest");
		given(operacionesProgramadasRepository.findById(anyInt())).willReturn(Optional.of(operacionProgramada));
		given(operacionesProgramadasRepository.save(any())).willReturn(operacionProgramada);

		List<UpdateEstadoOperacionesDTO> resultadoUpdate = conciliacionOperacionesUpdateService.updateEstadoProgramadas(estadoProgramadaDTOList);
		
		assertThat(resultadoUpdate).isNotEmpty().hasSize(estadoProgramadaDTOList.size());
		
		resultadoUpdate.forEach(estadoProgramadaDTO -> {
			assertThat(estadoProgramadaDTO.isUpdateExitoso()).isTrue();
			assertThat(estadoProgramadaDTO.getResultadoFallido()).isNull();
		});
		log.info("Finaliza updateEstadoProgramadasTest");
	}
}
