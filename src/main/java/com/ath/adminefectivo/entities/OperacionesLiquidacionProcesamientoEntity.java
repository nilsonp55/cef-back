package com.ath.adminefectivo.entities;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "v_detalle_liquidacion_procesamiento")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperacionesLiquidacionProcesamientoEntity {
	
	@Id
    @Named(value = "CONSECUTIVO_REGISTRO")
    private Integer consecutivoRegistro;

    @Named(value = "ID_ARCHIVO_CARGADO")
    private Integer idArchivoCargado;

    @Named(value = "ID_REGISTRO")
    private Integer idRegistro;

    @Named(value = "ID_LIQUIDACION")
    private Long idLiquidacion; // Se asume que ID_LIQUIDACION es INT8 en la tabla

    @Named(value = "TIPO_TRANSACCION")
    private Integer tipoTransaccion;

    @Named(value = "ENTIDAD")
    private String entidad;

    @Named(value = "FECHA_SERVICIO_TRANSPORTE")
    private LocalDateTime fechaServicioTransporte;

    @Named(value = "IDENTIFICACION_CLIENTE")
    private String identificacionCliente;

    @Named(value = "RAZON_SOCIAL")
    private String razonSocial;

    @Named(value = "CODIGO_PUNTO_CARGO")
    private String codigoPuntoCargo;

    @Named(value = "NOMBRE_PUNTO_CARGO")
    private String nombrePuntoCargo;

    @Named(value = "CIUDAD_FONDO")
    private String ciudadFondo;

    @Named(value = "NOMBRE_TIPO_SERVICIO")
    private String nombreTipoServicio;

    @Named(value = "MONEDA_DIVISA")
    private String monedaDivisa;

    @Named(value = "APLICATIVO")
    private Integer aplicativo;

    @Named(value = "TDV")
    private Integer tdv;

    @Named(value = "VALOR_PROCESADO_BILLETE")
    private Double valorProcesadoBillete;

    @Named(value = "VALOR_PROCESADO_BILLETE_TDV")
    private BigDecimal valorProcesadoBilleteTdv;

    @Named(value = "VALOR_PROCESADO_MONEDA")
    private Double valorProcesadoMoneda;

    @Named(value = "VALOR_PROCESADO_MONEDA_TDV")
    private BigDecimal valorProcesadoMonedaTdv;

    @Named(value = "VALOR_TOTAL_PROCESADO")
    private Double valorTotalProcesado;

    @Named(value = "VALOR_TOTAL_PROCESADO_TDV")
    private BigDecimal valorTotalProcesadoTdv;

    @Named(value = "SUBTOTAL")
    private Double subtotal;

    @Named(value = "SUBTOTAL_TDV")
    private BigDecimal subtotalTdv;

    @Named(value = "IVA")
    private BigDecimal iva;

    @Named(value = "VALOR_TOTAL")
    private BigDecimal valorTotal;

    @Named(value = "ESTADO")
    private String estado;

    @Named(value = "MODULO")
    private String modulo;
}
