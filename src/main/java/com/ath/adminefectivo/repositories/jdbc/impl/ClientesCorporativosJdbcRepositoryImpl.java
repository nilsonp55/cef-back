package com.ath.adminefectivo.repositories.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import com.ath.adminefectivo.repositories.jdbc.IClientesCorporativosJdbcRepository;


@Slf4j
@Repository
public class ClientesCorporativosJdbcRepositoryImpl implements IClientesCorporativosJdbcRepository {
    
    private static final String CHECK_CLIENTE_CORPORATIVO_EXISTS = """
            SELECT EXISTS (
                SELECT 1 
                FROM controlefect.CLIENTES_CORPORATIVOS 
                WHERE CODIGO_CLIENTE = ?
            )
            """;

    private final DataSource dataSource;

    public ClientesCorporativosJdbcRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean existsByCodigoCliente(Integer codigoCliente) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_CLIENTE_CORPORATIVO_EXISTS)) {
            
            stmt.setInt(1, codigoCliente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                boolean exists = rs.next() && rs.getBoolean(1);
                         
                return exists;
            }
            
        } catch (SQLException e) {
            log.error("Error verificando existencia de cliente corporativo con código: {}", 
                     codigoCliente, e);
            throw new RuntimeException("Error verificando existencia de cliente corporativo", e);
        }
    }
}