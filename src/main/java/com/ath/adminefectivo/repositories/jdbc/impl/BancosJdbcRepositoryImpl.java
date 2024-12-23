package com.ath.adminefectivo.repositories.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.ath.adminefectivo.dto.BancosDTO;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.jdbc.IBancosJdbcRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class BancosJdbcRepositoryImpl implements IBancosJdbcRepository {
	
	private static final String FIND_BANCO_BY_CODIGO_SQL = """
            SELECT codigo_punto, es_aval, codigo_compensacion, 
                   numero_nit, abreviatura, nombre_banco 
            FROM controlefect.bancos 
            WHERE codigo_punto = ?
            """;
    
    private final DataSource dataSource;

    public BancosJdbcRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public BancosDTO findBancoByCodigoPunto(int codigoPunto) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BANCO_BY_CODIGO_SQL)) {
            
            stmt.setInt(1, codigoPunto);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBancosDTO(rs);
                }
                
                throw new NegocioException(
                    ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getCode(),
                    "Banco no encontrado para codigo = " + codigoPunto,
                    ApiResponseCode.ERROR_BANCOS_NO_ENCONTRADO.getHttpStatus()
                );
            }
        } catch (SQLException e) {
            log.error("Error consultando banco con código: {}", codigoPunto, e);
			throw new NegocioException(ApiResponseCode.GENERIC_ERROR.getCode(), ApiResponseCode.GENERIC_ERROR.getDescription(),
					ApiResponseCode.GENERIC_ERROR.getHttpStatus());
        }
    }
    
    private BancosDTO mapResultSetToBancosDTO(ResultSet rs) throws SQLException {
        return BancosDTO.builder()
            .codigoPunto(rs.getInt("codigo_punto"))
            .esAVAL(rs.getBoolean("es_aval"))
            .codigoCompensacion(rs.getInt("codigo_compensacion"))
            .numeroNit(rs.getString("numero_nit"))
            .abreviatura(rs.getString("abreviatura"))
            .nombreBanco(rs.getString("nombre_banco"))
            .build();
    }
}