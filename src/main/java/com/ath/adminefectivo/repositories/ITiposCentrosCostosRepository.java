package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.TiposCentrosCostos;

public interface ITiposCentrosCostosRepository extends JpaRepository<TiposCentrosCostos, String>, 
		QuerydslPredicateExecutor<TiposCentrosCostos> {

}
