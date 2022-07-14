package com.ath.adminefectivo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.TransaccionesInternasDTO;
import com.ath.adminefectivo.entities.TransaccionesInternas;
import com.ath.adminefectivo.repositories.ITransaccionesInternasRepository;
import com.ath.adminefectivo.service.ITransaccionesInternasService;

/**
 * Servicios para gestionar las transacciones internas
 * @author Bayron Perez
 */

@Service
public class TransaccionesInternasServiceImpl implements ITransaccionesInternasService {

	@Autowired
	ITransaccionesInternasRepository transaccionesInternasRepository;
	
	@Override
	public List<TransaccionesInternas> getAllTransaccionesInternas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransaccionesInternas getTransaccionesInternasById(String idTransaccionesInternas) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransaccionesInternas saveTransaccionesInternasById(TransaccionesInternasDTO transaccionesInternasDTO) {
		return transaccionesInternasRepository.save(TransaccionesInternasDTO.CONVERTER_ENTITY.apply(transaccionesInternasDTO));
	}

	@Override
	public void deleteTransaccionesInternasById(String idTransaccionesInternas) {
		// TODO Auto-generated method stub
		
	}

}
