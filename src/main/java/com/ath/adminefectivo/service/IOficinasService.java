package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.OficinasDTO;
import com.querydsl.core.types.Predicate;

public interface IOficinasService {

	/**
	 * Servicio encargado de consultar la lista de todos los Bancos filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<OficinasDTO>
	 * @author cesar.castano
	 */
	List<OficinasDTO> getOficinas(Predicate predicate);
	
	/**
	 * Servicio encargado de consultar el codigo de Punto con el codigo de Oficina
	 * @param codigoOficina
	 * @param codigoBancoAval
	 * @return Integer
	 */
	Integer getCodigoPunto(Integer codigoOficina, Integer codigoBancoAval);
	
	/**
	 * Servicio encargado de consultar el codigo de Punto en la entidad Oficinas
	 * @param codigoPunto
	 * @return Boolean
	 */
	Boolean getCodigoPuntoOficina(Integer codigoPunto);

}
