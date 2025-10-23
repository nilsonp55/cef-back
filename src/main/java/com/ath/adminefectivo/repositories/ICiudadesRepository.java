package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

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
	
	
	@Query(value = """
	        SELECT c.codigo_dane 
	        FROM controlefect.puntos p
	        INNER JOIN controlefect.ciudades c ON c.codigo_dane = p.codigo_ciudad
	        WHERE p.nombre_punto = :nombrePunto
	        """, nativeQuery = true)
	    String findCodigoDaneByNombrePunto(@Param("nombrePunto") String nombrePunto);
}