package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.FondosDTO;
import com.ath.adminefectivo.entities.Fondos;
import com.querydsl.core.types.Predicate;

public interface IFondosService {

	/**
	 * Servicio encargado de consultar la lista de todos los Fondos filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<FondosDTO>
	 * @author cesar.castano
	 */
	List<FondosDTO> getFondos(Predicate predicate);

	/**
	 * Servicio encargado de consultar los datos del Fondo por codigo Este
	 * servicio lanza un error en caso de que el fondo no exista
	 * 
	 * @param codigo
	 * @return Fondos
	 * @author cesar.castano
	 */
	Fondos getDatosFondos(String codigo);
	
	/**
	 * Servicio encargado de consultar el codigo del Fondo. Este
	 * servicio lanza un error en caso de que el fondo no exista
	 * 
	 * @param nombreTransportadora
	 * @param codigoCompensacion
	 * @param codigoCiudad
	 * @return Fondos
	 * @author cesar.castano
	 */
	Fondos getCodigoFondo(String nombreTransportadora, Integer codigoCompensacion, String codigoCiudad);
	
	/**
	 * Servicio encargado de consultar el codigo del Fondo. Este
	 * servicio lanza un error en caso de que el fondo no exista
	 * 
	 * @param nombreTransportadora
	 * @param tipoPuntoBanco
	 * @param nombreBanco
	 * @param nombreCiudad
	 * @return Fondos
	 * @author cesar.castano
	 */
	Fondos getCodigoFondo(String nombreTransportadora, String tipoPuntoBanco, String nombreBanco, String nombreCiudad);

	/**
	 * Servicio encargado de consultar el codigo del Fondo. Este
	 * servicio lanza un error en caso de que el fondo no exista
	 * 
	 * @param codigoTransportadora
	 * @param tipoPuntoBanco
	 * @param nombreBanco
	 * @param codigoCiudad
	 * @return Fondos
	 * @author cesar.castano
	 */
	public Fondos getCodigoFondoCertificacion(String codigoTransportadora, String numeroNit, String codigoCiudad);
	
	/**
	 * Servicio encargado de consultar el codigo Punto. Este
	 * servicio lanza un error en caso de que el fondo no exista
	 * 
	 * @param codigoTransportadora
	 * @param nombreBanco
	 * @param tipoPuntoBanco
	 * @param codigoCiudad
	 * @return Fondos
	 * @author cesar.castano
	 */
	Boolean getCodigoPuntoFondo(Integer codigoPunto);
	
	/**
	 * Servicio encargado de consultar el codigo Punto. Este
	 * servicio lanza un error en caso de que el fondo no exista
	 * @param codigoPunto
	 * @return Fondos
	 * @author cesar.castano
	 */
	Fondos getEntidadFondo(Integer codigoPunto);
	
	/**
	 * Servicio encargado de consultar el fondo por codigo punto. Este
	 * servicio lanza un error en caso de que el fondo no exista
	 * @param codigoPunto
	 * @return FondosDTO
	 * @author duvan.naranjo
	 */
	FondosDTO getFondoByCodigoPunto(Integer codigoPunto);
	
	/**
	 * Servicio encargado de consultar el fondo por transportadora.
	 * servicio lanza un error en caso de que la transportadora no tenga fondos
	 * @param transportadora
	 * @return List<FondosDTO>
	 * @author duvan.naranjo
	 */
	List<FondosDTO> getFondoByTdvAndBanco(String transportadora, int bancoAval);

	/**
	 * Servicio encargado de consultar el codigo del Fondo.
	 * Este servicio consume una optimizacion mediante JDBC
	 * 
	 * @param codigoTransportadora
	 * @param tipoPuntoBanco
	 * @param nombreBanco
	 * @param codigoCiudad
	 * @return Fondos
	 */
	Fondos getCodigoFondoCertificacionJdbc(String codigoTransportadora, String numeroNit, String codigoCiudad);
}
