package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.LogProcesoDiario;

/**
 * Repository encargado de manejar la logica de la entidad LogProcesoDiario
 *
 * @author CamiloBenavides
 */
public interface LogProcesoDiarioRepository extends JpaRepository<LogProcesoDiario, Long>, QuerydslPredicateExecutor<LogProcesoDiario> {

	

	/**
	 * Retorna el numero de procesos para una fecha y un estado en particular
	 * 
	 * @param fecha
	 * @param estadoProceso
	 * @return Integer
	 * @author CamiloBenavides
	 */
	Integer countByFechaFinalizacionAndEstadoProceso(Date fecha,  String estadoProceso);
	
	/**
	 * Retorna la entidad para un codigo de pro ceso
	 * 
	 * @param codigoProceso
	 * @return LogProcesoDiario
	 * @author cesar.castano
	 */
	LogProcesoDiario findByCodigoProcesoAndFechaCreacion(String codigoProceso, Date fecha);
	
	/**
	 * Retorna lista de logsProcesoDiario por fecha
	 * 
	 * @param fechaProceso
	 * @return LogProcesoDiarios
	 * @author bayronperez
	 */
	List<LogProcesoDiario> findByFechaCreacion(Date fecha);
	
}
