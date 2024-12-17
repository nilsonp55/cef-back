package com.ath.adminefectivo.repositories.jdbc;

import com.ath.adminefectivo.entities.PuntosCodigoTDV;

import java.util.List;

public interface IPuntosCodigoTDVJdbcRepository {
	
	/**
     * Encontrar un PuntosCodigoTDV por criterios especificos usando implemntacion de JDBC directo
     * 
     * Este metodo provee una implementacion de optimizacion de desempeño mediante JDBC
     * 
     * @param codigoPropioTDV
     * @param codigoTDV
     * @param codigoBanco
     * @param codigoDane
     * @return PuntosCodigoTDV
     */
    PuntosCodigoTDV findByCodigoPropioTDVAndCodigoTDVAndBancosAndCiudadCodigo(String codigoPropioTDV, 
                                 String codigoTDV, 
                                 Integer codigoBanco, 
                                 String codigoDane);
    
    
    /**
     * Encontrar todos los registros PuntosCodigoTDV que emparejan a los criterios especificos
     * 
     * Este metodo provee una implementacion de optimizacion de desempeño mediante JDBC
     * 
     * @param codigoPropioTDV
     * @param codigoTDV
     * @param codigoBanco
     * @return List<PuntosCodigoTDV>
     */
    List<PuntosCodigoTDV> findByCodigoPropioTDVAndCodigoTDVAndBancos(String codigoPropioTDV, 
                                           String codigoTDV, 
                                           Integer codigoBanco);

}