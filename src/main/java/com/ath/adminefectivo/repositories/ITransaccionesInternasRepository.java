package com.ath.adminefectivo.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.OperacionesProgramadas;
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

}
