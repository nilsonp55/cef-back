package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.ath.adminefectivo.entities.EstadoConciliacionParametrosLiquidacion;

public interface IEstadoConciliacionParametrosLiquidacionRepository extends JpaRepository<EstadoConciliacionParametrosLiquidacion, Long>,
QuerydslPredicateExecutor<EstadoConciliacionParametrosLiquidacion>, PagingAndSortingRepository<EstadoConciliacionParametrosLiquidacion, Long> {
	
	List<EstadoConciliacionParametrosLiquidacion> findByIdLiquidacionAndEstado(Long idLiquidacion, Integer estado);
	
}
