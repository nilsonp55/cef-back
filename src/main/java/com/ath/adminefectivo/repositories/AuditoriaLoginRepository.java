package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.AuditoriaLogin;

public interface AuditoriaLoginRepository extends JpaRepository<AuditoriaLogin, Long>, QuerydslPredicateExecutor<AuditoriaLogin> {

}
