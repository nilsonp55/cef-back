package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Fondos;

/**
 * Repository encargado de manejar la logica de la entidad Fondos
 *
 * @author cesar.castano
 */
public interface IFondosRepository extends JpaRepository<Fondos, Integer>, QuerydslPredicateExecutor<Fondos>{

	/**
	 * Retorna el objeto Fondos cpn base en el codigo de punto
	 * @param codigoPunto
	 * @return Fondos
	 * @author cesar.castano
	 */
	public Fondos findByCodigoPunto (Integer codigoPunto);
	
	/**
	 * Retorna el objeto Fondos con base en la transportadora
	 * @param tdv
	 * @return Fondos
	 * @author cesar.castano
	 */
	List<Fondos> findByTdvAndBancoAVAL(String tdv, int bancoAval);
	
	
	
	/**
	 * Retorna el objeto Fondos con base en la transportadora, codigo compensacion y codigo ciudad
	 * @param nombreTransportadora
	 * @param codigoCompensacion
	 * @param codigoCiudad
	 * @return Fondos
	 * @author cesar.castano
	 */
	@Query("SELECT f FROM Fondos f JOIN Puntos p ON "
			+ "f.codigoPunto = p.codigoPunto "
			+ "WHERE f.tdv = (SELECT codigo FROM Transportadoras WHERE nombreTransportadora = ?1) and "
			+ "      f.bancoAVAL = (SELECT codigoPunto FROM Bancos WHERE codigoCompensacion = ?2) and "
			+ "      p.codigoCiudad in (SELECT codigoDANE FROM Ciudades WHERE nombreCiudad = ?3)")
	Fondos obtenerCodigoFondoTDV(String nombreTransportadora, Integer codigoCompensacion, String codigoCiudad);
	
	/**
	 * Retorna el objeto Fondos con base en la nombreTransportadora, tipoPuntoBnaco, 
	 * mombreBanco y nombre ciudad
	 * @param nombreTransportadora
	 * @param tipoPuntoBanco
	 * @param mombreBanco
	 * @param nombreCiudad
	 * @return Fondos
	 * @author cesar.castano
	 */
	@Query("SELECT f FROM Fondos f JOIN Puntos p ON "
			+ "f.codigoPunto = p.codigoPunto "
			+ "WHERE f.tdv = (SELECT codigo FROM Transportadoras WHERE nombreTransportadora = ?1) and "
			+ "      f.bancoAVAL = (SELECT codigoPunto FROM Puntos WHERE tipoPunto = ?2 and nombrePunto = ?3) and "
			+ "      p.codigoCiudad = (SELECT codigoDANE FROM Ciudades WHERE nombreCiudad = ?4)")
	Fondos obtenerCodigoFondoTDV1(
			String nombreTransportadora, String tipoPuntoBanco, String nombreBanco, String nombreCiudad);

	/**
	 * Retorna el objeto Fondos con base en la nombreTransportadora, tipoPuntoBnaco, 
	 * mombreBanco y codigo ciudad
	 * @param nombreTransportadora
	 * @param tipoPuntoBanco
	 * @param mombreBanco
	 * @param codigoCiudad
	 * @return Fondos
	 * @author cesar.castano
	 */
	@Query(value = "SELECT "
			+ "f.* "
			+ "FROM "
			+ "fondos f, "
			+ "puntos p "
			+ "WHERE "
			+ "f.codigo_punto = p.codigo_punto AND "
			+ "f.tdv = ?1 and "
			+ "f.banco_aval = (select b.codigo_punto  from bancos b where b.numero_nit = ?2) and "
			+ "p.codigo_ciudad = (select c.codigo_dane  from ciudades c where c.codigo_dane = ?3 or ('BRK' = ?1 and cast(c.codigo_brinks as varchar) = ?3 )) "
			+ "",nativeQuery = true)
	Fondos obtenerCodigoFondoTDV2(String codigoTransportadora, String numeroNit, String codigoCiudad);
}
