package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.dto.compuestos.ConteoContabilidadDTO;
import com.ath.adminefectivo.entities.TransaccionesContables;
import com.ath.adminefectivo.repositories.ITransaccionesContablesRepository;
import com.ath.adminefectivo.service.ITransaccionesContablesService;

import lombok.extern.log4j.Log4j2;

/**
 * Implementacion de Servicios para gestionar las transacciones contables
 * 
 * @author duvan.naranjo
 */

@Service
@Log4j2
public class TransaccionesContablesServiceImpl implements ITransaccionesContablesService {

	@Autowired
	ITransaccionesContablesRepository transaccionesContablesRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransaccionesContables> getAllTransaccionesContables() {
		return new ArrayList<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransaccionesContables getTransaccionesContablesById(String idTransaccionesContables) {
		return new TransaccionesContables();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransaccionesContables saveTransaccionesContablesById(TransaccionesContablesDTO transaccionesContablesDTO) {
		return transaccionesContablesRepository
				.save(TransaccionesContablesDTO.CONVERTER_ENTITY.apply(transaccionesContablesDTO));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTransaccionesContablesById(String idTransaccionesContables) {
		log.info("Not implement deleteTransaccionesContablesById: {}", idTransaccionesContables);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransaccionesContablesDTO> getTransaccionesContablesByFechas(Date fechaInicio, Date fechaFin) {
		List<TransaccionesContablesDTO> listadoTransaccionesContablesDTO = new ArrayList<>();

		List<TransaccionesContables> listadoTransaccionesContables = transaccionesContablesRepository
				.findByFechaBetween(fechaInicio, fechaFin);
		listadoTransaccionesContables.forEach(transaccionContable -> listadoTransaccionesContablesDTO
				.add(TransaccionesContablesDTO.CONVERTER_DTO.apply(transaccionContable))
			);

		return listadoTransaccionesContablesDTO;
	}

	@Override
	public String findBytipoProceso(String str) {
		return transaccionesContablesRepository.findBytipoProceso(str);
	}
	
	@Override
	public List<RespuestaContableDTO> getCierreContable(Date fecha, String tipoContabilidad,
			int codBanco) {
		
		List<RespuestaContableDTO> listadoTransaccionContables = null;
			if(codBanco == 0) {
				listadoTransaccionContables = transaccionesContablesRepository
						.cierreContableAllBancos(fecha,tipoContabilidad, 1);
				listadoTransaccionContables.forEach(transaccion ->
					transaccion.setFechaConversion(null)
				);
			}else if(codBanco > 0)
			{
				listadoTransaccionContables = transaccionesContablesRepository
						.cierreContablebyBanco(fecha,tipoContabilidad,codBanco,1 );
				listadoTransaccionContables.forEach(transaccion ->
					transaccion.setFechaConversion(null)
				);
			}

		return listadoTransaccionContables;
	}
	
	@Override
	public List<TransaccionesContablesDTO> getTransaccionesContablesByNaturaleza(String naturaleza) {
		List<TransaccionesContablesDTO> listadoTransaccionesContablesDTO = new ArrayList<>();

		List<TransaccionesContables> listadoTipoTransaccion = transaccionesContablesRepository
				.findByNaturaleza(naturaleza);
		listadoTipoTransaccion.forEach(transaccionContable -> listadoTransaccionesContablesDTO
				.add(TransaccionesContablesDTO.CONVERTER_DTO.apply(transaccionContable))
			);

		return listadoTransaccionesContablesDTO;
		
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConteoContabilidadDTO generarConteoContabilidad(Date fechaProceso, String tipoContabilidad) {
		return transaccionesContablesRepository.conteoContabilidad(fechaProceso, fechaProceso, tipoContabilidad);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTransaccionesContablesByFechasAndTipoProceso(Date fechaInicio, Date fechaFin, String tipoProceso) {
		List<TransaccionesContables> listadoTransaccionesContables = transaccionesContablesRepository
				.findByFechaBetweenAndTipoProceso(fechaInicio, fechaFin, tipoProceso);
		listadoTransaccionesContables.forEach(transaccionContable ->
			transaccionesContablesRepository.delete(transaccionContable)
		);		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String generarComprobanteContable(Date fecha, String tipoContabilidad) {
		return transaccionesContablesRepository.generarcomprobantecontable(fecha, tipoContabilidad);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> cierreContablebyBancoF1String(Date fecha, String tipoContabilidad,
			int codBanco){
		return transaccionesContablesRepository.cierreContablebyBancoF1String(fecha, tipoContabilidad, codBanco, 1);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> cierreContablebyBancoF2String(Date fecha, String tipoContabilidad,
			int codBanco){
		log.debug("tipoContabilidad: {} - codBanco: {}", tipoContabilidad, codBanco);
		return transaccionesContablesRepository.cierreContablebyBancoF2String(fecha, tipoContabilidad, codBanco, 1);
		
	}
	
}
