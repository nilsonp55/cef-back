package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.ErroresContablesDTO;
import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.ath.adminefectivo.dto.compuestos.ResultadoErroresContablesDTO;
import com.ath.adminefectivo.entities.ErroresContables;
import com.ath.adminefectivo.entities.TransaccionesInternas;
import com.ath.adminefectivo.repositories.IErroresContablesRepository;
import com.ath.adminefectivo.service.IErroresContablesService;

@Service
public class ErroresContablesServiceImpl implements IErroresContablesService{
	
	@Autowired
	IErroresContablesRepository erroresContablesRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ErroresContablesDTO> consultarErroresContablesByFechas(Date fechaInicio, Date fechaFin) {
		List<ErroresContables> listadoErroresContablesEntity = erroresContablesRepository.findByFechaBetween(fechaInicio, fechaFin);
		List<ErroresContablesDTO> listadoErroresContablesDTO = new ArrayList<>();
		listadoErroresContablesEntity.forEach(errorContableEntity -> {
			listadoErroresContablesDTO.add(ErroresContablesDTO.CONVERTER_DTO.apply(errorContableEntity));
		});
		
		return listadoErroresContablesDTO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ResultadoErroresContablesDTO> consultarErroresContables() {
		return erroresContablesRepository.consultarErroresContables();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransaccionesInternasDTO> generarRespuestaProcesoContables() {
		List<TransaccionesInternas> transaccionesInterasEntity = erroresContablesRepository.fnc_generar_proceso_contables();
		List<TransaccionesInternasDTO> transaccionesInternasDTO = new ArrayList<>();
		transaccionesInterasEntity.forEach(transaccion ->{
			transaccionesInternasDTO.add(TransaccionesInternasDTO.CONVERTER_DTO.apply(transaccion));
		});
		
		return transaccionesInternasDTO;
	}

	

}
