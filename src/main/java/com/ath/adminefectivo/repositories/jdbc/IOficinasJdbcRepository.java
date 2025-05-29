package com.ath.adminefectivo.repositories.jdbc;

public interface IOficinasJdbcRepository {
	/**
     * Checquea que una oficina exista para un codigo punto
     * 
     * Este metodo provee una implementacion de optimizacion de desempeño mediante JDBC
     * 
     * @param codigoPunto
     * @return boolean
     */
    boolean existsByCodigoPunto(Integer codigoPunto);
}