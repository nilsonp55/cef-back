package com.ath.adminefectivo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.ath.adminefectivo.entities.Bancos;
import com.ath.adminefectivo.entities.PuntosCodigoTDV;

/**
 * Repository encargado de manejar la logica de la entidad PuntosCodigoTDV
 *
 * @author cesar.castano
 */
public interface IPuntosCodigoTDVRepository
		extends JpaRepository<PuntosCodigoTDV, Integer>, QuerydslPredicateExecutor<PuntosCodigoTDV> {

	/**
	 * Retorna el objeto PuntosCodigoTDV para un codigoTDV
	 * @param codigoTDV
	 * @return PuntosCodigoTDV
	 * @author cesar.castano
	 */
	public PuntosCodigoTDV findByCodigoTDV(String codigoTDV);
	
	/**
	 * Retorna el objeto PuntosCodigoTDV para un codigoPropioTDV
	 * @param codigoPropioTdv
	 * @return
	 */
	public PuntosCodigoTDV findByCodigoPropioTDVAndCodigoTDV(String codigoPropioTdv, String codigoTdv);
	
	/**
	 * Retorna el objeto PuntosCodigoTDV para un codigoPropioTDV
	 * @param codigoPropioTdv
	 * @return
	 */
	public List<PuntosCodigoTDV> findByCodigoPropioTDVAndCodigoTDVAndBancos(String codigoPropioTdv, String codigoTdv, Bancos banco);
	
	/**
	 * Retorna el objeto PuntosCodigoTDV para un codigoPropioTDV
	 * @param codigoPropioTdv
	 * @return
	 */
	public PuntosCodigoTDV findByCodigoPropioTDVAndCodigoTDVAndBancosAndCiudadFondo(String codigoPropioTdv, String codigoTdv, Bancos banco, String ciudadFondo);

}
