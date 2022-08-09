package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ath.adminefectivo.entities.TransaccionesInternas;
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
	public TransaccionesInternas getTransaccionesInternasById(String idTransaccionesInternas) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransaccionesInternas saveTransaccionesInternasById(TransaccionesInternasDTO transaccionesInternasDTO) {
		System.out.println("/////// "+transaccionesInternasDTO);
		var x = TransaccionesInternasDTO.CONVERTER_ENTITY.apply(transaccionesInternasDTO);
		return transaccionesInternasRepository
				.save(x);
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
				.add(TransaccionesInternasDTO.CONVERTER_DTO.apply(transaccionInterna))
			);

		return listadoTransaccionesInternasDTO;
	}

}
