package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

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

	
	@Query("SELECT ct FROM CostosProcesamiento ct " 
	        + " WHERE idArchivoCargado =:idArchivo "
			)
	List<CostosProcesamiento> findByIdArchivoCargado(Long idArchivo);
	
	@Transactional
	@Modifying
    @Query(value = "UPDATE COSTOS_PROCESAMIENTO SET ESTADO_CONCILIACION = :estado WHERE ID_ARCHIVO_CARGADO = :idArchivoCargado", nativeQuery = true)
	void actualizarEstadoByIdArchivoCargado(Long idArchivoCargado, String estado);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM COSTOS_PROCESAMIENTO WHERE ID_ARCHIVO_CARGADO = :idArchivo", nativeQuery = true)
	void eliminarPorIdArchivoCargadoProcesamiento(@Param("idArchivo") Long idArchivo);
	
}
