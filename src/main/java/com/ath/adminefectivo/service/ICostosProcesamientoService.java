package com.ath.adminefectivo.service;

import java.util.Date;
import java.util.List;

import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.entities.CostosProcesamiento;

public interface ICostosProcesamientoService {
	
	
	Long persistir(ValidacionArchivoDTO validacionArchivo);
	
	void saveAll(List<CostosProcesamiento> costo);
	
	List<CostosProcesamiento> findAceptados(String entidad, 
											Date fechaServicioTransporte, 
											String codigoPuntoCargo,
											String nombrePuntoCargo, 
											String ciudadFondo, 
											String nombreTipoServicio);
	
	List<CostosProcesamiento> getByIdArchivoCargado(Long idArchivo);
	
	void aceptarConciliacionRegistro(Long idArchivoCargado);
	
	void persistirMaestroLlavesProcesamiento();

}
