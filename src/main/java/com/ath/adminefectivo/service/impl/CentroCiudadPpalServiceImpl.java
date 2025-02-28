package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.dto.CentroCiudadDTO;
import com.ath.adminefectivo.repositories.ICentroCiudadPpalRepository;
import com.ath.adminefectivo.service.ICentroCiudadPpalService;
import com.querydsl.core.types.Predicate;
import lombok.extern.log4j.Log4j2;

/**
 * Clase Servicios para gestionar los CentroCiudadPpal
 * 
 * @author prv_nparra
 */
@Log4j2
@Service
public class CentroCiudadPpalServiceImpl implements ICentroCiudadPpalService {

    private final ICentroCiudadPpalRepository centroCiudadPpalRepository;

	public CentroCiudadPpalServiceImpl(@Autowired ICentroCiudadPpalRepository centroCiudadPpalRepository) {
		this.centroCiudadPpalRepository = centroCiudadPpalRepository;
	}

	@Override
	public List<CentroCiudadDTO> listCentroCiudad(Predicate predicate) {
		log.debug("Listar CentroCiudadPpal: {}", predicate);
		List<CentroCiudadDTO> listDto = new ArrayList<>();
		centroCiudadPpalRepository.findAll(predicate)
				.forEach(entity -> listDto.add(CentroCiudadDTO.CONVERTER_DTO_PPAL.apply(entity)));
		log.debug("CentroCiudadPpal listar size: {}", listDto.size());
		return listDto;
	}

	@Override
	public CentroCiudadDTO create(CentroCiudadDTO centroCiudadDTO) {
		log.debug("CentroCiudadPpal crear: {}", centroCiudadDTO);		
		var entity = CentroCiudadDTO.CONVERTER_ENTITY_PPAL.apply(centroCiudadDTO);
		var entitySaved = centroCiudadPpalRepository.save(entity);
		log.debug("CentroCiudadPpal creado ID: {}", entitySaved.getIdCentroCiudadPpal());
		return CentroCiudadDTO.CONVERTER_DTO_PPAL.apply(entitySaved);
	}

	@Override
	public CentroCiudadDTO update(CentroCiudadDTO centroCiudadDTO) {
		log.debug("CentroCiudadPpal Update ID: {}", centroCiudadDTO.getIdCentroCiudad());
		centroCiudadPpalRepository.findById(centroCiudadDTO.getIdCentroCiudad()).orElseThrow();

		var entity = CentroCiudadDTO.CONVERTER_ENTITY_PPAL.apply(centroCiudadDTO);
		entity.setIdCentroCiudadPpal(centroCiudadDTO.getIdCentroCiudad());
		var entitySaved = centroCiudadPpalRepository.save(entity);
		log.debug("CentroCiudadPpal Updated ID: {}", entitySaved.getIdCentroCiudadPpal());
		return CentroCiudadDTO.CONVERTER_DTO_PPAL.apply(entitySaved);
	}
	
	@Override
	public void delete(Integer idCentroCiudad) {
		log.debug("Eliminar CentroCiudadPpal ID: {}", idCentroCiudad);
		centroCiudadPpalRepository.deleteById(idCentroCiudad);
	}
}
