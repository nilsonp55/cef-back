package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.CentroCiudadPpal;

/**
 * Repository para la entidad Centros_ciudad_ppal
 * @author prv_nparra
 */

public interface ICentroCiudadPpalRepository extends JpaRepository<CentroCiudadPpal, Integer>, QuerydslPredicateExecutor<CentroCiudadPpal> {

}
