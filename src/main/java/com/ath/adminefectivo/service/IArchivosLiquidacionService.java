package com.ath.adminefectivo.service;

import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;

public interface IArchivosLiquidacionService {
	
	Long persistirCostos(ValidacionArchivoDTO validacionArchivo);
	
	Long persistirCostoTransporte(ValidacionArchivoDTO validacionArchivo);
	
	Long persistirCostoProcesamiento(ValidacionArchivoDTO validacionArchivo);
	
	ValidacionArchivoDTO validarCostoConciliado(MaestrosDefinicionArchivoDTO maestroDefinicion,
				ValidacionArchivoDTO validacionArchivo);

}
