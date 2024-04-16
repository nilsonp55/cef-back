package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.entities.CostosTransporte;
import com.ath.adminefectivo.entities.OperacionesLiquidacionTransporteEntity;

public interface ICostosTransporteRepository extends JpaRepository<CostosTransporte, Long>,
QuerydslPredicateExecutor<CostosTransporte>, PagingAndSortingRepository<CostosTransporte, Long> {
	
	
	/**
	 * Metodo encargado de realizar la consulta de los registros conciliados
	 * desde la tabla de costos_transporte 
	 * 
	 * @param entidad
	 * @return boolean
	 * @author hector.mercado
	 */
	@Query(value = "SELECT ct.* "
			+ "FROM "
			+ "COSTOS_TRANSPORTE ct "
			+ "WHERE "
			+ " (:entidad is null or ct.entidad = cast(:entidad AS text)) "
			+ " AND (:identificacion is null or ct.identificacion_cliente = cast(:identificacion AS text)) ", nativeQuery = true)
	List<CostosTransporte> conciliadas(@Param("entidad") String entidad, @Param("identificacion") String identificacion);
	
	/**
	 * Metodo encargado de realizar la consulta de los registros cargados en costos_transporte
	 * @param entidad
	 * @param fechaServicioTransporte
	 * @param codigoPuntoCargo
	 * @param nombrePuntoCargo
	 * @param ciudadFondo
	 * @param nombreTipoServicio
	 * @param estadoConciliacion
	 * @return List<CostosTransporte>
	 * @author hector.mercado
	 */
	@Query("SELECT ct FROM CostosTransporte ct " 
	        + " WHERE estadoConciliacion =:estadoConciliacion"
			+ " AND entidad =:entidad "
			+ " AND fechaServicioTransporte =:fechaServicioTransporte"
			+ " AND codigoPuntoCargo =:codigoPuntoCargo"
			+ " AND nombrePuntoCargo =:nombrePuntoCargo"
			+ " AND ciudadFondo =:ciudadFondo"
			+ " AND nombreTipoServicio =:nombreTipoServicio"
			)
	List<CostosTransporte> findByEstadoEntidadFechaServicio(String entidad,
			Date fechaServicioTransporte,
			String codigoPuntoCargo,
			String nombrePuntoCargo,
			String ciudadFondo,
			String nombreTipoServicio,
			String estadoConciliacion);
}

