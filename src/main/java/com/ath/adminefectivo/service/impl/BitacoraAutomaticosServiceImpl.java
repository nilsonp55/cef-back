package com.ath.adminefectivo.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.BitacoraAutomaticosDTO;
import com.ath.adminefectivo.entities.BitacoraAutomaticos;
import com.ath.adminefectivo.repositories.IBitacoraAutomaticosRepository;
import com.ath.adminefectivo.service.IBitacoraAutomaticosService;
import com.querydsl.core.types.Predicate;

@Service
public class BitacoraAutomaticosServiceImpl implements IBitacoraAutomaticosService {

	@Autowired
	IBitacoraAutomaticosRepository bitacoraAutomaticosRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BitacoraAutomaticosDTO> getBitacoraAutomaticos(Predicate predicate) {
		return new ArrayList<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public  BitacoraAutomaticosDTO guardarBitacoraAutomaticos(BitacoraAutomaticosDTO bitacoraDTO) {
		BitacoraAutomaticos bitacora = BitacoraAutomaticosDTO.CONVERTER_ENTITY.apply(bitacoraDTO);
		
		return BitacoraAutomaticosDTO.CONVERTER_DTO.apply(bitacoraAutomaticosRepository.save(bitacora));	
	}




	
}
