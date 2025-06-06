package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.ErroresCostos;

/**
 * Repository encargado de manejar la logica de la entidad Dominio
 * @author bayron.perez
 */

public interface ErroresCostosRepository extends JpaRepository<ErroresCostos, Integer>, 
		QuerydslPredicateExecutor<ErroresCostos> {

	List<ErroresCostos> findBySeqGrupo(Integer idSeqGrupo);
}
