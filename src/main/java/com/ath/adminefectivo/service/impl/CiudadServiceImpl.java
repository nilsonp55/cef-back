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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CiudadesDTO createCiudad(CiudadesDTO ciudadDTO) {
		if (ciudadDTO == null || ciudadDTO.getCodigoDANE() == null) {
			throw new AplicationException(ApiResponseCode.ERROR_DATOS_ENTRADA_INVALIDOS.getCode(),
					"El código DANE no puede ser nulo para crear una ciudad.",
					ApiResponseCode.ERROR_DATOS_ENTRADA_INVALIDOS.getHttpStatus());
		}
		if (ciudadesRepository.existsById(ciudadDTO.getCodigoDANE())) {
			throw new AplicationException(ApiResponseCode.ERROR_CIUDAD_YA_EXISTE.getCode(),
					"Ciudad con código DANE " + ciudadDTO.getCodigoDANE() + " ya existe.",
					ApiResponseCode.ERROR_CIUDAD_YA_EXISTE.getHttpStatus());
		}
		Ciudades ciudadEntity = CiudadesDTO.CONVERTER_ENTITY.apply(ciudadDTO);
		Ciudades savedCiudad = ciudadesRepository.save(ciudadEntity);
		return CiudadesDTO.CONVERTER_DTO.apply(savedCiudad);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CiudadesDTO getCiudadById(String id) {
		Optional<Ciudades> ciudadOpt = ciudadesRepository.findById(id);
		if (ciudadOpt.isPresent()) {
			return CiudadesDTO.CONVERTER_DTO.apply(ciudadOpt.get());
		} else {
			// Consider if a specific "NOT_FOUND" error is more appropriate
			// Using existing ERROR_CIUDADES_NO_ENCONTRADO for now
			throw new AplicationException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getDescription() + " ID: " + id,
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CiudadesDTO updateCiudad(String id, CiudadesDTO ciudadDTO) {
		if (ciudadDTO == null || id == null) {
			throw new AplicationException(ApiResponseCode.ERROR_DATOS_ENTRADA_INVALIDOS.getCode(),
					"El ID de la ciudad y los datos de la ciudad no pueden ser nulos para actualizar.",
					ApiResponseCode.ERROR_DATOS_ENTRADA_INVALIDOS.getHttpStatus());
		}
		if (!ciudadesRepository.existsById(id)) {
			throw new AplicationException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
					"Ciudad con código DANE " + id + " no encontrada para actualizar.",
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());
		}
		// Ensure the ID in the DTO matches the path variable ID, or decide policy
		// Forcing path ID to be the source of truth for the entity ID
		ciudadDTO.setCodigoDANE(id);
		Ciudades ciudadEntity = CiudadesDTO.CONVERTER_ENTITY.apply(ciudadDTO);
		Ciudades updatedCiudad = ciudadesRepository.save(ciudadEntity);
		return CiudadesDTO.CONVERTER_DTO.apply(updatedCiudad);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteCiudad(String id) {
		if (id == null) {
			throw new AplicationException(ApiResponseCode.ERROR_DATOS_ENTRADA_INVALIDOS.getCode(),
					"El ID de la ciudad no puede ser nulo para eliminar.",
					ApiResponseCode.ERROR_DATOS_ENTRADA_INVALIDOS.getHttpStatus());
		}
		if (!ciudadesRepository.existsById(id)) {
			throw new AplicationException(ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getCode(),
					"Ciudad con código DANE " + id + " no encontrada para eliminar.",
					ApiResponseCode.ERROR_CIUDADES_NO_ENCONTRADO.getHttpStatus());
		}
		ciudadesRepository.deleteById(id);
		// Consider logging the deletion or returning some status
	}
}
