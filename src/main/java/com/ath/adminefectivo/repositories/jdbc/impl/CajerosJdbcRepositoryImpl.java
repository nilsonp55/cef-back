package com.ath.adminefectivo.repositories.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.jdbc.ICajerosJdbcRepository;

@Slf4j
@Repository
public class CajerosJdbcRepositoryImpl implements ICajerosJdbcRepository {
    private static final String CHECK_CAJERO_EXISTS_SQL = """
            SELECT EXISTS (
                SELECT 1
                FROM controlefect.CAJEROS_ATM
                WHERE CODIGO_PUNTO = ?
            )
            """;

    private final DataSource dataSource;

    public CajerosJdbcRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean existsByCodigoPunto(Integer codigoPunto) {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(CHECK_CAJERO_EXISTS_SQL)) {

            stmt.setInt(1, codigoPunto);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }

        } catch (SQLException e) {
            log.error("Error verificando existencia de cajero con código punto: {}", codigoPunto, e);
            throw new NegocioException(ApiResponseCode.GENERIC_ERROR.getCode(),
                    ApiResponseCode.GENERIC_ERROR.getDescription(), ApiResponseCode.GENERIC_ERROR.getHttpStatus());
        }
    }
}