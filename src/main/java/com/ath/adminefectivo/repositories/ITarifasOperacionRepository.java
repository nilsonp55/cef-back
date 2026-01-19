package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.ath.adminefectivo.entities.TarifasOperacion;

/**
 * Repository encargado de manejar la logica de la entidad tarifas operacion
 *
 * @author duvan.naranjo
 */
public interface ITarifasOperacionRepository extends JpaRepository<TarifasOperacion, Long>, QuerydslPredicateExecutor<TarifasOperacion> {

	@Query(value ="SELECT x.* FROM tarifas_operacion x "
			+ "WHERE x.fajado IS NULL AND "
			+ "x.comision_aplicar IN ('CLASIFICACION DETERIORADO','CLASIFICACION FAJADO','CLASIFICACION MONEDA') AND "
			+ "codigo_banco = ?1 AND "
			+ "codigo_tdv = ?2",nativeQuery=true)
	List<TarifasOperacion> findByBancoAndTransportadoraAndComisionAndFajado(int codigoBanco, String codigoTdv);

	@Query(value = "SELECT * FROM tarifas_operacion x "
            + "WHERE x.fecha_vigencia_ini >= ?1 "
            + "AND x.fecha_vigencia_fin <= ?2", nativeQuery = true)
    List<TarifasOperacion> findByFechaVigenciaBetween(Date fechaInicio, Date fechaFin);
	
	@Query(value = "SELECT * FROM tarifas_operacion x " 
			+ "WHERE x.fecha_vigencia_ini <= :fechaFin "
			+ "AND x.fecha_vigencia_fin >= :fechaInicio", nativeQuery = true)
	List<TarifasOperacion> findByFechaVigencia(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

}
