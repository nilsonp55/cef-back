package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.PuntosCodigoTdvDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.PuntosCodigoTDV;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IPuntosCodigoTDVRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.IPuntosCodigoTdvService;
import com.ath.adminefectivo.service.IPuntosService;
import com.querydsl.core.types.Predicate;

@Service
public class PuntosCodigoTDVServiceImpl implements IPuntosCodigoTdvService {

	@Autowired
	IPuntosCodigoTDVRepository puntosCodigoTDVRepository;
	
	@Autowired
	IPuntosService puntosService;
	
	@Autowired
	IBancosService bancoService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<PuntosCodigoTdvDTO> getPuntosCodigoTDV(Predicate predicate, Pageable page) {
		
		var puntosCodigoTDV = puntosCodigoTDVRepository.findAll(predicate, page);
		return new PageImpl<>(puntosCodigoTDV.getContent().stream()
				.map(PuntosCodigoTdvDTO.CONVERTER_DTO).toList(),puntosCodigoTDV
				.getPageable(), puntosCodigoTDV.getTotalElements());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCodigoTDV getEntidadPuntoCodigoTDV(String codigo) {
		var puntosCodigoTDV = puntosCodigoTDVRepository.findByCodigoTDV(codigo);
		if (Objects.isNull(puntosCodigoTDV)) {
			throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_CODIGO_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_CODIGO_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_PUNTOS_CODIGO_NO_ENCONTRADO.getHttpStatus());
		} else {
			return puntosCodigoTDV;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getCodigoPunto(String codigoPuntoTdv, String codigoTdv, Integer banco_aval, String codigoDane) {
		
		BancosDTO bancoAval = bancoService.findBancoByCodigoPunto(banco_aval);
		
		
		var puntosCodigoTDV = puntosCodigoTDVRepository.findByCodigoPropioTDVAndCodigoTDVAndBancosAndCiudadCodigo(
				codigoPuntoTdv.trim(), codigoTdv, BancosDTO.CONVERTER_ENTITY.apply(bancoAval), codigoDane);
				
		if (Objects.isNull(puntosCodigoTDV)) {
			List<PuntosCodigoTDV> puntosCodigoTDVList = puntosCodigoTDVRepository.findByCodigoPropioTDVAndCodigoTDVAndBancos(
					codigoPuntoTdv.trim(), codigoTdv, BancosDTO.CONVERTER_ENTITY.apply(bancoAval));
			if (puntosCodigoTDVList.size() > 1 || Objects.isNull(puntosCodigoTDV)) {
				if(puntosCodigoTDVList.size() > 1) {
					System.out.println("Codigo Punto TDV se encuentra más de una vez. "+codigoPuntoTdv.trim() +" - "+ codigoTdv);
				}
				
				return puntosService.getEntidadPunto(banco_aval).getCodigoPunto();
			}else {
				return puntosCodigoTDVList.get(0).getCodigoPunto();
			}
		} 
		
		return puntosCodigoTDV.getCodigoPunto();
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PuntosCodigoTdvDTO> getPuntosCodigoTdvAll() {
		List<PuntosCodigoTDV> puntosCodigoTDV = puntosCodigoTDVRepository.findAll();
		List<PuntosCodigoTdvDTO> listPuntosCodigoTDVDto = new ArrayList<>();
		puntosCodigoTDV.forEach(entity -> listPuntosCodigoTDVDto.add(PuntosCodigoTdvDTO.CONVERTER_DTO.apply(entity)));
		return listPuntosCodigoTDVDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCodigoTdvDTO getPuntosCodigoTdvById(Integer idPuntoCodigoTdv) {
		PuntosCodigoTDV puntosCodigoTdvEntity = puntosCodigoTDVRepository.findById(idPuntoCodigoTdv).get();
		if(Objects.isNull(puntosCodigoTdvEntity)) {
			throw new NegocioException(ApiResponseCode.ERROR_PUNTOS_CODIGO_TDV_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_CODIGO_TDV_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_PUNTOS_CODIGO_TDV_NO_ENCONTRADO.getHttpStatus());
		}
		return PuntosCodigoTdvDTO.CONVERTER_DTO.apply(puntosCodigoTdvEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCodigoTdvDTO guardarPuntosCodigoTdv(PuntosCodigoTdvDTO puntosCodigoTdvDTO) {
		PuntosCodigoTDV puntosCodigoTdvEntity = PuntosCodigoTdvDTO.CONVERTER_ENTITY.apply(puntosCodigoTdvDTO);
		var response = puntosCodigoTDVRepository.save(puntosCodigoTdvEntity);
		return PuntosCodigoTdvDTO.CONVERTER_DTO.apply(response);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCodigoTdvDTO actualizarPuntosCodigoTdv(PuntosCodigoTdvDTO puntosCodigoTdvDTO) {
		return this.guardarPuntosCodigoTdv(puntosCodigoTdvDTO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarPuntosCodigoTdv(Integer idPuntoCodigoTdv) {
		PuntosCodigoTDV puntosCodigoTdvEntity = puntosCodigoTDVRepository.findById(idPuntoCodigoTdv).get();
		
		puntosCodigoTdvEntity.setEstado(Dominios.ESTADO_GENERAL_ELIMINADO);
		PuntosCodigoTDV PuntosCodigoTDVActualizado = puntosCodigoTDVRepository.save(puntosCodigoTdvEntity);
		
		if(!Objects.isNull(PuntosCodigoTDVActualizado)) {
			if(puntosCodigoTdvEntity.getEstado() == Dominios.ESTADO_GENERAL_ELIMINADO) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}

}
