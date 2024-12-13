package com.ath.adminefectivo.repositories.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.ath.adminefectivo.entities.Ciudades;
import com.ath.adminefectivo.repositories.jdbc.ICiudadesJdbcRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CiudadesJdbcRepositoryImpl implements ICiudadesJdbcRepository{
	private static final String FIND_CIUDAD_BY_DANE_SQL = """
            SELECT 
                c.CODIGO_DANE,
                c.NOMBRE_CIUDAD,
                c.NOMBRE_CIUDAD_FISERV,
                c.CODIGO_BRINKS,
                c.COBRO_IVA
            FROM controlefect.CIUDADES c
            WHERE c.CODIGO_DANE = ?
            """;
	
	private static final String FIND_CIUDAD_BY_BRINKS_SQL = """
            SELECT 
                c.CODIGO_DANE,
                c.NOMBRE_CIUDAD,
                c.NOMBRE_CIUDAD_FISERV,
                c.CODIGO_BRINKS,
                c.COBRO_IVA
            FROM controlefect.CIUDADES c
            WHERE c.CODIGO_BRINKS = ?
            """;

    private final DataSource dataSource;

    public CiudadesJdbcRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Ciudades findCiudadByCodigoDane(String codigoDane) {
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_CIUDAD_BY_DANE_SQL)) {
            
            stmt.setString(1, codigoDane);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ciudades ciudad = mapResultSetToEntity(rs);
                    return ciudad;
                }
                
                log.debug("No encontrada la ciudad para codigo DANE: {}", codigoDane);
                return null;
            }
            
        } catch (SQLException e) {
            log.error("Error al consultar ciudad con codigo DANE: {}", codigoDane, e);
            throw new RuntimeException("Error al consultar ciudad", e);
        }
    }
    
    @Override
    public Ciudades findCiudadByCodigoBrinks(Integer codigoBrinks) {
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_CIUDAD_BY_BRINKS_SQL)) {
            
            stmt.setInt(1, codigoBrinks);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ciudades ciudad = mapResultSetToEntity(rs);
                    return ciudad;
                }
                
                log.debug("Ciudad no encontrada para codigo Brinks: {}", codigoBrinks);
                return null;
            }
            
        } catch (SQLException e) {
            log.error("Error al consultar ciudad con codigo Brinks: {}", codigoBrinks, e);
            throw new RuntimeException("Error consultado ciudad por codigo Brinks", e);
        }
    }

    private Ciudades mapResultSetToEntity(ResultSet rs) throws SQLException {
        return Ciudades.builder()
            .codigoDANE(rs.getString("CODIGO_DANE"))
            .nombreCiudad(rs.getString("NOMBRE_CIUDAD"))
            .nombreCiudadFiserv(rs.getString("NOMBRE_CIUDAD_FISERV"))
            .codigoBrinks(rs.getInt("CODIGO_BRINKS"))
            .cobroIva(rs.getBoolean("COBRO_IVA"))
            .build();
    }
}