package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import com.ath.adminefectivo.entities.CentroCiudad;

public interface ICentroCiudadRepository extends JpaRepository<CentroCiudad, Integer>, QuerydslPredicateExecutor<CentroCiudad> {

}
