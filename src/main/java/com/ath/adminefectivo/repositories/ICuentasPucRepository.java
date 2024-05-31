package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.CuentasPuc;

public interface ICuentasPucRepository extends JpaRepository<CuentasPuc, Long>, 
			QuerydslPredicateExecutor<CuentasPuc> {

}
