package com.ath.adminefectivo.service;

import java.util.List;
import com.ath.adminefectivo.dto.SitiosClientesDTO;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.querydsl.core.types.Predicate;

public interface ISitiosClientesService {

	/**
	 * Servicio que retorna la lista de los Sitios Clientes
	 * @param predicate
	 * @return List<SitiosClientesDTO>
	 * @author cesar.castano
	 */
	List<SitiosClientesDTO> getSitiosClientes(Predicate predicate);
	
	/**
	 * Metodo que retorna el codigo Punto del Sitio segun el codigo del cliente
	 * @param codigoCliente
	 * @return Integer
	 * @author cesar.castano
	 */
	SitiosClientes getCodigoPuntoSitio(Integer codigoPunto);

	/**
	 * Metodo que retorna el codigo Punto del Sitio segun el codigo del cliente
	 * Este servicio consume una optimizacion mediante JDBC
	 * 
	 * @param codigoCliente
	 * @return Integer
	 */
	SitiosClientes getCodigoPuntoSitioJdbc(Integer codigoPunto);
	
	/**
	 * Crear un registro en la tabla sitios_clientes
	 * @param sitioCliente
	 * @return DTO con datos del registro creado
	 * @author prv_nparra
	 */
	SitiosClientesDTO createSitioCliente(SitiosClientesDTO sitioCliente);
	
	/**
	 * actualizar un registro en la tabla sitios_clientes
	 * @param sitioCliente
	 * @return DTO con datos del registro actualizado
	 * @author prv_nparra
	 */
	SitiosClientesDTO updateSitioCliente(SitiosClientesDTO sitioCliente);
	
	/**
	 * eliminar un registro en la tabla sitios_clientes
	 * @param codigoPunto
	 * @author prv_nparra
	 */
	void deteleSitioCliente(Integer codigoPunto);
}
