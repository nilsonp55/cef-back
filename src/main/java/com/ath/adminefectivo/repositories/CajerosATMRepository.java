package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.CajerosATM;

public interface CajerosATMRepository extends JpaRepository<CajerosATM, Integer>, QuerydslPredicateExecutor<CajerosATM> {

}
