package com.ath.adminefectivo.repositories.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.Puntos;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.jdbc.IPuntosJdbcRepository;

@Slf4j
@Repository
public class PuntosJdbcRepositoryImpl implements IPuntosJdbcRepository {
    private static final String FIND_PUNTO_BY_AVAL_SQL = """
            SELECT
                p.CODIGO_PUNTO,
                p.TIPO_PUNTO,
                p.NOMBRE_PUNTO,
                p.CODIGO_CIUDAD,
                p.ESTADO
            FROM controlefect.PUNTOS p
            INNER JOIN controlefect.SITIOS_CLIENTE s ON p.CODIGO_PUNTO = s.CODIGO_PUNTO
            INNER JOIN controlefect.CLIENTES_CORPORATIVOS c ON c.CODIGO_CLIENTE = s.CODIGO_CLIENTE
            WHERE p.TIPO_PUNTO = 'CLIENTE'
            AND c.IDENTIFICACION = '9999999999'
            AND c.CODIGO_BANCO_AVAL = ?
            """;

    private static final String CHECK_PUNTO_EXISTS_SQL = """
            SELECT EXISTS (
                SELECT 1
                FROM controlefect.PUNTOS
                WHERE CODIGO_PUNTO = ?
                AND TIPO_PUNTO = ?
            )
            """;

    private static final String FIND_PUNTO_SQL = """
            SELECT
                pt.CODIGO_PUNTO,
                pt.TIPO_PUNTO,
                pt.NOMBRE_PUNTO,
                pt.CODIGO_CIUDAD,
                pt.ESTADO
            FROM controlefect.PUNTOS pt
            WHERE pt.CODIGO_PUNTO = ?
            """;

    private final DataSource dataSource;

    public PuntosJdbcRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Puntos findPuntoByCodigoAval(Integer codigoAval) {
        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(FIND_PUNTO_BY_AVAL_SQL)) {

            stmt.setInt(1, codigoAval);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
                log.debug("Punto no encontrado para codigo AVAL: {}", codigoAval);
                return null;
            }

        } catch (SQLException e) {
            log.error("Error consultando Punto con codigo AVAL: {}", codigoAval, e);
            throw new NegocioException(ApiResponseCode.GENERIC_ERROR.getCode(),
                    ApiResponseCode.GENERIC_ERROR.getDescription(), ApiResponseCode.GENERIC_ERROR.getHttpStatus());
        }
    }

    @Override
    public Boolean existsByTipoPuntoAndCodigoPunto(String tipoPunto, Integer codigoPunto) {

        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(CHECK_PUNTO_EXISTS_SQL)) {

            stmt.setInt(1, codigoPunto);
            stmt.setString(2, tipoPunto);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }

        } catch (SQLException e) {
            log.error("Error verificando existencia de punto - tipo: {}, código: {}", tipoPunto, codigoPunto, e);
            throw new NegocioException(ApiResponseCode.GENERIC_ERROR.getCode(),
                    ApiResponseCode.GENERIC_ERROR.getDescription(), ApiResponseCode.GENERIC_ERROR.getHttpStatus());
        }
    }

    private Puntos mapResultSetToEntity(ResultSet rs) throws SQLException {
        return Puntos.builder().codigoPunto(rs.getInt("CODIGO_PUNTO")).tipoPunto(rs.getString("TIPO_PUNTO"))
                .nombrePunto(rs.getString("NOMBRE_PUNTO")).codigoCiudad(rs.getString("CODIGO_CIUDAD"))
                .estado(rs.getString("ESTADO"))
                // No se cargan las relaciones para optimizar el desempeño
                .oficinas(null).sitiosClientes(null).puntosCodigoTDV(null).fondos(null).cajeroATM(null).bancos(null)
                .build();
    }

    @Override
    public Puntos findByCodigoPunto(Integer codigoPunto) {
        log.debug("Buscando punto con código: {}", codigoPunto);

        try (Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(FIND_PUNTO_SQL)) {

            stmt.setInt(1, codigoPunto);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Puntos punto = mapResultSetToEntity(rs);
                    log.debug("Punto encontrado: {} - {}", punto.getCodigoPunto(), punto.getNombrePunto());
                    return punto;
                }

                log.debug("No se encontró punto con código: {}", codigoPunto);
                return null;
            }

        } catch (SQLException e) {
            log.error("Error consultando punto con código: {}", codigoPunto, e);
            throw new NegocioException(ApiResponseCode.GENERIC_ERROR.getCode(),
                    ApiResponseCode.GENERIC_ERROR.getDescription(), ApiResponseCode.GENERIC_ERROR.getHttpStatus());
        }
    }

}