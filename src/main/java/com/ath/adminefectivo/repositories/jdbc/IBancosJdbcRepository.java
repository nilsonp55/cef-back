package com.ath.adminefectivo.repositories.jdbc;

import com.ath.adminefectivo.dto.BancosDTO;

public interface IBancosJdbcRepository {
	
	/**
     * Encontrar un banco por su codigoPunto mediante implementacion JDBC directo
     * 
     * Este metodo provee una implementacion de optimizacion de desempeño mediante JDBC
     * 
     * @param codigoPunto
     * @return BancosDTO
     */
    BancosDTO findBancoByCodigoPunto(int codigoPunto);

}