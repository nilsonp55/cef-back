package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.SitiosClientes;

public interface ISitiosClientesRepository extends JpaRepository<SitiosClientes, Integer>, QuerydslPredicateExecutor<SitiosClientes> {
	
	/**
	 * Retorna el objeto SitiosClientes con base en el codigo del punto
	 * @param codigoCliente
	 * @return SitiosClientes
	 * @author cesar.castano
	 */
	SitiosClientes findByCodigoPunto(Integer codigoPunto);

}
