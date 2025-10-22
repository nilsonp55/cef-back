package com.ath.adminefectivo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import com.ath.adminefectivo.dto.PuntoCiudadesDTO;
import com.ath.adminefectivo.entities.SitiosClientes;

public interface ISitiosClientesRepository extends JpaRepository<SitiosClientes, Integer>, QuerydslPredicateExecutor<SitiosClientes> {
	
	/**
	 * Retorna el objeto SitiosClientes con base en el codigo del punto
	 * @param codigoCliente
	 * @return SitiosClientes
	 * @author cesar.castano
	 */
	SitiosClientes findByCodigoPunto(Integer codigoPunto);
	
	/**
	 * Retorna los puntos asociados a un cliente utilizando el Codigo Cliente como parametro de entrada
	 * @param codigoCliente
	 * @return List<PuntoCiudadesDTO>
	 * @author jose.pabon
	 */
	@Query(
		    value = "SELECT " +
		            "c.CODIGO_DANE AS codigo_dane, " +
		            "c.NOMBRE_CIUDAD AS nombre_ciudad, " +
		            "CONCAT(c.NOMBRE_CIUDAD, '-', c.CODIGO_DANE) AS codigo_nombre_ciudad, " +
		            "p.CODIGO_PUNTO AS codigo_punto, " +
		            "p.NOMBRE_PUNTO AS nombre_punto, " +
		            "CONCAT(p.NOMBRE_PUNTO, '-', cc.IDENTIFICACION, '-', p.CODIGO_PUNTO) AS puntos_cliente " +
		            "FROM SITIOS_CLIENTE sc " +
		            "INNER JOIN PUNTOS p ON sc.CODIGO_PUNTO = p.CODIGO_PUNTO " +
		            "INNER JOIN CIUDADES c ON p.CODIGO_CIUDAD = c.CODIGO_DANE " +
		            "INNER JOIN CLIENTES_CORPORATIVOS cc ON cc.CODIGO_CLIENTE = sc.CODIGO_CLIENTE " +
		            "WHERE sc.CODIGO_CLIENTE = :codigoCliente "+ 
		            "ORDER BY c.NOMBRE_CIUDAD ASC",
		    nativeQuery = true
		)
		List<Object[]> findPuntoAsociadosClientes(@Param("codigoCliente") Integer codigoCliente);

}
