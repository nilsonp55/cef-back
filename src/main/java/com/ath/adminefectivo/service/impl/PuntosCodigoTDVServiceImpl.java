package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
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
import com.ath.adminefectivo.entities.QPuntosCodigoTDV;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IPuntosCodigoTDVRepository;
import com.ath.adminefectivo.repositories.jdbc.IPuntosCodigoTDVJdbcRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.IPuntosCodigoTdvService;
import com.ath.adminefectivo.service.IPuntosService;
import com.ath.adminefectivo.utils.UtilsString;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PuntosCodigoTDVServiceImpl implements IPuntosCodigoTdvService {

	@Autowired
	IPuntosCodigoTDVRepository puntosCodigoTDVRepository;
	
	@Autowired
	IPuntosService puntosService;
	
	@Autowired
	IBancosService bancoService;
	
	@Autowired
	IPuntosCodigoTDVJdbcRepository puntosCodigoTDVJdbcRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<PuntosCodigoTdvDTO> getPuntosCodigoTDV(Predicate predicate, Pageable page, String busqueda) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(predicate);
		if(StringUtils.isNoneEmpty(busqueda)) {
			builder.and(QPuntosCodigoTDV.puntosCodigoTDV.codigoPropioTDV.containsIgnoreCase(busqueda));
		}
		Page<PuntosCodigoTDV> puntosCodigoTDV = puntosCodigoTDVRepository
				.findAll(builder, page);
	
		return new PageImpl<>(puntosCodigoTDV.getContent().stream().map(PuntosCodigoTdvDTO.CONVERTER_DTO).toList(),
				puntosCodigoTDV.getPageable(), puntosCodigoTDV.getTotalElements());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PuntosCodigoTDV getEntidadPuntoCodigoTDV(String codigo) {
		var puntosCodigoTDV = puntosCodigoTDVRepository.findByCodigoTDV(codigo);
		if (Objects.isNull(puntosCodigoTDV)) {
			throw new AplicationException(ApiResponseCode.ERROR_PUNTOS_CODIGO_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_PUNTOS_CODIGO_NO_ENCONTRADO.getDescription() + "Codigo no encontrado = "+codigo,
					ApiResponseCode.ERROR_PUNTOS_CODIGO_NO_ENCONTRADO.getHttpStatus());
		} else {
			return puntosCodigoTDV;
		}
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
	public Integer getCodigoPunto(String codigoPuntoTdv, String codigoTdv, Integer bancoAval, String codigoDane) {
      // Sanitize inputs to prevent injection
      log.debug(
          "getCodigoPunto - codigoPuntoTdv: {} - codigoTdv: {} - bancoAval: {} - codigoDane: {}",
          UtilsString.sanitizeInput(codigoPuntoTdv), UtilsString.sanitizeInput(codigoTdv),
          bancoAval, UtilsString.sanitizeInput(codigoDane));
    
      BancosDTO bancoAvalDTO = bancoService.findBancoByCodigoPuntoJdbc(bancoAval);
      var puntosCodigoTDV = puntosCodigoTDVJdbcRepository.findByCodigoPropioTDVAndCodigoTDVAndBancosAndCiudadCodigo(
         	codigoPuntoTdv.trim(), codigoTdv, bancoAvalDTO.getCodigoPunto(), codigoDane);

        if (Objects.isNull(puntosCodigoTDV)) {
            List<PuntosCodigoTDV> puntosCodigoTDVList = puntosCodigoTDVJdbcRepository.findByCodigoPropioTDVAndCodigoTDVAndBancos(
           		codigoPuntoTdv.trim(), codigoTdv, bancoAvalDTO.getCodigoPunto());
            if (!Objects.isNull(puntosCodigoTDVList) ) {
                if (puntosCodigoTDVList.size() > 1 ) {
                    log.debug("Codigo Punto TDV se encuentra mas de una vez. codigoPuntoTdv: {} - codigoTdv: {}", 
                        UtilsString.sanitizeInput(codigoPuntoTdv.trim()), UtilsString.sanitizeInput(codigoTdv));
                    return puntosService.getCodigoPuntoJdbc(bancoAval).getCodigoPunto();
                }else {
                    if (puntosCodigoTDVList.size() == 1) {
                        return puntosCodigoTDVList.get(0).getCodigoPunto();
                    }
                    else {
                    	return puntosService.getCodigoPuntoJdbc(bancoAval).getCodigoPunto();
                    }
                }
            }
            else {
            	return puntosService.getCodigoPuntoJdbc(bancoAval).getCodigoPunto();
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
					ApiResponseCode.ERROR_PUNTOS_CODIGO_TDV_NO_ENCONTRADO.getDescription()+ " codigo punto TDV con codigo = "+idPuntoCodigoTdv,
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
		puntosCodigoTDVRepository.save(puntosCodigoTdvEntity);
		
		return (puntosCodigoTdvEntity.getEstado() == Dominios.ESTADO_GENERAL_ELIMINADO);
	}

}
