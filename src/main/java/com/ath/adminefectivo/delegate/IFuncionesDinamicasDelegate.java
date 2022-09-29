package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.FuncionesDinamicasDTO;
import com.querydsl.core.types.Predicate;

/**
 * Delegate que expone los servicios referente a las funciones dinamicas
 * @author duvan.naranjo
 */
public interface IFuncionesDinamicasDelegate {

	/**
	 * Delegate encargado de retornar la lista de todas las funciones dinamicas
	 * 
	 * @return List<FuncionesDinamicasDTO>
	 * @author duvan.naranjo
	 */
	List<FuncionesDinamicasDTO> obtenerFuncionesDinamicasActivas();

	/**
	 * Delegate encargado de ejecutar la funcion con los parametros recibidos 
	 * y retornar un listado de string separados por comas del resultado arrojado 
	 * desde el service
	 * 
	 * @param idFuncion
	 * @param parametros
	 * @return List<String>
	 * @author duvan.naranjo
	 */
	List<String> ejecutarFuncionDinamica(int idFuncion, List<String> parametros);

}
