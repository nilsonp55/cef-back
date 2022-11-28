package com.ath.adminefectivo.service;

import java.io.InputStream;
import java.util.List;

import com.ath.adminefectivo.dto.MaestrosDefinicionArchivoDTO;

/**
 * Clase que contiene los servicios encargados de la lectura de los archivos
 *
 * @author CamiloBenavides
 */
public interface ILecturaArchivoService {

	/**
	 * Servicio encargado de consultar y retornar el patrón delimitador de un archivo
	 * 
	 * @param maestroDefinicion
	 * @return String
	 * @author CamiloBenavides
	 */
	String obtenerDelimitadorArchivo(MaestrosDefinicionArchivoDTO maestroDefinicion);
	

	/**
	 * Metodo encargado la informacion de un documento, 
	 * @param archivo
	 * @param delimitador
	 * @return
	 * @return List<String[]>
	 * @author CamiloBenavides
	 */
	List<String[]> leerArchivo(InputStream archivo, String delimitador, String tipoEncriptado);

		
}
