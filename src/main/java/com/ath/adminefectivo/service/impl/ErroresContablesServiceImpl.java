package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.ErroresContablesDTO;
import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.ath.adminefectivo.dto.compuestos.ResultadoErroresContablesDTO;
import com.ath.adminefectivo.entities.ErroresContables;
import com.ath.adminefectivo.repositories.IErroresContablesRepository;
import com.ath.adminefectivo.service.IErroresContablesService;
import com.ath.adminefectivo.service.ITransaccionesInternasService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ErroresContablesServiceImpl implements IErroresContablesService{
	
	@Autowired
	IErroresContablesRepository erroresContablesRepository;
	
	@Autowired
	ITransaccionesInternasService transaccionesInternasService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ErroresContablesDTO> consultarErroresContablesByFechaAndTipoProceso(Date fechaFin, String tipoProceso) {
		List<ErroresContables> listadoErroresContablesEntity = erroresContablesRepository.findByFechaBetweenAndTipoProceso(fechaFin, tipoProceso);
		List<ErroresContablesDTO> listadoErroresContablesDTO = new ArrayList<>();
		listadoErroresContablesEntity.forEach(errorContableEntity ->
			listadoErroresContablesDTO.add(ErroresContablesDTO.CONVERTER_DTO.apply(errorContableEntity))
		);
		
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
		List<TransaccionesInternasDTO> transaccionesInternasDTO = new ArrayList<>();
		erroresContablesRepository.fncGenerarProcesoContables().forEach(idTransaccion ->
			transaccionesInternasDTO.add(transaccionesInternasService.getTransaccionesInternasById(idTransaccion))
		);
		
		return transaccionesInternasDTO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void eliminarErrorContableByIdTransaccionInterna(long idTransaccionesInternas) {
		List<ErroresContables> erroresListado = erroresContablesRepository.findByTransaccionInterna(idTransaccionesInternas);
		erroresListado.forEach(errorContable -> {
			erroresContablesRepository.delete(errorContable);
			log.debug(errorContable.getMensajeError());
		});
		
	}

	

}
