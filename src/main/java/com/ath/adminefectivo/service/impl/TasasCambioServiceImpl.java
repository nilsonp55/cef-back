package com.ath.adminefectivo.service.impl;

import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.TasasCambio;
import com.ath.adminefectivo.entities.TasasCambioPK;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.TasasCambioRepository;
import com.ath.adminefectivo.service.TasasCambioService;
import com.querydsl.core.types.Predicate;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class TasasCambioServiceImpl implements TasasCambioService {

	@Autowired
	TasasCambioRepository tasasCambioRepository;
	
	
    @Override
    public Page<TasasCambio> getTasasCambios(Predicate predicate, Pageable page) {
      Page<TasasCambio> tasasCambios = tasasCambioRepository.findAll(predicate, page);
      return tasasCambios;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TasasCambio getTasasCambioById(TasasCambioPK tasasCambioPK) {
		Optional<TasasCambio> tasa = tasasCambioRepository.findById(tasasCambioPK);
		if (Objects.isNull(tasa)) {
			throw new AplicationException(ApiResponseCode.ERROR_TASAS_CAMBIO_NO_EXIST.getCode(),
					ApiResponseCode.ERROR_TASAS_CAMBIO_NO_EXIST.getDescription(),
					ApiResponseCode.ERROR_TASAS_CAMBIO_NO_EXIST.getHttpStatus());
		} 
		return tasa.get();
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public TasasCambio postTasasCambio(TasasCambio tasasCambio) {
      Optional<TasasCambio> tasa = tasasCambioRepository.findById(tasasCambio.getTasasCambioPK());
      tasa.ifPresent(t -> {
        log.debug("Id tasa existe: {}", t.getTasasCambioPK());
        throw new NegocioException(ApiResponseCode.ERROR_TASA_CAMBIO_EXISTE.getCode(),
            ApiResponseCode.ERROR_TASA_CAMBIO_EXISTE.getDescription(),
            ApiResponseCode.ERROR_TASA_CAMBIO_EXISTE.getHttpStatus());
      });
      return tasasCambioRepository.save(tasasCambio);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TasasCambio putTasasCambio(TasasCambio tasasCambio) {
	  Optional<TasasCambio> tasa = tasasCambioRepository.findById(tasasCambio.getTasasCambioPK());
	  tasa.ifPresentOrElse(t -> {
	    tasasCambioRepository.save(tasasCambio);
	  }, () -> {
	    log.debug("Id tasa de cambio noexiste: {}", tasasCambio.getTasasCambioPK());
	    throw new NegocioException(ApiResponseCode.ERROR_TASAS_CAMBIO_NO_ENCONTRADO.getCode(), 
	        ApiResponseCode.ERROR_TASAS_CAMBIO_NO_ENCONTRADO.getDescription(), 
	        ApiResponseCode.ERROR_TASAS_CAMBIO_NO_ENCONTRADO.getHttpStatus());
	  });
	  return tasasCambio;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTasasCambio(TasasCambio tasasCambio) {
		tasasCambioRepository.delete(tasasCambio);
	}

}

