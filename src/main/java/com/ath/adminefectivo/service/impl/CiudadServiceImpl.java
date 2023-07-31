package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.exception.AplicationException;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ICiudadesRepository;
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
	public CiudadesDTO getCiudadPorCodigoDaneOrCodigoBrinks(String codigo) {
		Date fechaProceso = parametroService.valorParametroDate(Constantes.FECHA_DIA_PROCESO);
		Ciudades ciudadOpt = ciudadesRepository.findBycodigoDANE(codigo);
		if (Objects.isNull(ciudadOpt)) {
			Ciudades ciudadBrinks = ciudadesRepository.findByCodigoBrinks(Integer.parseInt(codigo));
			if (Objects.isNull(ciudadBrinks)) {
				auditoriaProcesosService.actualizarAuditoriaProceso(Dominios.CODIGO_PROCESO_LOG_CERTIFICACION,
						fechaProceso, Constantes.ESTADO_PROCESO_ERROR,
						ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription());

				throw new NegocioException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
						ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription()
								+ "No existe Ciudad con codigoDane o CodigoBrinks = " + codigo,
						ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());
			} else {
				return CiudadesDTO.CONVERTER_DTO.apply(ciudadBrinks);
			}
		} else {
			return CiudadesDTO.CONVERTER_DTO.apply(ciudadOpt);
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
}
