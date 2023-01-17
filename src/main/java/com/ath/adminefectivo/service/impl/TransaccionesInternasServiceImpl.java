package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.ath.adminefectivo.entities.TransaccionesInternas;
import com.ath.adminefectivo.repositories.ITransaccionesInternasRepository;
import com.ath.adminefectivo.service.IErroresContablesService;
import com.ath.adminefectivo.service.ITransaccionesInternasService;

/**
 * Servicios para gestionar las transacciones internas
 * 
 * @author Bayron Perez
 */

@Service
public class TransaccionesInternasServiceImpl implements ITransaccionesInternasService {

	@Autowired
	ITransaccionesInternasRepository transaccionesInternasRepository;
	
	@Autowired
	IErroresContablesService erroresContablesService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransaccionesInternas> getAllTransaccionesInternas() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransaccionesInternasDTO getTransaccionesInternasById(Long idTransaccionesInternas) {
		TransaccionesInternas transaccionInternaEntity = transaccionesInternasRepository.getById(idTransaccionesInternas);
		
		return TransaccionesInternasDTO.CONVERTER_DTO.apply(transaccionInternaEntity);
	}

	@Override
	public TransaccionesInternas saveTransaccionesInternasById(TransaccionesInternasDTO transaccionesInternasDTO) {
		System.out.println("/////// " + transaccionesInternasDTO);
		var x = TransaccionesInternasDTO.CONVERTER_ENTITY.apply(transaccionesInternasDTO);
		return transaccionesInternasRepository.save(x);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTransaccionesInternasById(String idTransaccionesInternas) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransaccionesInternasDTO> getTransaccionesInternasByFechas(Date fechaInicio, Date fechaFin) {
		List<TransaccionesInternasDTO> listadoTransaccionesInternasDTO = new ArrayList<>();

		List<TransaccionesInternas> listadoTransaccionesInternas = transaccionesInternasRepository
				.findByFechaBetween(fechaInicio, fechaFin);
		listadoTransaccionesInternas.forEach(transaccionInterna -> listadoTransaccionesInternasDTO
				.add(TransaccionesInternasDTO.CONVERTER_DTO.apply(transaccionInterna)));

		return listadoTransaccionesInternasDTO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean generarMovimientosContables(Date fechaInicio, Date fechaFin, String tipoContabilidad,
			int estadoContabilidadGenerado) {
		return transaccionesInternasRepository.fnc_transcciones_contables(fechaInicio, fechaFin,
				tipoContabilidad, estadoContabilidadGenerado);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTransaccionesInternasByFechasAndTipoProceso(Date fechaInicio, Date fechaFin, String tipoProceso) {
		List<TransaccionesInternas> listadoTransaccionesInternas = transaccionesInternasRepository
				.findByFechaBetweenAndTipoProceso(fechaInicio, fechaFin, tipoProceso);
		
		listadoTransaccionesInternas.forEach(transaccionInterna ->{
			erroresContablesService.eliminarErrorContableByIdTransaccionInterna(transaccionInterna.getIdTransaccionesInternas());
			transaccionesInternasRepository.delete(transaccionInterna);
			
		});
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existErroresContablesByBanco (Date fecha,String tipoContabilidad,int codBanco) {
		return transaccionesInternasRepository.existErroresContablesByBanco(fecha, tipoContabilidad, codBanco, 3);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existErroresContablesAllBanco (Date fecha,String tipoContabilidad) {
		return  transaccionesInternasRepository.existErroresContablesAllBanco(fecha, tipoContabilidad, 3);

		
	}

	@Override
	public void deleteTransaccionesInternasByFechas(Date fechaInicio, Date fechaFin) {
		List<TransaccionesInternas> listadoTransaccionesInternas = transaccionesInternasRepository
				.findByFechaBetween(fechaInicio, fechaFin);
		
		listadoTransaccionesInternas.forEach(transaccionInterna ->{
			transaccionesInternasRepository.delete(transaccionInterna);
		});
	}

}
