package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.ConfContableEntidades;

public interface IConfContableEntidadesRepository extends JpaRepository<ConfContableEntidades, Long>, 
			QuerydslPredicateExecutor<ConfContableEntidades> {

}
