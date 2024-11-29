package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.CostoBolsaMonedaAdicional;


/**
 * Repository encargado de manejar la logica de la entidad costo_bolsa_moneda_adicional
 *
 * @author duvan.naranjo
 */
public interface ICostoBolsaMonedaAdicionalRepository extends JpaRepository<CostoBolsaMonedaAdicional, Integer>, QuerydslPredicateExecutor<CostoBolsaMonedaAdicional> {

	@Query(value ="SELECT x.* FROM costo_bolsa_moneda_adicional x "
			+ "WHERE codigo_tdv = :codigoTdv and estado = 1 ",nativeQuery=true)
	List<CostoBolsaMonedaAdicional> findByTransportadora(String codigoTdv);


}
