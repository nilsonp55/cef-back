package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.ath.adminefectivo.entities.TransaccionesInternas;

public interface ITransaccionesInternasRepository extends JpaRepository<TransaccionesInternas, Long>, 
			QuerydslPredicateExecutor<TransaccionesInternas> {
	
	/**
	 * Retorna una lista de transacciones internas por fecha
	 * @param start
	 * @param end
	 * @return List<TransaccionesInternas>
	 * @author duvan.naranjo
	 */
	List<TransaccionesInternas> findByFechaBetween(Date start, Date end);
	
	/**
	 * Retorna una lista de transacciones internas por fecha
	 * @param start
	 * @param end
	 * @param tipoProceso
	 * @return List<TransaccionesInternas>
	 * @author duvan.naranjo
	 */
	List<TransaccionesInternas> findByFechaBetweenAndTipoProceso(Date start, Date end, String tipoProceso);

	/**
	 * Metodo encargado de ejecutar la funcion de la base de datos para 
	 * realizar el proceso de transacciones contables o movimientos contables
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @param tipoContabilidad
	 * @param estadoContabilidadGenerado
	 * @return duvan.naranjo
	 */
	@Procedure(name = "fnc_transcciones_contables")
	boolean fnc_transcciones_contables(@Param("p_fechaInicio") Date fechaInicio, @Param("p_fechaFin") Date fechaFin, @Param("p_tipoContabilidad") String tipoContabilidad,
			@Param("p_estado") int estadoContabilidadGenerado);

	
	/**
	 * Metodo encargado de realizar la consulta de los errores contables por banco
	 * 
	 * @param fecha
	 * @param tipoContabilidad
	 * @param codBanco
	 * @param i
	 * @return boolean
	 * @author duvan.naranjo
	 */
	@Query(value = "SELECT "
			+ "CASE WHEN count(1) > 0 THEN "
			+ "TRUE "
			+ "ELSE "
			+ "FALSE "
			+ "END "
			+ "FROM "
			+ "transacciones_internas "
			+ "WHERE "
			+ "fecha = ?1 AND "
			+ "estado = ?4 AND "
			+ "tipo_proceso = ?2 AND "
			+ "banco_aval = ?3 ", nativeQuery = true)
	boolean existErroresContablesByBanco(Date fecha, String tipoContabilidad, int codBanco, int i);

	
	/**
	 * Metodo encargado de realizar la consulta de los errores contables para todos los bancos
	 * 
	 * @param fecha
	 * @param tipoContabilidad
	 * @param estado
	 * @return boolean
	 * @author duvan.naranjo
	 */
	@Query(value = "SELECT "
			+ "	CASE WHEN count(1) > 0 THEN "
			+ "TRUE "
			+ "ELSE "
			+ "FALSE "
			+ "END "
			+ "FROM "
			+ "transacciones_internas "
			+ "WHERE "
			+ "fecha = ?1 AND "
			+ "estado = ?3 AND "
			+ "tipo_proceso = ?2 ", nativeQuery = true)
	boolean existErroresContablesAllBanco(Date fecha, String tipoContabilidad, int estado);

}
