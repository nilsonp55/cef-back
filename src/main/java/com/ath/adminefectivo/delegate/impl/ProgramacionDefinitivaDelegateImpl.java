package com.ath.adminefectivo.delegate.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IProgramacionDefinitivaDelegate;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.ArchivosCargadosRepository;
import com.ath.adminefectivo.service.IOperacionesProgramadasService;

@Service
public class ProgramacionDefinitivaDelegateImpl implements IProgramacionDefinitivaDelegate{

	@Autowired
	ArchivosCargadosRepository archivosCargadosRepository;
	
	@Autowired
	IOperacionesProgramadasService operacionesProgramadasService;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean procesarProgramacionDefinitiva(String modeloArchivo, Long idArchivo) { 

		List<ArchivosCargados> archivosCargados = archivosCargadosRepository.findByIdModeloArchivoAndIdArchivo(
														modeloArchivo, idArchivo);

		if(archivosCargados.isEmpty()) {
			throw new NegocioException(ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getCode(),
					ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getDescription(),
					ApiResponseCode.ERROR_ARCHICOS_CARGADOS_NO_ENCONTRADO.getHttpStatus());
		}else {
			operacionesProgramadasService.procesarArchivos(archivosCargados);
			return true;
		}
	}
}
