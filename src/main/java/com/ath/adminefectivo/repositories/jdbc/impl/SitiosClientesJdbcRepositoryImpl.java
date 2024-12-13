package com.ath.adminefectivo.repositories.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import com.ath.adminefectivo.entities.SitiosClientes;
import com.ath.adminefectivo.repositories.jdbc.ISitiosClientesJdbcRepository;

@Slf4j
@Repository
public class SitiosClientesJdbcRepositoryImpl implements ISitiosClientesJdbcRepository {
	
	private static final String FIND_SITIO_CLIENTE_SQL = """
            SELECT 
                sc.CODIGO_PUNTO,
                sc.CODIGO_CLIENTE,
                sc.FAJADO
            FROM controlefect.SITIOS_CLIENTE sc
            WHERE sc.CODIGO_PUNTO = ?
            """;

    private final DataSource dataSource;

    public SitiosClientesJdbcRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public SitiosClientes findByCodigoPunto(Integer codigoPunto) {
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_SITIO_CLIENTE_SQL)) {
            
            stmt.setInt(1, codigoPunto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    SitiosClientes sitioCliente = mapResultSetToEntity(rs);
                    return sitioCliente;
                }
                return null;
            }
            
        } catch (SQLException e) {
            log.error("Error al consultar SitiosClientes con codigo_punto: {}", codigoPunto, e);
            throw new RuntimeException("Error al consultar SitiosClientes", e);
        }
    }


    private SitiosClientes mapResultSetToEntity(ResultSet rs) throws SQLException {
        return SitiosClientes.builder()
            .codigoPunto(rs.getInt("CODIGO_PUNTO"))
            .codigoCliente(rs.getInt("CODIGO_CLIENTE"))
            .fajado(rs.getBoolean("FAJADO"))
            .puntos(null)
            .build();
    }
}