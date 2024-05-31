package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;

public interface IFuncionesDinamicasService {

	/**
	 * Servicio encargado de consultar la lista de todas las funciones 
	 * dinamicas activas
	 * 
	 * @return List<FuncionesDinamicasDTO>
	 * @author duvan.naranjo
	 */
	List<FuncionesDinamicasDTO> obtenerFuncionesDinamicasActivas();

	
	/**
	 * Metodo encargado de ejecutar la funcion enviada con los parametros recibidos 
	 * 
	 * @param idFuncion
	 * @param parametros
	 * @return List<String>
	 * @author duvan.naranjo
	 */
	List<String> ejecutarFuncionDinamica(int idFuncion, String parametros);



	

}
