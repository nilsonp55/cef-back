package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.CentroCiudadDTO;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ICentroCiudadPpalRepository;
import com.ath.adminefectivo.service.ICentroCiudadPpalService;
import com.querydsl.core.types.Predicate;

/**
 * Clase Servicios para gestionar los CentroCiudadPpal
 * 
 * @author prv_nparra
 */
@Service
public class CentroCiudadPpalServiceImpl implements ICentroCiudadPpalService {

    private final ICentroCiudadPpalRepository centroCiudadPpalRepository;

	public CentroCiudadPpalServiceImpl(@Autowired ICentroCiudadPpalRepository centroCiudadPpalRepository) {
		this.centroCiudadPpalRepository = centroCiudadPpalRepository;
	}

    @Override
    public List<CentroCiudadDTO> listCentroCiudad(Predicate predicate) {
        try {
            List<CentroCiudadDTO> listDto = new ArrayList<>();
            centroCiudadPpalRepository.findAll(predicate)
                    .forEach(entity -> listDto.add(CentroCiudadDTO.CONVERTER_DTO_PPAL.apply(entity)));
            return listDto;
        } catch (Exception e) {
            throw new NegocioException(
                "CC-002",
                "Error al listar los centros ciudad principal",
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
            );
        }
    }

    @Override
    public CentroCiudadDTO create(CentroCiudadDTO centroCiudadDTO) {
        try {
            var entity = CentroCiudadDTO.CONVERTER_ENTITY_PPAL.apply(centroCiudadDTO);
            var entitySaved = centroCiudadPpalRepository.save(entity);
            return CentroCiudadDTO.CONVERTER_DTO_PPAL.apply(entitySaved);
        } catch (Exception e) {
            throw new NegocioException(
                "CC-001",
                "Error al crear el centro ciudad principal",
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage()
            );
        }
    }
}
