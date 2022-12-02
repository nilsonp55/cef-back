package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.BancosDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a los bancos
 * @author cduvan.naranjo
 */
public interface IEncriptarDelegate {

	/**
	 * Delegate encargado de encriptar un archivo recibido por la ruta especifica
	 * 
	 * @param path
	 * @param nombreArchivo
	 * @return List<BancosDTO>
	 * @author duvan.naranjo
	 */
	String encriptarArchivo(String path, String nombreArchivo);
	
	/**
	 * Delegate encargado de desencriptar un archivo recibido por la ruta especifica
	 * 
	 * @param path
	 * @param nombreArchivo
	 * @return List<BancosDTO>
	 * @author duvan.naranjo
	 */
	String desencriptarArchivo(String path, String nombreArchivo);

	/**
	 * Metodo encargado de generar las Keys del algoritmo RSA
	 * 
	 * @return String
	 * @author duvan.naranjo
	 */
	String generarLlaves();

}
