package com.ath.adminefectivo.repositories.jdbc;

public interface IClientesCorporativosJdbcRepository {
	
	 /**
     * Verifica si un fliente corporativo existe  por su codigoCliente.
     * Este metodo provee una implementacion de optimizacion de desempeño mediante JDBC
     * 
     *
     * @param codigoCliente
     * @return boolean
     */
    boolean existsByCodigoCliente(Integer codigoCliente);
}