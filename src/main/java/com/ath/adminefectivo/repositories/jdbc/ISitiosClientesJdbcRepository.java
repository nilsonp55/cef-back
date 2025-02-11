package com.ath.adminefectivo.repositories.jdbc;

import com.ath.adminefectivo.entities.SitiosClientes;

public interface ISitiosClientesJdbcRepository {
	
	 /**
     * Busca un SitiosClientes por su código de punto usando una consulta JDBC optimizada.
     *
     * @param codigoPunto
     * @return SitiosClientes
     */
    SitiosClientes findByCodigoPunto(Integer codigoPunto);

}