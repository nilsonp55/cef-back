package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.FuncionesDinamicas;
import com.ath.adminefectivo.entities.TarifasOperacion;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IBancosRepository;
import com.ath.adminefectivo.repositories.IFuncionesDinamicasRepository;
import com.ath.adminefectivo.repositories.ITarifasOperacionRepository;
import com.ath.adminefectivo.service.IFuncionesDinamicasService;
import com.ath.adminefectivo.service.ITarifasOperacionService;
import com.querydsl.core.types.Predicate;

@Service
public class TarifasOperacionServiceImpl implements ITarifasOperacionService {

	@Autowired
	ITarifasOperacionRepository tarifasOperacionRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TarifasOperacionDTO> getTarifasOperacion(Predicate predicate) {
		var tarifasOperacionesEntity = tarifasOperacionRepository.findAll(predicate);
		List<TarifasOperacionDTO> tarifasOperacionesDTO = new ArrayList<>();
		tarifasOperacionesEntity.forEach(tarifaOperacion ->{
			tarifasOperacionesDTO.add(TarifasOperacionDTO.CONVERTER_DTO.apply(tarifaOperacion));
		});
		return tarifasOperacionesDTO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TarifasOperacionDTO getTarifasOperacionById(Integer idTarifaOperacion) {
		TarifasOperacion tarifaOperacionEntity = tarifasOperacionRepository.findById(idTarifaOperacion).get();
		if(Objects.isNull(tarifaOperacionEntity)) {
			throw new NegocioException(ApiResponseCode.ERROR_TARIFAS_OPERACION_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_TARIFAS_OPERACION_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_TARIFAS_OPERACION_NO_ENCONTRADO.getHttpStatus());
		}
		return TarifasOperacionDTO.CONVERTER_DTO.apply(tarifaOperacionEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TarifasOperacionDTO guardarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO) {
		TarifasOperacion tarifaOperacionEntity = TarifasOperacionDTO.CONVERTER_ENTITY.apply(tarifasOperacionDTO);
		return TarifasOperacionDTO.CONVERTER_DTO.apply(tarifasOperacionRepository.save(tarifaOperacionEntity));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TarifasOperacionDTO actualizarTarifasOperacion(TarifasOperacionDTO tarifasOperacionDTO) {
		return this.guardarTarifasOperacion(tarifasOperacionDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarTarifasOperacion(Integer idTarifaOperacion) {
		TarifasOperacion tarifaOperacionEntity = tarifasOperacionRepository.findById(idTarifaOperacion).get();
		
		tarifaOperacionEntity.setEstado(Dominios.ESTADO_GENERAL_ELIMINADO);
		TarifasOperacion tarifaOperacionActualizado = tarifasOperacionRepository.save(tarifaOperacionEntity);
		
		if(!Objects.isNull(tarifaOperacionActualizado)) {
			return true;
		}else {
			return false;
		}
	}

	
	


	
}
