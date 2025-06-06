package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.ath.adminefectivo.entities.CostosTransporte;

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
	        + " WHERE estadoConciliacionTransporte =:estadoConciliacionTransporte"
			+ " AND entidadTransporte =:entidadTransporte "
			+ " AND fechaServicioTransporte =:fechaServicioTransporte"
			+ " AND codigoPuntoCargoTransporte =:codigoPuntoCargoTransporte"
			+ " AND nombrePuntoCargoTransporte =:nombrePuntoCargoTransporte"
			+ " AND ciudadFondoTransporte =:ciudadFondoTransporte"
			+ " AND nombreTipoServicioTransporte =:nombreTipoServicioTransporte"
			)
	List<CostosTransporte> findByEstadoEntidadFechaServicio(String entidadTransporte,
			Date fechaServicioTransporte,
			String codigoPuntoCargoTransporte,
			String nombrePuntoCargoTransporte,
			String ciudadFondoTransporte,
			String nombreTipoServicioTransporte,
			String estadoConciliacionTransporte);

	@Query("SELECT ct FROM CostosTransporte ct " 
	        + " WHERE idArchivoCargadoTransporte =:idArchivo "
			)
	List<CostosTransporte> findByIdArchivoCargado(Long idArchivo);
	
	@Transactional
	@Modifying
    @Query(value = "UPDATE COSTOS_TRANSPORTE SET ESTADO_CONCILIACION =:estado WHERE ID_ARCHIVO_CARGADO =:idArchivoCargado", nativeQuery = true)
	void actualizarEstadoByIdArchivoCargado(Long idArchivoCargado, String estado);
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM COSTOS_TRANSPORTE WHERE ID_ARCHIVO_CARGADO = :idArchivo", nativeQuery = true)
	void eliminarPorIdArchivoCargado(@Param("idArchivo") Long idArchivo);
}

