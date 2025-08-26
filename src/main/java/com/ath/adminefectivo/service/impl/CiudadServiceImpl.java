package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.exception.NotFoundException;
import com.ath.adminefectivo.repositories.ICiudadesRepository;
import com.ath.adminefectivo.repositories.jdbc.ICiudadesJdbcRepository;
import com.ath.adminefectivo.service.IAuditoriaProcesosService;
import com.ath.adminefectivo.service.ICiudadesService;
import com.ath.adminefectivo.service.IParametroService;
import com.querydsl.core.types.Predicate;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CiudadServiceImpl implements ICiudadesService {

	@Autowired
	ICiudadesRepository ciudadesRepository;

	@Autowired
	IAuditoriaProcesosService auditoriaProcesosService;

	@Autowired
	IParametroService parametroService;
	
	@Autowired
	ICiudadesJdbcRepository ciudadesJdbcRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CiudadesDTO> getCiudades(Predicate predicate) {
		var ciudades = ciudadesRepository.findAllByOrderByNombreCiudadAsc();
		List<CiudadesDTO> listCiudadesDto = new ArrayList<>();
		ciudades.forEach(entity -> listCiudadesDto.add(CiudadesDTO.CONVERTER_DTO.apply(entity)));
		return listCiudadesDto;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNombreCiudad(String codigoCiudad) {
		log.info("Find by codigoCiudad: {}", codigoCiudad);
		var ciudadOpt = ciudadesRepository.findById(codigoCiudad);
		if (ciudadOpt.isPresent() && Objects.nonNull(ciudadOpt.get().getNombreCiudad())) {
			return ciudadOpt.get().getNombreCiudad();
		} else {
			throw new AplicationException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getCodigoCiudad(String nombre) {
		var ciudadOpt = ciudadesRepository.findByNombreCiudad(nombre);
		if (Objects.isNull(ciudadOpt)) {
			throw new AplicationException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());
		} else {
			return ciudadOpt.getCodigoDANE();
		}
	}

	@Override
	public CiudadesDTO getCiudadPorCodigoDane(String codigo) {
		Ciudades ciudadOpt = ciudadesRepository.findBycodigoDANE(codigo);
		if (Objects.isNull(ciudadOpt)) {
			throw new AplicationException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription() + "No existe Ciudad con codigoDane = " + codigo,
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());
		} else {
			return CiudadesDTO.CONVERTER_DTO.apply(ciudadOpt);
		}
	}

	@Override
	public Ciudades getCiudadPorCodigoDaneOrCodigoBrinks(String codigo) { 
		Date fechaProceso = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		Ciudades ciudadOpt = ciudadesJdbcRepository.findCiudadByCodigoDane(codigo);
		if (Objects.isNull(ciudadOpt)) {
			Ciudades ciudadBrinks = ciudadesJdbcRepository.findCiudadByCodigoBrinks(Integer.parseInt(codigo));
			if (Objects.isNull(ciudadBrinks)) {
				auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION,
						fechaProceso, Constantes.ESTADO_PROCESO_ERROR,
						ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription());

				throw new NegocioException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription()
								+ "No existe Ciudad con codigoDane o CodigoBrinks = " + codigo,
						ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());
			} else {
				return ciudadBrinks;
			}
		} else {
			return ciudadOpt;
		}
	}

	@Override
	public CiudadesDTO getCiudadPorNombreCiudadFiserv(String nombreCiudad) {
		Ciudades ciudadOpt = ciudadesRepository.findByNombreCiudadFiserv(nombreCiudad);

		if (Objects.isNull(ciudadOpt)) {
			throw new NegocioException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription() + "No existe Ciudad con nombreCiudad = "
							+ nombreCiudad,
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());

		} else {
			return CiudadesDTO.CONVERTER_DTO.apply(ciudadOpt);
		}
	}
	
	@Override
	public CiudadesDTO createCiudad(CiudadesDTO ciudad) {
	  log.debug("crear ciudad id: {}", ciudad.getCodigoDANE());
	  Optional<Ciudades> ciudadFind = ciudadesRepository.findById(ciudad.getCodigoDANE());
	  ciudadFind.ifPresent(c -> {
	    log.error("crear ciudad id existe: {}", ciudad.getCodigoDANE());
	    throw new NegocioException(ApiResponseCode.ERROR_EXIST_REGISTRO.getCode(), 
	        ApiResponseCode.ERROR_EXIST_REGISTRO.getDescription(), 
	        ApiResponseCode.ERROR_EXIST_REGISTRO.getHttpStatus());
	  });
	  
	  Ciudades ciudadEntity = CiudadesDTO.CONVERTER_ENTITY.apply(ciudad);
	  ciudadEntity = ciudadesRepository.save(ciudadEntity);
	  log.debug("creada ciudad id: {}", ciudad.getCodigoDANE());
	  return CiudadesDTO.CONVERTER_DTO.apply(ciudadEntity);
	}
    
    @Override
    public CiudadesDTO updateCiudad(CiudadesDTO ciudad) {
      log.debug("actualizar ciudad id: {}", ciudad.getCodigoDANE());
      Optional<Ciudades> ciudadFind = ciudadesRepository.findById(ciudad.getCodigoDANE());
      log.debug("actualizar ciudad isEmpty: {}", ciudadFind.isEmpty());

      Ciudades ciudadEntity = ciudadFind.orElseThrow(
          () -> { 
            log.error("actualizar ciudad id no existe: {}", ciudad.getCodigoDANE());
            throw new NegocioException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
              ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription(),
              ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus()); });
      
      ciudadEntity = CiudadesDTO.CONVERTER_ENTITY.apply(ciudad);
      ciudadEntity = ciudadesRepository.save(ciudadEntity);
      log.debug("actualizada ciudad id: {}", ciudadEntity.getCodigoDANE());
      return CiudadesDTO.CONVERTER_DTO.apply(ciudadEntity);
    }
    
	@Override
    public void deleteCiudad(String codigoDane) {
	  log.debug("deleteCiudad - codigoDane: {}", codigoDane);
	  Optional<Ciudades> ciudad = ciudadesRepository.findById(codigoDane);
	  log.debug("deleteCiudad - isEmpty: {}", ciudad.isEmpty());
	  ciudad.ifPresentOrElse(c -> {
	    ciudadesRepository.delete(c);
	    log.debug("deleteCiudad - existoso codigoDane: {}", codigoDane);
	  }, () -> {
	    log.debug("deleteCiudad - error codigoDane: {}", codigoDane);
	    throw new NotFoundException(CiudadServiceImpl.class.getName(), codigoDane);
	  });
    }
}
