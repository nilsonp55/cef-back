package com.ath.adminefectivo.service;

import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;
import com.ath.adminefectivo.entities.MaestroLlavesCostosEntity;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;


public interface IArchivosLiquidacionService {
	
	Long persistirCostos(ValidacionArchivoDTO validacionArchivo, ArchivosLiquidacionDTO archivoProcesar);
	
	Long persistirCostoTransporte(ValidacionArchivoDTO validacionArchivo, ArchivosLiquidacionDTO archivoProcesar);
	
	Long persistirCostoProcesamiento(ValidacionArchivoDTO validacionArchivo, ArchivosLiquidacionDTO archivoProcesar);
	
	ValidacionArchivoDTO validarCostoConciliado(MaestrosDefinicionArchivoDTO maestroDefinicion,
				ValidacionArchivoDTO validacionArchivo);
	
	String getEntradaSalida(String tipoServicio, String idMaestroDefinicion);
}
