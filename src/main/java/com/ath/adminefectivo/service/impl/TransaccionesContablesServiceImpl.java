package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;
import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.ath.adminefectivo.entities.TransaccionesContables;
import com.ath.adminefectivo.entities.TransaccionesInternas;
import com.ath.adminefectivo.repositories.ITransaccionesContablesRepository;
import com.ath.adminefectivo.repositories.ITransaccionesInternasRepository;
import com.ath.adminefectivo.service.ITransaccionesContablesService;
import com.ath.adminefectivo.service.ITransaccionesInternasService;

/**
 * Implementacion de Servicios para gestionar las transacciones contables
 * 
 * @author duvan.naranjo
 */

@Service
public class TransaccionesContablesServiceImpl implements ITransaccionesContablesService {

	@Autowired
	ITransaccionesContablesRepository transaccionesContablesRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TransaccionesContables> getAllTransaccionesContables() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransaccionesContables getTransaccionesContablesById(String idTransaccionesContables) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub

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
		String tipoProceso = transaccionesContablesRepository.findBytipoProceso(str);
		return tipoProceso;
	}
	
	@Override
	public List<RespuestaContableDTO> getCierreContable(Date fecha, String tipoContabilidad, String numeroBancos,
			String codBanco) {
		List<TransaccionesContablesDTO> listadoTransaccionesCierreDTO = new ArrayList<>();
		
		List<RespuestaContableDTO> listadoTransaccionContables = null;
			if(numeroBancos.equals("TODOS")) {
				listadoTransaccionContables = transaccionesContablesRepository
						.cierreContableAllBancos(fecha,tipoContabilidad);
			}else if(numeroBancos.equals("UNO"))
			{
				listadoTransaccionContables = transaccionesContablesRepository
						.cierreContablebyBanco(fecha,tipoContabilidad,codBanco);
			}

		return listadoTransaccionContables;
	}
	
	@Override
	public List<TransaccionesContablesDTO> getTransaccionesContablesByNaturaleza(String Naturaleza) {
		List<TransaccionesContablesDTO> listadoTransaccionesContablesDTO = new ArrayList<>();

		List<TransaccionesContables> listadoTipoTransaccion = transaccionesContablesRepository
				.findByNaturaleza(Naturaleza);
		listadoTipoTransaccion.forEach(transaccionContable -> listadoTransaccionesContablesDTO
				.add(TransaccionesContablesDTO.CONVERTER_DTO.apply(transaccionContable))
			);

		return listadoTransaccionesContablesDTO;
		
	}
	
	public List<RespuestaContableDTO> existErroresContablesByBanco (Date fecha,String tipoContabilidad,String codBanco) {
		//aqui va el repository
		List<RespuestaContableDTO>  dato = transaccionesContablesRepository.erroresContablesbybanco(fecha, tipoContabilidad, codBanco);
		return dato;
	}
	
	public List<RespuestaContableDTO> existErroresContablesAllBanco (Date fecha,String tipoContabilidad) {
		//aqui va el repository
		List<RespuestaContableDTO> dato = transaccionesContablesRepository.erroresContablesAllbanco(fecha, tipoContabilidad);

		return dato;
		
	}
	
	public String estadovalidacionContable(String estado) {
		String estadoCierre = transaccionesContablesRepository.estadovalidacionContable(estado);
		return estadoCierre;
	}
	
}
