package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.ArchivosTarifasEspecialesDTO;
import com.ath.adminefectivo.dto.compuestos.ValidacionArchivoDTO;

public interface IArchivosTarifasEspecialesService {
	
	 	/**
		 * Procesa una lista de archivos filtrando y asignando atributos según la
		 * máscara y extensión esperada.
		 *
		 * @param archivos          lista de archivos a procesar
		 * @param mascara           patrón que define el prefijo y el formato de fecha
		 *                          esperado
		 * @param extensionEsperada extensión de archivo permitida (sin punto)
		 * @param isMaestroDefinicion identificador del maestro de definicion del archivo
		 * @param generarIndices    indica si se deben generar IDs secuenciales para
		 *                          cada archivo procesado
		 * @return lista de archivos procesados y filtrados
		 */
		List<ArchivosTarifasEspecialesDTO> filtrarPorMascara(List<ArchivosTarifasEspecialesDTO> archivos, String mascara,
				String extensionEsperada, String idMaestroDefinicion, boolean generarIndices);
		
		
		
		ValidacionArchivoDTO procesarAchivoCargadoTarifaEspecial(ArchivosLiquidacionDTO archivoProcesar);

}
