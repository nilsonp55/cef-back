package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.repositories.IBancosRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.IDominioService;
import com.ath.adminefectivo.service.IPuntosService;
import com.querydsl.core.types.Predicate;

@Service
public class BancosServiceImpl implements IBancosService {

	@Autowired
	IBancosRepository bancosRepository;
	
	@Autowired
	IDominioService dominioService;
	
	@Autowired
	IPuntosService puntoService;
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BancosDTO> getBancos(Predicate predicate) {
		var bancos = bancosRepository.findAll(predicate);
		List<BancosDTO> listBancosDto = new ArrayList<>();
		bancos.forEach(entity -> {var bancosDTO = new BancosDTO();
								  bancosDTO = BancosDTO.CONVERTER_DTO.apply(entity);
								  bancosDTO.setNombreBanco(puntoService.getNombrePunto(
										  	dominioService.valorTextoDominio(
										  			Constantes.DOMINIO_TIPOS_PUNTO, 
										  			Dominios.TIPOS_PUNTO_BANCO), 
										  	entity.getCodigoPunto()));
								  listBancosDto.add(bancosDTO);
									});
		return listBancosDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAbreviatura(Integer codigo) {
		var bancoOpt = bancosRepository.findById(codigo);

		if (bancoOpt.isPresent() && Objects.nonNull(bancoOpt.get().getAbreviatura())) {
			return bancoOpt.get().getAbreviatura();
		} else {
			throw new AplicationException(ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getHttpStatus());
		}
	}
}
