package com.ath.adminefectivo.service;

import java.util.List;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
import com.querydsl.core.types.Predicate;

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
	List<String[]> ejecutarFuncionDinamica(int idFuncion, String parametros);



	

}
