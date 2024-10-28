package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.PuntosCodigoTdvDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.PuntosCodigoTDV;
import com.ath.adminefectivo.entities.QPuntosCodigoTDV;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IPuntosCodigoTDVRepository;
import com.ath.adminefectivo.service.IBancosService;
import com.ath.adminefectivo.service.IPuntosCodigoTdvService;
import com.ath.adminefectivo.service.IPuntosService;
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<PuntosCodigoTdvDTO> getPuntosCodigoTDV(Predicate predicate, Pageable page, String busqueda) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(predicate);
		if(StringUtils.hasText(busqueda)) {
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
      log.debug("getCodigoPunto - codigoPuntoTdv: {} - codigoTdv: {} - bancoAval: {} - codigoDane: {}", codigoPuntoTdv, codigoTdv, bancoAval, codigoDane);  
      BancosDTO bancoAvalDTO = bancoService.findBancoByCodigoPunto(bancoAval);
        var puntosCodigoTDV = puntosCodigoTDVRepository.findByCodigoPropioTDVAndCodigoTDVAndBancosAndCiudadCodigo(
                codigoPuntoTdv.trim(), codigoTdv, BancosDTO.CONVERTER_ENTITY.apply(bancoAvalDTO), codigoDane);
        if (Objects.isNull(puntosCodigoTDV)) {
            List<PuntosCodigoTDV> puntosCodigoTDVList = puntosCodigoTDVRepository.findByCodigoPropioTDVAndCodigoTDVAndBancos(
                    codigoPuntoTdv.trim(), codigoTdv, BancosDTO.CONVERTER_ENTITY.apply(bancoAvalDTO));
            if (!Objects.isNull(puntosCodigoTDVList) ) {
                if (puntosCodigoTDVList.size() > 1 ) {
                    log.debug("Codigo Punto TDV se encuentra mas de una vez. "+codigoPuntoTdv.trim() +" - "+ codigoTdv);
                    return puntosService.getEntidadPunto(bancoAval).getCodigoPunto();
                }else {
                    if (puntosCodigoTDVList.size() == 1) {
                        return puntosCodigoTDVList.get(0).getCodigoPunto();
                    }
                    else {
                        return puntosService.getEntidadPunto(bancoAval).getCodigoPunto();
                    }
                }
            }
            else {
                return puntosService.getEntidadPunto(bancoAval).getCodigoPunto();
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
		PuntosCodigoTDV puntosCodigoTDVActualizado = puntosCodigoTDVRepository.save(puntosCodigoTdvEntity);
		
		if(!Objects.isNull(puntosCodigoTDVActualizado)) {
			return (puntosCodigoTdvEntity.getEstado() == Dominios.ESTADO_GENERAL_ELIMINADO);
		}else {
			return false;
		}
	}

}
