package com.ath.adminefectivo.service;

import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;

public interface IArchivosLiquidacionService {
	
	Long persistirCostos(ValidacionArchivoDTO validacionArchivo, ArchivosLiquidacionDTO archivoProcesar);
	
	Long persistirCostoTransporte(ValidacionArchivoDTO validacionArchivo, ArchivosLiquidacionDTO archivoProcesar);
	
	Long persistirCostoProcesamiento(ValidacionArchivoDTO validacionArchivo, ArchivosLiquidacionDTO archivoProcesar);
	
	ValidacionArchivoDTO validarCostoConciliado(MaestrosDefinicionArchivoDTO maestroDefinicion,
				ValidacionArchivoDTO validacionArchivo);

}
