package com.ath.adminefectivo.delegate.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ath.adminefectivo.delegate.IArchivosCargadosDelegate;
import com.ath.adminefectivo.dto.ArchivosCargadosDTO;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.service.IArchivosCargadosService;
import com.ath.adminefectivo.service.IFilesService;
import com.querydsl.core.types.Predicate;

@Service
public class ArchivosCargadosDelegateImpl implements IArchivosCargadosDelegate {

	@Autowired
	IArchivosCargadosService archivosCargadosService;
	
	@Autowired
	IFilesService filesService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArchivosCargadosDTO> getAll() {
		System.out.println("Entro al Delegate");
		return archivosCargadosService.getAll();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ArchivosCargados> guardarArchivo(ArchivosCargadosDTO archivo) {
		archivo.setFechaCreacion(new Date());
		archivo.setUsuarioCreacion("cbenavides");
		archivo.setFechaInicioCargue(new Date());
		return archivosCargadosService.guardarArchivos(Arrays.asList(archivo));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<ArchivosCargadosDTO> getAll(Predicate predicate, Pageable page) {
		return archivosCargadosService.getAll(predicate, page);
	}

	@Override
	public Page<ArchivosCargadosDTO> getAllByAgrupador(String agrupador, Pageable page) {
		return archivosCargadosService.getAllByAgrupador(agrupador, page);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean eliminarArchivo(Long idArchivo) {
		var archivoCargadoDTO = archivosCargadosService.eliminarArchivoCargado(idArchivo);
		if(Objects.nonNull(archivoCargadoDTO.getUrl())) {
			return filesService.eliminarArchivo(archivoCargadoDTO.getUrl());			
		}
		return false;
	}

}
