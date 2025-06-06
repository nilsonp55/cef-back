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
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IBancosRepository;
import com.ath.adminefectivo.repositories.jdbc.IBancosJdbcRepository;
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
	
	@Autowired
	IBancosJdbcRepository bancosJdbcRepository;
		
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
			throw new NegocioException(ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getDescription()+ " banco no encontrado ="+codigo,
					ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getHttpStatus());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getCodigoPuntoBanco(Integer codigoCompensacion) {
		var bancoOpt = bancosRepository.findByCodigoCompensacion(codigoCompensacion);
		if (bancoOpt == null) {
			throw new NegocioException(ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getDescription()+" Banco no encontrado para codigo = "+codigoCompensacion,
					ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getHttpStatus());
		} else {
			return bancoOpt.getCodigoPunto();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BancosDTO findBancoByCodigoPunto(int codigoPunto) {
		Bancos bancoOpt = bancosRepository.findBancoByCodigoPunto(codigoPunto);

		if (!Objects.isNull(bancoOpt)) {
			return BancosDTO.CONVERTER_DTO.apply(bancoOpt);
		} else {
			throw new NegocioException(ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getDescription() +" Banco no encontrado para codigo = "+codigoPunto,
					ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getHttpStatus());
		}
	}
	
	@Override
	public BancosDTO findBancoByCodigoPuntoJdbc(int codPunto) {	
		BancosDTO bancoDto = bancosJdbcRepository.findBancoByCodigoPunto(codPunto);
		if (Objects.isNull(bancoDto)) {
			throw new NegocioException(ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getCode(), ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getHttpStatus());
		}		
		return bancoDto;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BancosDTO findBancoByAbreviatura(String abreviatura) {
		Bancos bancoOpt = bancosRepository.findBancoByAbreviatura(abreviatura);

		if (!Objects.isNull(bancoOpt)) {
			return BancosDTO.CONVERTER_DTO.apply(bancoOpt);
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean getCodigoPunto(Integer codigoPunto) {
		Boolean estado = true;
		var bancoOpt = bancosRepository.findByCodigoPunto(codigoPunto);
		if (bancoOpt == null) {
			estado = false;
		}
		return estado;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BancosDTO validarPuntoBancoEsAval(int codigoPunto) {
		Bancos banco = bancosRepository.findBancoByCodigoPunto(codigoPunto);
		if(banco.getEsAVAL()) {
			return BancosDTO.CONVERTER_DTO.apply(banco);
		}else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BancosDTO> getBancosPorAval(boolean esAval) {
		List<Bancos> bancos = bancosRepository.findByEsAVAL(esAval);
		List<BancosDTO> bancosDTO = new ArrayList<>();
		bancos.forEach(banco ->
			bancosDTO.add(BancosDTO.CONVERTER_DTO.apply(banco))
		);
		return bancosDTO;
	}
}
