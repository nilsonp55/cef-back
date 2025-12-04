package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ath.adminefectivo.dto.TiposCuentasDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.TiposCuentas;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.ConflictException;
import com.ath.adminefectivo.repositories.ITiposCuentasRepository;
import com.ath.adminefectivo.service.ITiposCuentasService;
import com.querydsl.core.types.Predicate;

import lombok.extern.log4j.Log4j2;

/**
 * Servicios para gestionar los tipos de cuentas
 * @author Bayron Perez
 */

@Log4j2
@Service
public class TiposCuentasServiceImpl implements ITiposCuentasService {

	@Autowired
	ITiposCuentasRepository tiposCuentasRepository;
	
	
	@Override
	public List<TiposCuentasDTO> getAllTiposCuentas(Predicate predicate) {
		var cuentas = tiposCuentasRepository.findAll(predicate);
		List<TiposCuentasDTO> listcuentasDto = new ArrayList<>();
		cuentas.forEach(entity -> listcuentasDto.add(TiposCuentasDTO.CONVERTER_DTO.apply(entity)));
		return listcuentasDto;
	}

    @Override
    public TiposCuentasDTO getTiposCuentasById(String idTipoCuentas) {
      Optional<TiposCuentas> cuentas = tiposCuentasRepository.findById(idTipoCuentas);
      TiposCuentas tipoCuenta = cuentas.orElseThrow(
          () -> new AplicationException(ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getCode(),
              ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getDescription()
                  + " TipoCuenta no encontrada para id = " + idTipoCuentas,
              ApiResponseCode.ERROR_PUNTOS_NO_ENCONTRADO.getHttpStatus()));

      return TiposCuentasDTO.CONVERTER_DTO.apply(tipoCuenta);
    }

	@Override
	public TiposCuentasDTO saveTiposCuentas(TiposCuentasDTO tiposCuentasDTO) {
		if (tiposCuentasDTO.getTipoCuenta() != null && tiposCuentasRepository
				.existsById(tiposCuentasDTO.getTipoCuenta())) {		
			throw new ConflictException(ApiResponseCode.ERROR_TIPOS_CUENTAS_EXIST.getDescription());		
		}
		TiposCuentas tipoCuenta = TiposCuentasDTO.CONVERTER_ENTITY.apply(tiposCuentasDTO);
		TiposCuentas tipoCuentas = tiposCuentasRepository.save(tipoCuenta);
		
		return TiposCuentasDTO.CONVERTER_DTO.apply(tipoCuentas);
	}

	@Override
	public TiposCuentasDTO putTiposCuentas(TiposCuentasDTO tiposCuentasDTO) {
		if (tiposCuentasDTO.getTipoCuenta() == null && !tiposCuentasRepository
				.existsById(tiposCuentasDTO.getTipoCuenta())) {		
			throw new ConflictException(ApiResponseCode.ERROR_TIPOS_CUENTAS_NO_EXIST.getDescription());		
		}
		TiposCuentas tipoCuentas = tiposCuentasRepository.save(TiposCuentasDTO.CONVERTER_ENTITY
				.apply(tiposCuentasDTO));
		
		return TiposCuentasDTO.CONVERTER_DTO.apply(tipoCuentas);
	}

	@Override
	public void deleteTiposCuentasById(TiposCuentasDTO tiposCuentasDTO) {
		if (tiposCuentasDTO.getTipoCuenta() == null && !tiposCuentasRepository
				.existsById(tiposCuentasDTO.getTipoCuenta())) {
			log.error("Throw exception, Eliminar Id TiposCuentas: {}", tiposCuentasDTO.getTipoCuenta());
			throw new ConflictException(ApiResponseCode.ERROR_CUENTAS_PUC_NO_EXIST.getDescription());		
		}
		log.debug("Eliminar Id TiposCuentas: {}", tiposCuentasDTO.getTipoCuenta());
		tiposCuentasRepository.deleteById(tiposCuentasDTO.getTipoCuenta());
		log.debug("Se elimino Id TiposCuentas: {}", tiposCuentasDTO.getTipoCuenta());
	}

}
