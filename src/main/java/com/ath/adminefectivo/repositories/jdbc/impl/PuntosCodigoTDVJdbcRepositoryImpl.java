package com.ath.adminefectivo.repositories.jdbc.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.ath.adminefectivo.entities.PuntosCodigoTDV;
import com.ath.adminefectivo.repositories.jdbc.IPuntosCodigoTDVJdbcRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PuntosCodigoTDVJdbcRepositoryImpl implements IPuntosCodigoTDVJdbcRepository {
    
    private static final String FIND_PUNTO_TDV_BY_PARAMETERS_SQL = """
            SELECT 
                p.id_punto_codigo_tdv,
                p.codigo_punto,
                p.codigo_tdv,
                p.codigo_propio_tdv,
                p.ciudad_fondo,
                p.estado
            FROM controlefect.puntos_codigo_tdv p
            WHERE p.codigo_propio_tdv = ?
            AND p.codigo_tdv = ?
            AND p.codigo_banco = ?
            AND p.ciudad_fondo = ?
            """;    
    
    private static final String FIND_PUNTOS_TDV_LIST_SQL = """
            SELECT 
                p.id_punto_codigo_tdv,
                p.codigo_punto,
                p.codigo_tdv,
                p.codigo_propio_tdv,
                p.ciudad_fondo,
                p.estado
            FROM controlefect.puntos_codigo_tdv p
            WHERE p.codigo_propio_tdv = ?
                AND p.codigo_tdv = ?
                AND p.codigo_banco = ?
            """;

    private final DataSource dataSource;

    public PuntosCodigoTDVJdbcRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public PuntosCodigoTDV findByCodigoPropioTDVAndCodigoTDVAndBancosAndCiudadCodigo(String codigoPropioTDV, 
                                       String codigoTDV, 
                                       Integer codigoBanco, 
                                       String codigoDane) {
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_PUNTO_TDV_BY_PARAMETERS_SQL)) {
        	
            stmt.setString(1, codigoPropioTDV.trim());
            stmt.setString(2, codigoTDV);
            stmt.setInt(3, codigoBanco);
            stmt.setString(4, codigoDane);
            
            try (ResultSet rs = stmt.executeQuery()) {
                PuntosCodigoTDV result = rs.next() ? mapResultSetToEntity(rs) : null;
                return result;
            }
            
        } catch (SQLException e) {
            log.error("Error consultando PuntoTDV: codigoPropioTDV={}, codigoTDV={}, codigoBanco={}", 
                      codigoPropioTDV, codigoTDV, codigoBanco, e);
            throw new RuntimeException("Error consultando PuntoTDV", e);
        }
    }
    
    
    @Override
    public List<PuntosCodigoTDV> findByCodigoPropioTDVAndCodigoTDVAndBancos(String codigoPropioTDV, 
                                                   String codigoTDV, 
                                                   Integer codigoBanco) {        
        List<PuntosCodigoTDV> resultList = new ArrayList<>();
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_PUNTOS_TDV_LIST_SQL)) {
            
            stmt.setString(1, codigoPropioTDV.trim());
            stmt.setString(2, codigoTDV);
            stmt.setInt(3, codigoBanco);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PuntosCodigoTDV entity = mapResultSetToEntity(rs);
                    resultList.add(entity);
                }
            }            
            return resultList;
            
        } catch (SQLException e) {
            log.error("Error consultando lista de PuntosTDV para el codigoTDV: {}, banco: {}", 
                     codigoTDV, codigoBanco, e);
            throw new RuntimeException("Error consultando lista de PuntosTDV", e);
        }
    }

    private PuntosCodigoTDV mapResultSetToEntity(ResultSet rs) throws SQLException {
        return PuntosCodigoTDV.builder()
            .idPuntoCodigoTdv(rs.getInt("id_punto_codigo_tdv"))
            .codigoPunto(rs.getInt("codigo_punto"))
            .codigoTDV(rs.getString("codigo_tdv"))
            .codigoPropioTDV(rs.getString("codigo_propio_tdv"))
            .ciudadCodigo(rs.getString("ciudad_fondo"))
            .estado(rs.getInt("estado"))
            .bancos(null)
            .build();
    }
}