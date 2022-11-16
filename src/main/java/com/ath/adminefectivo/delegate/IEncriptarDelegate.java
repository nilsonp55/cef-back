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
	 * Delegate encargado de retornar la lista de todas las Ciudades
	 * 
	 * @return List<BancosDTO>
	 * @author duvan.naranjo
	 */
	String encriptarArchivo(String path, String nombreArchivo);
	
	String desencriptarArchivo(String path, String nombreArchivo);

}
