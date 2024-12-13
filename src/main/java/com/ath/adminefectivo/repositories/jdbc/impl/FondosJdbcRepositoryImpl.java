package com.ath.adminefectivo.repositories.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import com.ath.adminefectivo.entities.Fondos;
import com.ath.adminefectivo.repositories.jdbc.IFondosJdbcRepository;

@Slf4j
@Repository
public class FondosJdbcRepositoryImpl  implements IFondosJdbcRepository {
    
    private static final String FIND_FONDO_SQL = """
            SELECT f.* 
            FROM controlefect.fondos f
            INNER JOIN controlefect.puntos p ON f.codigo_punto = p.codigo_punto 
            WHERE f.tdv = ? 
                AND f.banco_aval = (
                    SELECT b.codigo_punto 
                    FROM controlefect.bancos b 
                    WHERE b.numero_nit = ?
                ) 
                AND p.codigo_ciudad = (
                    SELECT c.codigo_dane 
                    FROM controlefect.ciudades c 
                    WHERE c.codigo_dane = ? 
                        OR ('BRK' = ? AND CAST(c.codigo_brinks AS VARCHAR) = ?)
                )
            """;

    private final DataSource dataSource;

    public FondosJdbcRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Fondos findFondoByTransportadoraAndNitAndCiudad(
            String codigoTransportadora, 
            String numeroNit, 
            String codigoCiudad) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_FONDO_SQL)) {
            
            stmt.setString(1, codigoTransportadora);
            stmt.setString(2, numeroNit);
            stmt.setString(3, codigoCiudad);
            stmt.setString(4, codigoTransportadora);
            stmt.setString(5, codigoCiudad);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            log.error("Error consultando fondo - Transportadora: {}, NIT: {}", 
                     codigoTransportadora, numeroNit, e);
            throw new RuntimeException("Error consultando fondo", e);
        }
    }

    private Fondos mapResultSetToEntity(ResultSet rs) throws SQLException {
        return Fondos.builder()
            .codigoPunto(rs.getInt("codigo_punto"))
            .tdv(rs.getString("tdv"))
            .bancoAVAL(rs.getInt("banco_aval"))
            .nombreFondo(rs.getString("nombre_fondo"))
            .puntos(null)
            .build();
    }
}