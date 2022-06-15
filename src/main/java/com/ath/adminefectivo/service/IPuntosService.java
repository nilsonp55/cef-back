package com.ath.adminefectivo.service;

import java.util.List;

import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CajerosATM;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.Oficinas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.querydsl.core.types.Predicate;

public interface IPuntosService {

	/**
	 * Servicio encargado de consultar la lista de todos los Puntos filtrados
	 * con el predicado
	 * 
	 * @param predicate
	 * @return List<PuntosDTO>
	 * @author cesar.castano
	 */
	List<PuntosDTO> getPuntos(Predicate predicate);

	/**
	 * Servicio encargado de consultar el objeto Punto por codigo y tipo. Este
	 * servicio lanza un error en caso de que la ciudad no exista
	 * 
	 * @param codigoPunto
	 * @param tipoPunto
	 * @return Puntos
	 * @author cesar.castano
	 */
	Puntos getEntidadPunto(String tipoPunto, Integer codigoPunto);
	
	/**
	 * Servicio encargado de consultar el nombre del Punto por codigo y tipo. Este
	 * servicio lanza un error en caso de que el punto no exista
	 * 
	 * @param codigoPunto
	 * @param tipoPunto
	 * @return String
	 * @author cesar.castano
	 */
	String getNombrePunto(String tipoPunto, Integer codigoPunto);
	
	/**
	 * Servicio encargado de consultar el punto por nombre del punto
	 * 
	 * @param nombrePunto
	 * @return PuntosDTO
	 * @author duvan.naranjo
	 */
	PuntosDTO getPuntoByNombrePunto(String nombrePunto);

	/**
	 * Servicio encargado de consultar el punto por tipoPunto y CodigoCiudad
	 * 
	 * @param nombrePunto
	 * @param codigoCiudad
	 * @return PuntosDTO
	 * @author duvan.naranjo
	 */
	PuntosDTO getPuntoByTipoPuntoAndCodigoCiudad(String tipoPunto, String codigoCiudad);
	/**
	 * Servicio encargado de la persistencia de los puntos para banco
	 * 
	 * @return Puntos
	 * @author Bayron Andres Perez M.
	 */
	Puntos guardarPuntoBanco(Puntos punto, Bancos banco);
	
	/**
	 * Servicio encargado de la persistencia de los puntos para oficina
	 * 
	 * @return Puntos
	 * @author Bayron Andres Perez M.
	 */
	Puntos guardarPuntoOficina(Puntos punto, Oficinas oficina);
	
	/**
	 * Servicio encargado de la persistencia de los puntos para cajeroATM
	 * 
	 * @return Puntos
	 * @author Bayron Andres Perez M.
	 */
	Puntos guardarPuntoCajeroATM(Puntos punto, CajerosATM cajerosATM);
	
	/**
	 * Servicio encargado de la persistencia de los puntos para sitio cliente
	 * 
	 * @return Puntos
	 * @author Bayron Andres Perez M.
	 */
	Puntos guardarPuntoSitioCliente(Puntos punto, SitiosClientes sitiosClientes);
	
	/**
	 * Servicio encargado de la persistencia de los puntos para fondos
	 * 
	 * @return Puntos
	 * @author Bayron Andres Perez M.
	 */
	Puntos guardarPuntoFondo(Puntos punto, Fondos fondo);
	
	/**
	 * Servicio encargado de retornar un punto por
	 * 
	 * @return List<PuntosDTO>
	 * @author cesar.castano
	 */
	Puntos getPuntoById(Integer idPunto);
	
		/**
	 * Servicio encargado de consultar el nombre del Punto por codigo y tipo. Este
	 * servicio lanza un error en caso de que el punto no exista
	 * 
	 * @param nombrePunto
	 * @param tipoPunto
	 * @return Integer
	 * @author cesar.castano
	 */
	Integer getcodigoPunto(String tipoPunto, String nombrePunto);
	
	/**
	 * Servicio encargado de consultar el objeto Punto por codigo y tipo. Este
	 * servicio lanza un error en caso de que la ciudad no exista
	 * 
	 * @param codigoPunto
	 * @param tipoPunto
	 * @return Boolean
	 * @author cesar.castano
	 */
	Boolean getEntidadPuntoBanrep(String tipoPunto, Integer codigoPunto);
	
}
