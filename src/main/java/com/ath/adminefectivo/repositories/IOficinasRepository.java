package com.ath.adminefectivo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Oficinas;
/**
 * Repository encargado de manejar la logica de la entidad Oficinas
 *
 * @author cesar.castano
 */
public interface IOficinasRepository extends JpaRepository<Oficinas, Integer>, QuerydslPredicateExecutor<Oficinas> {
	
	/**
	 * Retorna el objeto Oficinas con base en el codigoOficina y codigoBancoAVAL
	 * @param codigoOficina
	 * @param bancoAVAL
	 * @return Oficinas
	 */
	Oficinas findByCodigoOficinaAndBancoAval(Integer codigoOficina, Integer bancoAVAL);

	/**
	 * Retorna el objeto Oficinas con base en el codigoPunto
	 * @param codigoPunto
	 * @param bancoAVAL
	 * @return Oficinas
	 */
	Oficinas findByCodigoPunto(Integer codigoPunto);

}
