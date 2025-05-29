package com.ath.adminefectivo.repositories.jdbc.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.entities.OperacionesCertificadas;
import com.ath.adminefectivo.exception.NegocioException;
import com.ath.adminefectivo.repositories.jdbc.IOperacionesCertificadasJdbcRepository;

@Slf4j
@Repository
public class OperacionesCertificadasJdbcRepositoryImpl implements IOperacionesCertificadasJdbcRepository {
	
	private static final String FIND_OPERACION_CERTIFICADA_SQL = """
            SELECT 
                id_certificacion,
                codigo_fondo_tdv,
                codigo_punto_origen,
                codigo_punto_destino,
                fecha_ejecucion,
                tipo_operacion,
                tipo_servicio,
                estado_conciliacion,
                conciliable,
                valor_total,
                valor_faltante,
                valor_sobrante,
                fallida_oficina,
                usuario_creacion,
                usuario_modificacion,
                fecha_creacion,
                fecha_modificacion,
                codigo_servico_tdv,
                entrada_salida,
                id_archivo_cargado,
                nombre_tdv,
                nombre_banco_aval,
                tipo_punto_origen,
                tipo_punto_destino,
                nombre_fondotdv,
                codigo_propio_tdv,
                moneda,
                codigo_operacion,
                consecutivo_registro,
                codigopunto_codigotdv,
                descripcionpunto_codigotdv
            FROM controlefect.operaciones_certificadas
            WHERE codigo_fondo_tdv = ?
            AND codigo_punto_origen = ?
            AND codigo_punto_destino = ?
            AND codigo_servico_tdv = ?
            AND entrada_salida = ?
            AND fecha_ejecucion = ?
            AND codigo_propio_tdv = ?
            AND id_archivo_cargado = ?
            """;
            
    private final DataSource dataSource;

    public OperacionesCertificadasJdbcRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public OperacionesCertificadas findOperacionCertificadaByParametros(
            Integer codigoFondoTDV,
            Integer codigoPuntoOrigen,
            Integer codigoPuntoDestino,
            OperacionesCertificadas operacionesCertificadasObj) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_OPERACION_CERTIFICADA_SQL)) {

            stmt.setInt(1, codigoFondoTDV);
            stmt.setInt(2, codigoPuntoOrigen);
            stmt.setInt(3, codigoPuntoDestino);
            stmt.setString(4, operacionesCertificadasObj.getCodigoServicioTdv());
            stmt.setString(5, operacionesCertificadasObj.getEntradaSalida());
            stmt.setTimestamp(6, new Timestamp(operacionesCertificadasObj.getFechaEjecucion().getTime()));
            stmt.setString(7, operacionesCertificadasObj.getCodigoPropioTDV());
            stmt.setLong(8, operacionesCertificadasObj.getIdArchivoCargado());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                   return mapResultSetToEntity(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            log.error("Error consultando Operaciones Certificada: fondoTDV={}, puntoOrigen={}", 
                    codigoFondoTDV, codigoPuntoOrigen, e);          
			throw new NegocioException(ApiResponseCode.GENERIC_ERROR.getCode(),
					ApiResponseCode.GENERIC_ERROR.getDescription(),
					ApiResponseCode.GENERIC_ERROR.getHttpStatus());
        }
    }

    private OperacionesCertificadas mapResultSetToEntity(ResultSet rs) throws SQLException {
        return OperacionesCertificadas.builder()
            .idCertificacion(rs.getInt("id_certificacion"))
            .codigoFondoTDV(rs.getInt("codigo_fondo_tdv"))
            .codigoPuntoOrigen(rs.getInt("codigo_punto_origen"))
            .codigoPuntoDestino(rs.getInt("codigo_punto_destino"))
            .fechaEjecucion(rs.getTimestamp("fecha_ejecucion"))
            .tipoOperacion(rs.getString("tipo_operacion"))
            .tipoServicio(rs.getString("tipo_servicio"))
            .estadoConciliacion(rs.getString("estado_conciliacion"))
            .conciliable(rs.getString("conciliable"))
            .valorTotal(rs.getDouble("valor_total"))
            .valorFaltante(rs.getDouble("valor_faltante"))
            .valorSobrante(rs.getDouble("valor_sobrante"))
            .fallidaOficina(rs.getString("fallida_oficina"))
            .usuarioCreacion(rs.getString("usuario_creacion"))
            .usuarioModificacion(rs.getString("usuario_modificacion"))
            .fechaCreacion(rs.getTimestamp("fecha_creacion"))
            .fechaModificacion(rs.getTimestamp("fecha_modificacion"))
            .codigoServicioTdv(rs.getString("codigo_servico_tdv"))
            .entradaSalida(rs.getString("entrada_salida"))
            .idArchivoCargado(rs.getLong("id_archivo_cargado"))
            .tdv(rs.getString("nombre_tdv"))
            .bancoAVAL(rs.getString("nombre_banco_aval"))
            .tipoPuntoOrigen(rs.getString("tipo_punto_origen"))
            .tipoPuntoDestino(rs.getString("tipo_punto_destino"))
            .nombreFondoTDV(rs.getString("nombre_fondotdv"))
            .codigoPropioTDV(rs.getString("codigo_propio_tdv"))
            .moneda(rs.getString("moneda"))
            .codigoOperacion(rs.getString("codigo_operacion"))
            .consecutivoRegistro(rs.getLong("consecutivo_registro"))
            .build();
    }
}
