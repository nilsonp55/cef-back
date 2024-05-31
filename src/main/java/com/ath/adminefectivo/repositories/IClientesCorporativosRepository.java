package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.ClientesCorporativos;

public interface IClientesCorporativosRepository extends JpaRepository<ClientesCorporativos, Integer>, 
													QuerydslPredicateExecutor<ClientesCorporativos>{

	/**
	 * Metodo que retorna el codigo del cliente con base en el Nit
	 * @param nit
	 * @return ClientesCorporativos
	 * @author cesar.castano
	 */
	ClientesCorporativos findByCodigoBancoAvalAndIdentificacion(Integer codigoBancoAval, String identificacion);

	/**
	 * Metodo que retorna el objeto cliente con base en el codigo punto
	 * @param nit
	 * @return ClientesCorporativos
	 * @author cesar.castano
	 */
	ClientesCorporativos findByCodigoCliente(Integer codigoCliente);
}
