package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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
	 * Metodo encargado de ejecutar la funcion de la base de datos para 
	 * realizar el proceso de transacciones contables o movimientos contables
	 * @Query("SELECT fnc_transcciones_contables(?1, ?2,  ?3::VARCHAR , ?4)")
	 * @param fechaInicio
	 * @param fechaFin
	 * @param tipoContabilidad
	 * @param estadoContabilidadGenerado
	 * @return duvan.naranjo
	 */
	@Procedure(name = "public.fnc_transcciones_contables")
	boolean fnc_transcciones_contables(@Param("p_fechaInicio") String fechaInicio, @Param("p_fechaFin") String fechaFin, @Param("p_tipoContabilidad") String tipoContabilidad,
			@Param("p_estado") int estadoContabilidadGenerado, @Param("p_formato_fecha") String formatoFecha);

}
