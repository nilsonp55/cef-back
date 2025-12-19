package com.ath.adminefectivo.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ath.adminefectivo.dto.PuntosDTO;
import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.CajerosATM;
import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.entities.Oficinas;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.exception.NegocioException;
import com.querydsl.core.types.Predicate;

public interface IPuntosService {

  /**
   * Servicio encargado de consultar la lista de todos los Puntos filtrados con el predicado
   * 
   * @param predicate
   * @return List<PuntosDTO>
   * @author cesar.castano
   */
  Page<PuntosDTO> getPuntos(Predicate predicate, Pageable page, String busqueda);

  /**
   * Servicio encargado de consultar la lista de todos los Puntos filtrados con el predicado
   * 
   * @param predicate
   * @return List<PuntosDTO>
   * @author cesar.castano
   */
  List<PuntosDTO> getPuntos(Predicate predicate);

  /**
   * Servicio encargado de consultar el objeto Punto por codigo y tipo. Este servicio lanza un error
   * en caso de que la ciudad no exista
   * 
   * @param codigoPunto
   * @param tipoPunto
   * @return Puntos
   * @author cesar.castano
   */
  Puntos getEntidadPunto(String tipoPunto, Integer codigoPunto);

  /**
   * Servicio encargado de consultar el nombre del Punto por codigo y tipo. Este servicio lanza un
   * error en caso de que el punto no exista
   * 
   * @param codigoPunto
   * @param tipoPunto
   * @return String
   * @author cesar.castano
   */
  String getNombrePunto(String tipoPunto, Integer codigoPunto);

  /**
   * Servicio encargado de consultar el nombre del Punto por codigo y tipo. Este servicio lanza un
   * error en caso de que el punto no exista
   * 
   * @param codigoPunto
   * @param tipoPunto
   * @return String
   * @author cesar.castano
   */
  String getNombrePunto(Integer codigoPunto);

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
   * 
   * @param punto
   * @return
   */
  Puntos actualizarPunto(Puntos punto);
  
  /**
   * 
   * @param punto
   * @return
   */
  Puntos crearPunto(Puntos punto);
  
  /**
   * Servicio encargado de la persistencia de los puntos para banco
   * 
   * @return Puntos
   * @author Bayron Andres Perez M.
   */
  Puntos crearPuntoBanco(Puntos punto, Bancos banco);

  /**
   * Servicio encargado de la persistencia de los puntos para oficina
   * 
   * @return Puntos
   * @author Bayron Andres Perez M.
   */
  Puntos crearPuntoOficina(Puntos punto, Oficinas oficina);

  /**
   * Servicio encargado de la persistencia de los puntos para cajeroATM
   * 
   * @return Puntos
   * @author Bayron Andres Perez M.
   */
  Puntos crearPuntoCajeroATM(Puntos punto, CajerosATM cajerosATM);

  /**
   * Servicio encargado de la persistencia de los puntos para sitio cliente
   * 
   * @return Puntos
   * @author Bayron Andres Perez M.
   */
  Puntos crearPuntoSitioCliente(Puntos punto, SitiosClientes sitiosClientes);

  /**
   * Servicio encargado de la persistencia de los puntos para fondos
   * 
   * @return Puntos
   * @author Bayron Andres Perez M.
   */
  Puntos crearPuntoFondo(Puntos punto, Fondos fondo);
  
  /**
   * 
   * @param punto
   * @return
   */
  Puntos crearPuntoBanrep(Puntos punto);

  /**
   * Servicio encargado de retornar un punto por
   * 
   * @return List<PuntosDTO>
   * @author cesar.castano
   */
  Puntos getPuntoById(Integer idPunto);

  /**
   * Servicio encargado de consultar el nombre del Punto por codigo y tipo. Este servicio lanza un
   * error en caso de que el punto no exista
   * 
   * @param nombrePunto
   * @param tipoPunto
   * @return Integer
   * @author cesar.castano
   */
  Integer getcodigoPunto(String tipoPunto, String nombrePunto);

  /**
   * Servicio encargado de consultar el objeto Punto por codigo y tipo. Este servicio lanza un error
   * en caso de que la ciudad no exista
   * 
   * @param codigoPunto
   * @param tipoPunto
   * @return Boolean
   * @author cesar.castano
   */
  Boolean getEntidadPuntoBanrep(String tipoPunto, Integer codigoPunto);

  /**
   * Servicio con optimización JDBC encargado de consultar el objeto Punto por codigo y tipo.
   * Este servicio lanza un error en caso de que la ciudad no exista
   * 
   * @param codigoPunto
   * @param tipoPunto
   * @return Boolean
   */
  Boolean getEntidadPuntoBanrepJdbc(String tipoPunto, Integer codigoPunto);

  /**
   * Servicio encargado de consultar el objeto Puntos
   * 
   * @param codigo_banco_aval
   * @return Puntos
   * @author cesar.castano
   */
  Puntos getEntidadPunto(Integer codigoBancoAval);
  
  /**
   * Servicio encargado de consultar el objeto Puntos de la implementación de JDBC optimizado
   * 
   * @param codigo_banco_aval
   * @return Puntos
   */
  Puntos getCodigoPuntoJdbc(Integer codigoBancoAval);

  /**
   * Valida que un punto sea del tipo dado.
   * 
   * @param codigoPunto
   * @param tipo punto
   * @return Puntos
   * @author rafael.parra
   */
  Puntos validarPuntoActualizar(Integer codigoPunto, String tipoPunto);

  /*
   * Generar un estructura de mapa con los regitros de la tabla punto, de puntos tipo oficina.
   * 
   * @return Retorna un map <IdPunto, NombrePunto> de puntos
   * 
   * @author prv_nparra
   */
  HashMap<Integer, String> getPuntosTipoOficina();

  /**
   * 
   * @return
   * @author prv_nparra
   */
  HashMap<Integer, Puntos> getAllPuntos();
  
  /**
   * Eliminar un punto por Id Punto
   * @return void
   * @author prv_nparra
   */
  void eliminarPunto(Integer codigoPunto) throws NegocioException;

	/**
	 * 
	 * @return Puntos
	 */
	Puntos getPuntoByIdJdbc(Integer idPunto);

	/**
	 * 
	 * @param p
	 * @param f
	 * @throws NegocioException
	 * @author prv_nparra
	 */
	void validarPuntoFondoUnique(Puntos p, Fondos f) throws NegocioException;
	
	/**
	 * 
	 * @param p
	 * @param b
	 * @throws NegocioException
	 * @author prv_nparra
	 */
	void validarPuntoBancoUnique(Puntos p, Bancos b) throws NegocioException;
	
	/**
	 * 
	 * @param p
	 * @param c
	 * @throws NegocioException
	 * @author prv_nparra
	 */
	void validarPuntoCajeroUnique(Puntos p, CajerosATM c) throws NegocioException;
	
	/**
	 * 
	 * @param p
	 * @param co
	 * @throws NegocioException
	 * @author prv_nparra
	 */
	void validarPuntoOficinaUnique(Puntos p, Oficinas o) throws NegocioException;
	
	/**
	 * 
	 * @param p
	 * @throws NegocioException
	 * @author prv_nparra
	 */
	void validarPuntoBanrepUnique(Puntos p) throws NegocioException;
	
	/**
	 * 
	 * @param p
	 * @param sc
	 * @throws NegocioException
	 * @author prv_nparra
	 */
	void validarPuntoClienteUnique(Puntos p, SitiosClientes sc) throws NegocioException;
	
	PuntosDTO getPuntoBancoByNombrePuntoTipoPunto(String nombrePunto, String tipoPunto);
}
