package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ath.adminefectivo.entities.CostosProcesamiento;

public interface ICostosProcesamientoRepository extends JpaRepository<CostosProcesamiento, Long>,
QuerydslPredicateExecutor<CostosProcesamiento>, PagingAndSortingRepository<CostosProcesamiento, Long> {
	
	/**
	 * Metodo encargado de realizar la consulta de los registros cargados en costos_procesamiento
	 * @param entidad
	 * @param fechaServicioTransporte
	 * @param codigoPuntoCargo
	 * @param nombrePuntoCargo
	 * @param ciudadFondo
	 * @param nombreTipoServicio
	 * @param estadoConciliacion
	 * @return List<CostosProcesamiento>
	 * @author hector.mercado
	 */
	@Query("SELECT ct FROM CostosProcesamiento ct " 
	        + " WHERE estadoConciliacion =:estadoConciliacion"
			+ " AND entidad =:entidad "
			+ " AND fechaServicioTransporte =:fechaServicioTransporte"
			+ " AND codigoPuntoCargo =:codigoPuntoCargo"
			+ " AND nombrePuntoCargo =:nombrePuntoCargo"
			+ " AND ciudadFondo =:ciudadFondo"
			+ " AND nombreTipoServicio =:nombreTipoServicio"
			)
	List<CostosProcesamiento> findByEstadoEntidadFechaServicio(String entidad,
			Date fechaServicioTransporte,
			String codigoPuntoCargo,
			String nombrePuntoCargo,
			String ciudadFondo,
			String nombreTipoServicio,
			String estadoConciliacion);

}
