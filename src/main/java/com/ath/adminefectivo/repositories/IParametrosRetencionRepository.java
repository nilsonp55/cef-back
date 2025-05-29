package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.ParametrosRetencion;

public interface IParametrosRetencionRepository extends JpaRepository<ParametrosRetencion, Integer>, QuerydslPredicateExecutor<ParametrosRetencion>{

	List<ParametrosRetencion> findConfParametrosRetencionByActivo(boolean activo);
	
}
