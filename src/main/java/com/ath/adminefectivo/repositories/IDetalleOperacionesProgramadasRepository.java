package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.DetalleOperacionesProgramadas;

public interface IDetalleOperacionesProgramadasRepository extends JpaRepository<DetalleOperacionesProgramadas, Integer>,
																	QuerydslPredicateExecutor<DetalleOperacionesProgramadas> {

	@Query("SELECT SUM(dop.valorDetalle) FROM DetalleOperacionesProgramadas dop "
		 + "WHERE dop.operacionesProgramadas.idOperacion = ?1")
	Double valorTotal(Integer idOperacion);

//	List<DetalleOperacionesProgramadas> findByidOperacion(Integer idOperacion);
}
