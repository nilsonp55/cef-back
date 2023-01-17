package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.TiposCuentas;

public interface ITiposCuentasRepository extends JpaRepository<TiposCuentas, String>, 
		QuerydslPredicateExecutor<TiposCuentas> {

}
