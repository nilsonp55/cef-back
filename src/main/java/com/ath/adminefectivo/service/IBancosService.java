package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.PuntosDTO;
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
	
	/**
	 * Servicio encargado de consultar el codigo punto del banco por codigo de compensacion
	 * Este servicio lanza un error en caso de que el banco no exista
	 * 
	 * @param codigoCompensacion
	 * @return String
	 * @author cesar.castano
	 */
	Integer getCodigoPuntoBanco(Integer codigoCompensacion);

	
	/**
	 * Servicio encargado de consultar el banco por codigo punto, 
	 * y convertirlo a DTO
	 * 
	 * @param codigoPunto
	 * @return BancosDTO
	 * @author duvan.naranjo
	 */
	BancosDTO findBancoByCodigoPunto(int codigoPunto);
	
	/**
	 * Servicio encargado de consultar el banco por abreviatura, 
	 * y convertirlo a DTO
	 * 
	 * @param abreviatura
	 * @return BancosDTO
	 * @author duvan.naranjo
	 */
	BancosDTO findBancoByAbreviatura(String abreviatura);
	
		/**
	 * Servicio encargado de consultar el codigo punto del banco por codigo de compensacion
	 * Este servicio lanza un error en caso de que el banco no exista
	 * 
	 * @param codigoPunto
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean getCodigoPunto(Integer codigoPunto);
	
	/**
	 * Servicio encargado de consultar un banco por codigo punto y 
	 * validar si es un banco aval, en caso de no serlo retorna null
	 * 
	 * @param codigoPunto
	 * @return BancosDTO
	 * @author duvan.naranjo
	 */
	BancosDTO validarPuntoBancoEsAval(int codigoPunto);

}
