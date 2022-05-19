package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.PuntosCodigoTDV;

/**
 * Repository encargado de manejar la logica de la entidad PuntosCodigoTDV
 *
 * @author cesar.castano
 */
public interface IPuntosCodigoTDVRepository
		extends JpaRepository<PuntosCodigoTDV, String>, QuerydslPredicateExecutor<PuntosCodigoTDV> {

	/**
	 * Retorna el objeto PuntosCodigoTDV para un codigoTDV
	 * @param codigoTDV
	 * @return PuntosCodigoTDV
	 * @author cesar.castano
	 */
	public PuntosCodigoTDV findByCodigoTDV(String codigoTDV);
}
