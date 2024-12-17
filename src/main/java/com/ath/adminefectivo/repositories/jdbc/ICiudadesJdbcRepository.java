package com.ath.adminefectivo.repositories.jdbc;

import com.ath.adminefectivo.entities.Ciudades;

public interface ICiudadesJdbcRepository {
	/**
     * Retorna el objeto Ciudades con base en el codigo DANE
     * Este metodo provee una implementacion de optimizacion de desempeño mediante JDBC
     *
     * @param codigoDane
     * @return Ciudades
     */
    Ciudades findCiudadByCodigoDane (String codigoDane);
    
    
    /**
     * Retorna el objeto Ciudades con base en el codigo Brinks
     * Este metodo provee una implementacion de optimizacion de desempeño mediante JDBC
     *
     * @param codigoBrinks
     * @return Ciudades
     */
    Ciudades findCiudadByCodigoBrinks(Integer codigoBrinks);
}