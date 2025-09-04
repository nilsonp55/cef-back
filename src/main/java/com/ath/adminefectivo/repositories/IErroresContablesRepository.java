package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.dto.compuestos.ResultadoErroresContablesDTO;
import com.ath.adminefectivo.entities.ErroresContables;
import com.ath.adminefectivo.dto.ErroresContablesConsultaDTO;

/**
 * Repository encargado de manejar la logica de la entidad Errores Contables
 *
 * @author duvan.naranjo
 */
public interface IErroresContablesRepository extends JpaRepository<ErroresContables, Integer>, QuerydslPredicateExecutor<ErroresContables> {

	/**
	 * Consulta la lista de errores contables entre un rango de fechas 
	 * 
	 * @param fechaFin
	 * @param tipoContabilidad
	 * @return List<ErroresContablesConsultaDTO>
	 * @author jose.pabon
	 */
	@Query(value = "SELECT new com.ath.adminefectivo.dto.ErroresContablesConsultaDTO(ec.idErroresContables, ti.idTransaccionesInternas, ec.fecha, ec.mensajeError, ec.estado) "
			+ "from ErroresContables ec, "
			+ " TransaccionesInternas ti "
			+ "where "
			+ "ec.fecha  = ?1 and "
			+ "ec.transaccionInterna.idTransaccionesInternas = ti.idTransaccionesInternas and "
			+ "ti.tipoProceso = ?2 ")
	public List<ErroresContablesConsultaDTO> findByFechaBetweenAndTipoProcesoNotRelation(Date end, String tipoContabilidad);

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
	public List<Long> fncGenerarProcesoContables();

	/**
	 * Metodo encargado de consultar los errores existentes para la 
	 * transaccion interna recibida
	 * 
	 * @param transaccionesInternas
	 * @return List<ErroresContables>
	 */
	@Query(value = "SELECT * "
			+ "FROM "
			+ "errores_contables ec "
			+ "WHERE "
			+ "ec.id_transacciones_internas = ?1 ", nativeQuery = true )
	public List<ErroresContables> findByTransaccionInterna(long idTransaccionesInternas);

}
