package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Puntos;

/**
 * Repository encargado de manejar la logica de la entidad Puntos
 *
 * @author cesar.castano
 */
public interface IPuntosRepository extends JpaRepository<Puntos, Integer>, QuerydslPredicateExecutor<Puntos>{

	/**
	 * Retorna el objeto Puntos para tipo de punto y codigo de punto
	 * 
	 * @param codigoPunto
	 * @param tipoPunto
	 * @return Puntos
	 * @author cesar.castano
	 */
	public Puntos findByCodigoPuntoAndTipoPunto(Integer codigoPunto, String tipoPunto);

	/**
	 * Retorna el objeto Puntos consultado por nombrePunto
	 * 
	 * @param nombrePunto
	 * @param tipoPunto
	 * @return Puntos
	 * @author duvan.naranjo
	 */
	public Puntos findByNombrePunto(String nombrePunto);
	
	/**
	 * Retorna el objeto Puntos consultado por nombrePunto
	 * 
	 * @param nombrePunto
	 * @param tipoPunto
	 * @return Puntos
	 * @author duvan.naranjo
	 */
	public Puntos findByTipoPuntoAndCodigoCiudad(String tipoPunto,String codigoCiudad);

	/**
	 * Retorna el objeto Puntos para tipo de punto y nombre de punto
	 * @param nombrePunto
	 * @param tipoPunto
	 * @return Puntos
	 * @author cesar.castano
	 */
	public Puntos findByTipoPuntoAndNombrePunto(String tipoPunto, String nombrePunto);

}
