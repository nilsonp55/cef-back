package com.ath.adminefectivo.repositories.jdbc;

import com.ath.adminefectivo.entities.Puntos;

public interface IPuntosJdbcRepository {
	
	 /**
     * Encontrar un punto basado en el codigo del banco AVAL, especificamente para puntos
     * enlazada a cliente corporativo con identificacion '9999999999'
     * 
     * Este metodo provee una implementacion de optimizacion de desempeño mediante JDBC
     * 
     * @param codigoAval
     * @return Puntos
     */
    Puntos findPuntoByCodigoAval(Integer codigoAval);
    
    /**
     * Verifica si un puntos existe
     * Este metodo provee una implementacion de optimizacion de desempeño mediante JDBC
     *
     * @param tipoPunto
     * @param codigoPunto
     * @return boolean
     */
    Boolean existsByTipoPuntoAndCodigoPunto(String tipoPunto, Integer codigoPunto);
    
    /**
     * Encuentra una entidad Punto por su código punto using an optimized JDBC query.}
     * Este metodo provee una implementacion de optimizacion de desempeño mediante JDBC
     *
     * @param codigoPunto The unique identifier of the punto to retrieve
     * @return Puntos entity if found, null otherwise
     */
    Puntos findByCodigoPunto(Integer codigoPunto);

}