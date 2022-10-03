package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;
import com.ath.adminefectivo.dto.EscalasDTO;
import com.ath.adminefectivo.dto.TarifasOperacionDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.FuncionesDinamicas;
import com.ath.adminefectivo.entities.Escalas;
import com.ath.adminefectivo.entities.TarifasOperacion;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IBancosRepository;
import com.ath.adminefectivo.repositories.IFuncionesDinamicasRepository;
import com.ath.adminefectivo.repositories.IEscalasRepository;
import com.ath.adminefectivo.repositories.ITarifasOperacionRepository;
import com.ath.adminefectivo.service.IEscalasService;
import com.ath.adminefectivo.service.IFuncionesDinamicasService;
import com.ath.adminefectivo.service.ITarifasOperacionService;
import com.querydsl.core.types.Predicate;

@Service
public class EscalasServiceImpl implements IEscalasService {

	@Autowired
	IEscalasRepository escalasRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<EscalasDTO> getEscalas(Predicate predicate) {
		var escalasEntity = escalasRepository.findAll(predicate);
		List<EscalasDTO> escalasDTO = new ArrayList<>();
		escalasEntity.forEach(escala ->{
			escalasDTO.add(EscalasDTO.CONVERTER_DTO.apply(escala));
		});
		return escalasDTO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public EscalasDTO getEscalasById(Integer idEscalas) {
		Escalas escalasEntity = escalasRepository.findById(idEscalas).get();
		if(Objects.isNull(escalasEntity)) {
			throw new NegocioException(ApiResponseCode.ERROR_ESCALAS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_ESCALAS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_ESCALAS_NO_ENCONTRADO.getHttpStatus());
		}
		return EscalasDTO.CONVERTER_DTO.apply(escalasEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EscalasDTO guardarEscalas(EscalasDTO escalasDTO) {
		Escalas escalasEntity = EscalasDTO.CONVERTER_ENTITY.apply(escalasDTO);
		return EscalasDTO.CONVERTER_DTO.apply(escalasRepository.save(escalasEntity));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EscalasDTO actualizarEscalas(EscalasDTO escalasDTO) {
		return this.guardarEscalas(escalasDTO);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean eliminarEscalas(Integer idEscalas) {
		Escalas escalasEntity = escalasRepository.findById(idEscalas).get();
		
		escalasEntity.setEstado(Dominios.ESTADO_GENERAL_ELIMINADO);
		Escalas escalasActualizado = escalasRepository.save(escalasEntity);
		
		if(!Objects.isNull(escalasActualizado)) {
			if(escalasActualizado.getEstado() == Dominios.ESTADO_GENERAL_ELIMINADO) {
				return true;
			}else {
				return false;
			}
			
		}else {
			return false;
		}
	}
	
}
