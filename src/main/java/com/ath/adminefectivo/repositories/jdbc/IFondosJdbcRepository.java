package com.ath.adminefectivo.repositories.jdbc;

import com.ath.adminefectivo.entities.Fondos;

public interface IFondosJdbcRepository {
	/**
     * Retorna el objeto Fondos con base en los parametros especificados
     *
     * @param codigoTransportadora
     * @param numeroNit
     * @param codigoCiudad
     * @return Fondos
     */
    Fondos findFondoByTransportadoraAndNitAndCiudad(
        String codigoTransportadora, 
        String numeroNit, 
        String codigoCiudad
    );
}