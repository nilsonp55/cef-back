package com.ath.adminefectivo.repositories.jdbc;

import java.util.Date;

import com.ath.adminefectivo.entities.OperacionesCertificadas;

public interface IOperacionesCertificadasJdbcRepository {
	/**
     * Retorna la entidad operaciones certificadas segun parametros
     * Este metodo provee una implementacion de optimizacion de desempeño mediante JDBC
     *
     *
     * @param codigoFondoTDV
     * @param codigoPuntoOrigen
     * @param codigoPuntoDestino
     * @param codigoServicioTdv
     * @param entradaSalida
     * @param fechaEjecucion
     * @param codigoPropioTDV
     * @param idArchivoCargado
     * @return OperacionesCertificadas
     */
    OperacionesCertificadas findOperacionCertificadaByParametros(
        Integer codigoFondoTDV,
        Integer codigoPuntoOrigen,
        Integer codigoPuntoDestino,
        OperacionesCertificadas operacionesCertificadasObj
    );
    
}