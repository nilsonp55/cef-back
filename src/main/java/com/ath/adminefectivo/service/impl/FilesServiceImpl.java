package com.ath.adminefectivo.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Parametros;
import com.ath.adminefectivo.dto.DownloadDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.service.IFilesService;
import com.ath.adminefectivo.service.IParametroService;

@Service
public class FilesServiceImpl implements IFilesService {

	private static final String TEMPORAL_URL = "C:\\Ath\\Docs";
	
	@Autowired
	IParametroService parametroService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean persistirArchvos(List<MultipartFile> files) {

		this.validarPath(TEMPORAL_URL);
		for (MultipartFile file : files) {
			var dest = new File(TEMPORAL_URL, file.getOriginalFilename());
			try {
				Files.copy(file.getInputStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;

	}

	/**
	 */
	@Override
	public String persistirArchvo(MultipartFile file) {

		this.validarPath(TEMPORAL_URL);
		File dest = new File(TEMPORAL_URL, file.getOriginalFilename());
		try {
			Files.copy(file.getInputStream(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			throw new NegocioException(ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getCode(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getDescription(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getHttpStatus());

		}
		return dest.getPath();

	}

	@Override
	public DownloadDTO downloadFile(DownloadDTO download) {

		String path = download.getUrl();
		try {
			File initialFile = new File(path);
			Resource recurso = new UrlResource(initialFile.toURI());
			download.setFile(recurso.getInputStream());

		} catch (IOException e) {
			e.printStackTrace();
			throw new NegocioException(ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getCode(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getDescription(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getHttpStatus());
		}
		return download;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean eliminarArchivo(String url) {
		try {
			Files.delete(Path.of(url));
			return true;

		} catch (IOException e) {
			throw new NegocioException(ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getCode(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getDescription(),
					ApiResponseCode.ERROR_ARCHIVOS_NO_EXISTE_BD.getHttpStatus());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> obtenerContenidoCarpeta(String url) {
		File carpeta = new File(url);
		if (!carpeta.isDirectory()) {
			throw new NegocioException(ApiResponseCode.ERROR_CARPETA_NO_ENCONTRADA.getCode(),
					ApiResponseCode.ERROR_CARPETA_NO_ENCONTRADA.getDescription(),
					ApiResponseCode.ERROR_CARPETA_NO_ENCONTRADA.getHttpStatus());
		}

		return Arrays.asList(carpeta.list());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean moverArchivos(String urlSource, String urlDestino, String nombreArchivo) {
		Path origenPath = FileSystems.getDefault().getPath(urlSource);
		this.validarPath(urlDestino);
		Path destinoPath = FileSystems.getDefault().getPath(urlDestino, nombreArchivo);

		try {
			Files.move(origenPath, destinoPath);
		} catch (IOException e) {
			throw new NegocioException(ApiResponseCode.ERROR_MOVER_ARCHIVOS.getCode(),
					ApiResponseCode.ERROR_MOVER_ARCHIVOS.getDescription(),
					ApiResponseCode.ERROR_MOVER_ARCHIVOS.getHttpStatus());
		}

		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String consultarPathArchivos(String estado) {
		String parametro;
		switch (estado) {
		case Constantes.ESTADO_CARGUE_PENDIENTE:
			parametro = Parametros.RUTA_ARCHIVOS_PENDIENTES;
			break;
		case Constantes.ESTADO_CARGUE_ERROR:
			parametro = Parametros.RUTA_ARCHIVOS_ERRADOS;
			break;
		case Constantes.ESTADO_CARGUE_VALIDO:
			parametro = Parametros.RUTA_ARCHIVOS_PROCESADOS;
			break;
		default:
			throw new NegocioException(ApiResponseCode.ERROR_ESTADO_ARCHIVO.getCode(),
					ApiResponseCode.ERROR_ESTADO_ARCHIVO.getDescription(),
					ApiResponseCode.ERROR_ESTADO_ARCHIVO.getHttpStatus());
		}

		return parametroService.valorParametro(parametro);

	}

	/**
	 * Metodo encargado de validar si la ruta del path existe en cason contrario la
	 * crea
	 * 
	 * @param path
	 * @author CamiloBenavides
	 */
	private void validarPath(String path) {
		var file = new File(path);
		if (!file.exists() && !file.mkdirs()) {
			throw new IllegalArgumentException();
		}
	}

}
