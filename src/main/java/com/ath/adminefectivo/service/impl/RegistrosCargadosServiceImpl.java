package com.ath.adminefectivo.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.dto.DominioDTO;
import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.RegistrosCargadosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.RegistrosCargados;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.IRegistrosCargadosRepository;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.IParametroService;
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

		List<RegistrosCargados> registrosCargados = registrosCargadosRepository.findByIdIdArchivo(idArchivo);
		List<RegistrosCargadosDTO> listRegistrosArchivoDto = new ArrayList<>();
		registrosCargados
				.forEach(entity -> listRegistrosArchivoDto.add(RegistrosCargadosDTO.CONVERTER_DTO.apply(entity)));
		return listRegistrosArchivoDto;
	}

}
