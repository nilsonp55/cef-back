package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.dto.compuestos.ResultadoErroresContablesDTO;
import com.ath.adminefectivo.entities.ErroresContables;

/**
 * Repository encargado de manejar la logica de la entidad Errores Contables
 *
 * @author duvan.naranjo
 */
public interface IErroresContablesRepository extends JpaRepository<ErroresContables, Integer>, QuerydslPredicateExecutor<ErroresContables> {

	/**
	 * Consulta la lista de errores contables entre un rango de fechas 
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return List<ErroresContables>
	 * @author duvan.naranjo
	 */
	@Query(value = "SELECT ec.* from errores_contables ec, "
			+ " transacciones_internas ti "
			+ "	where "
			+ "ec.fecha  = ?1 and "
			+ "ec.id_transacciones_internas = ti.id_transacciones_internas and "
			+ "ti.tipo_proceso = ?2 ", nativeQuery = true )
	public List<ErroresContables> findByFechaBetweenAndTipoProceso(Date end, String tipoContabilidad);

	/**
	 * Consulta la lista de errores contables y lo devuelve en el tipo
	 * ResultadoErroresContablesDTO
	 * 
	 * @return List<ResultadoErroresContablesDTO>
	 * @author duvan.naranjo
	 */
	@Query(nativeQuery = true)
	public List<ResultadoErroresContablesDTO> consultarErroresContables();

	@Query(value ="SELECT * "
			+ " from fnc_generar_proceso_contables()", nativeQuery = true)
	public List<Long> fnc_generar_proceso_contables();

	/**
	 * Metodo encargado de consultar los errores existentes para la 
	 * transaccion interna recibida
	 * 
	 * @param transaccionesInternas
	 * @return List<ErroresContables>
	 */
	@Query(value = "SELECT * "
			+ "FROM "
			+ "	errores_contables ec "
			+ "WHERE "
			+ "ec.id_transacciones_internas = ?1 ", nativeQuery = true )
	public List<ErroresContables> findByTransaccionInterna(long idTransaccionesInternas);

}
