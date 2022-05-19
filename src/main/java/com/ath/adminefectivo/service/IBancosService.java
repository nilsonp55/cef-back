package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.BancosDTO;
import com.querydsl.core.types.Predicate;

public interface IBancosService {

	/**
	 * Servicio encargado de consultar la lista de todos los Bancos filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<BancosDTO>
	 * @author cesar.castano
	 */
	List<BancosDTO> getBancos(Predicate predicate);

	/**
	 * Servicio encargado de consultar el nombre del banco por codigo Este
	 * servicio lanza un error en caso de que el banco no exista
	 * 
	 * @param codigo
	 * @return String
	 * @author cesar.castano
	 */
	String getAbreviatura(Integer codigo);
	
}
