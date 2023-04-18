package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.LogProcesoMensual;

/**
 * Repository encargado de manejar la logica de la entidad LogProcesoMensual
 *
 * @author duvan.naranjo
 */
public interface LogProcesoMensualRepository extends JpaRepository<LogProcesoMensual, Long>, QuerydslPredicateExecutor<LogProcesoMensual> {

	
	/**
	 * Retorna la entidad para un codigo de proceso
	 * 
	 * @param codigoProceso
	 * @return LogProcesoMensual
	 * @author duvan.naranjo
	 */
	LogProcesoMensual findByCodigoProceso(String codigoProceso);
	
	/**
	 * Retorna lista de logsProcesoMensual por fecha
	 * 
	 * @param fechaProceso
	 * @return LogProcesoMensual
	 * @author duvan.naranjo
	 */
	List<LogProcesoMensual> findByFechaCreacion(Date fecha);

	/**
	 * Metodo encargado de obtener un log proceso diario con el estado y codigo 
	 * del proceso
	 * 
	 * @param codigoProceso
	 * @param estado
	 * @return List<LogProcesoMensual>
	 * @author duvan.naranjo
	 */
	List<LogProcesoMensual> findByCodigoProcesoAndEstado(String codigoProceso, String estado);

	/**
	 * Metodo encargado de realizar la validacion logica segun el estado y fechas enviadas
	 * valida si existen o no registros que cumplan con los valores enviados 
	 * 
	 * @param estadoProcesoDiaPendiente
	 * @param year
	 * @param month
	 * @return boolean
	 * @author duvan.naranjo
	 */
	@Query(value = "SELECT "
			+ "	CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END  "
			+ "FROM "
			+ "	log_proceso_diario "
			+ "WHERE "
			+ "	estado_proceso  = ?1 AND "
			+ "	EXTRACT(YEAR FROM fecha_finalizacion) = ?2 AND "
			+ "	EXTRACT(MONTH FROM fecha_finalizacion) = ?3 ", nativeQuery = true)
	boolean existenLogParaMesAnioYEstado(String estadoProcesoDiaPendiente, int year, int month);

	/**
	 * Metodo encargado de validar si existen costos clasificacion para 
	 * el mes y anio enviado
	 * 
	 * @param mesAnio
	 * @return boolean
	 * @author duvan.naranjo
	 */
	@Query(value = "SELECT "
			+ "	CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END  "
			+ "FROM "
			+ "	costos_clasificacion "
			+ "WHERE mes_año = ?1 ",nativeQuery = true)
	boolean existenCostosClasificacionPorMesAnio(String mesAnio);
	
	/**
	 * Metodo encargado de validar si existen costos clasificacion para 
	 * el mes y anio enviado
	 * 
	 * @param mesAnio
	 * @return boolean
	 * @author duvan.naranjo
	 */
	@Query(value = "SELECT "
			+ "	CASE WHEN COUNT(1) > 0 THEN TRUE ELSE FALSE END  "
			+ "FROM "
			+ "	parametros_liquidacion_costo plc "
			+ "WHERE fecha_concilia = ?1 ",nativeQuery = true)
	boolean existenLiquidacionCostoDiariaByFecha(Date fecha);
	
}
