package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Ciudades;

/**
 * Repository encargado de manejar la logica de la entidad Ciudades
 *
 * @author cesar.castano
 */
public interface ICiudadesRepository extends JpaRepository<Ciudades, String>, QuerydslPredicateExecutor<Ciudades> {

	/**
	 * Retorna el objeto Ciudades con base en el codigo DANE
	 * @param codigoCiudad
	 * @return Ciudades
	 * @author cesar.castano
	 */
	public Ciudades findBycodigoDANE (String codigoCiudad);
}
