package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.TasasCambio;
import com.ath.adminefectivo.entities.TasasCambioPK;

public interface TasasCambioRepository 
		extends JpaRepository<TasasCambio, TasasCambioPK>, QuerydslPredicateExecutor<TasasCambio>{

}
