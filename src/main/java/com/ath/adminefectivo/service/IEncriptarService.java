package com.ath.adminefectivo.service;

import java.io.InputStream;
import java.util.List;

public interface IEncriptarService {

	/**
	 * Metodo encargado de realizar el servicio de encriptar un archivo segun 
	 * su ubicacion
	 * 
	 * @param path
	 * @param nombreArchivo
	 * @return String
	 * @author prv_dnaranjo
	 */
	
	public String encriptarArchivo(String path, String nombreArchivo);
	
	/**
	 * Metodo encargado de realizar el servicio de desencriptar un archivo segun 
	 * su ubicacion
	 * 
	 * @param path
	 * @param nombreArchivo
	 * @return String
	 * @author prv_dnaranjo
	 */
	public String desencriptarArchivo(String path, String nombreArchivo);

	/**
	 * Metodo encargado de realizar el servicio de desencriptar un listado de string   
	 * segun el algoritmo configurado
	 * 
	 * @param algoritmoEncriptado
	 * @param resultadoSinValidar
	 * @return List<String[]>
	 * @author prv_dnaranjo
	 */
	public List<String[]> desencriptarArchivoPorAlgoritmo(String algoritmoEncriptado,
			InputStream archivo, String delimitador);

	/**
	 * Servicio encargado de realizar el llamado a la creacion de la 
	 * llave publica y privada del algoritmo RSA de encriptación
	 * 
	 * @return String
	 * @author prv_dnaranjo
	 */
	public String generarLlaves();

	

}
