package com.ath.adminefectivo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.dto.RegistrosCargadosDTO;
import com.ath.adminefectivo.repositories.IRegistrosCargadosRepository;
import com.ath.adminefectivo.service.IRegistrosCargadosService;

@Service
public class RegistrosCargadosServiceImpl implements IRegistrosCargadosService {

	@Autowired
	IRegistrosCargadosRepository registrosCargadosRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RegistrosCargadosDTO> consultarRegistrosCargadosPorIdArchivo(Long idArchivo) {
		var registrosCargados = registrosCargadosRepository.findByIdIdArchivo(idArchivo);
		List<RegistrosCargadosDTO> listRegistrosArchivoDto = new ArrayList<>();
		registrosCargados
				.forEach(entity -> listRegistrosArchivoDto.add(RegistrosCargadosDTO.CONVERTER_DTO.apply(entity)));
		return listRegistrosArchivoDto;
	}

}
