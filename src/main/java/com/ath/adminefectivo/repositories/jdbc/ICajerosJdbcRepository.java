package com.ath.adminefectivo.repositories.jdbc;

public interface ICajerosJdbcRepository {
	
	 /**
     * Verifica si existe un Cajero para un  codigo punto usando una consulta JDBC optimizada.
     *
     * @param codigoPunto
     * @return boolean
     */
    boolean existsByCodigoPunto(Integer codigoPunto);

}