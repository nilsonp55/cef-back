package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.OperacionesProgramadas;
import com.ath.adminefectivo.entities.TransaccionesContables;
import com.ath.adminefectivo.entities.TransaccionesInternas;

public interface ITransaccionesContablesRepository extends JpaRepository<TransaccionesContables, Long>, 
			QuerydslPredicateExecutor<TransaccionesContables> {
	
	/**
	 * Retorna una lista de transacciones contables por fecha
	 * @param start
	 * @param end
	 * @return List<TransaccionesContables>
	 * @author duvan.naranjo
	 */
	List<TransaccionesContables> findByFechaBetween(Date start, Date end);

}
