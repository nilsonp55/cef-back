package com.ath.adminefectivo.repositories;

import java.util.List;

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
	 * Retorna el objeto Ciudades con base en el nombre
	 * @param nombre
	 * @return Ciudades
	 * @author cesar.castano
	 */
	public Ciudades findByNombreCiudad(String nombre);
	
	/**
	 * Retorna el objeto Ciudades con base en el codigo DANE
	 * 
	 * @param codigo
	 * @return Ciudades
	 * @author duvan.naranjo
	 */
	public Ciudades findBycodigoDANE(String codigo);
	
	/**
	 * Retorna el objeto Ciudades con base en el nombre Fiserv
	 * 
	 * @param nombreCiudadFiserv
	 * @return Ciudades
	 * @author rafael.parra
	 */
	public Ciudades findByNombreCiudadFiserv(String nombreCiudadFiserv);

	/**
	 * Retorna el objeto Ciudades con base en el codigo Brinks
	 * 
	 * @param codigo
	 * @return Ciudades
	 * @author duvan.naranjo
	 */
	public Ciudades findByCodigoBrinks(Integer codigo);
	
	/**
	 * Retorna el objeto Ciudades ordenado por nombre de ciudad
	 * 
	 * @param codigo
	 * @return Ciudades
	 * @author duvan.naranjo
	 */
	public List<Ciudades> findAllByOrderByNombreCiudadAsc();
}