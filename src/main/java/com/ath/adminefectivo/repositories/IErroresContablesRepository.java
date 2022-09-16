package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.dto.compuestos.ResultadoErroresContablesDTO;
import com.ath.adminefectivo.entities.Dominio;
import com.ath.adminefectivo.entities.ErroresContables;
import com.ath.adminefectivo.entities.TransaccionesInternas;
import com.ath.adminefectivo.entities.id.DominioPK;

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
	public List<ErroresContables> findByFechaBetween(Date start, Date end);

	/**
	 * Consulta la lista de errores contables y lo devuelve en el tipo
	 * ResultadoErroresContablesDTO
	 * 
	 * @return List<ResultadoErroresContablesDTO>
	 * @author duvan.naranjo
	 */
	@Query(nativeQuery = true)
	public List<ResultadoErroresContablesDTO> consultarErroresContables();

//	@Procedure(name = "public.fnc_generar_proceso_contables")
	@Query(value ="SELECT * "
			+ " from fnc_generar_proceso_contables()", nativeQuery = true)
	public List<Long> fnc_generar_proceso_contables();

}
