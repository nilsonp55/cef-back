package com.ath.adminefectivo.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "v_detalle_liquidacion_transporte")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperacionesLiquidacionTransporteEntity {

	@Id
    @Named(value = "CONSECUTIVO_REGISTRO")
    private Integer consecutivoRegistro;

    @Named(value = "ID_ARCHIVO_CARGADO")
    private Integer idArchivoCargado;

    @Named(value = "ID_REGISTRO")
    private Integer idRegistro;

    @Named(value = "ID_LIQUIDACION")
    private Integer idLiquidacion;

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

    @Named(value = "TIPO_PEDIDO")
    private String tipoPedido;

    @Named(value = "TIPO_PEDIDO_TDV")
    private String tipoPedidoTdv;

    @Named(value = "ESCALA")
    private String escala;

    @Named(value = "ESCALA_TDV")
    private String escalaTdv;

    @Named(value = "VALOR_TRANSPORTADO_BILLETES")
    private BigDecimal valorTransportadoBilletes;

    @Named(value = "VALOR_TRANSPORTADO_BILLETES_TDV")
    private BigDecimal valorTransportadoBilletesTdv;

    @Named(value = "VALOR_TRANSPORTADO_MONEDAS")
    private BigDecimal valorTransportadoMonedas;

    @Named(value = "VALOR_TRANSPORTADO_MONEDAS_TDV")
    private BigDecimal valorTransportadoMonedasTdv;

    @Named(value = "VALOR_TOTAL_TRANSPORTADO")
    private BigDecimal valorTotalTransportado;

    @Named(value = "VALOR_TOTAL_TRANSPORTADO_TDV")
    private BigDecimal valorTotalTransportadoTdv;
    
    @Named(value = "NUMERO_FAJOS")
    private BigDecimal numeroFajos;

    @Named(value = "NUMERO_FAJOS_TDV")
    private BigDecimal numeroFajosTdv;

    @Named(value = "NUMERO_BOLSAS")
    private BigDecimal numeroBolsas;

    @Named(value = "NUMERO_BOLSAS_TDV")
    private BigDecimal numeroBolsasTdv;

    @Named(value = "COSTO_FIJO")
    private Integer costoFijo;

    @Named(value = "COSTO_FIJO_TDV")
    private Integer costoFijoTdv;

    @Named(value = "COSTO_MILAJE")
    private BigDecimal costoMilaje;

    @Named(value = "COSTO_MILAJE_TDV")
    private BigDecimal costoMilajeTdv;

    @Named(value = "COSTO_BOLSA")
    private BigDecimal costoBolsa;

    @Named(value = "COSTO_BOLSA_TDV")
    private BigDecimal costoBolsaTdv;

    @Named(value = "COSTO_FLETE")
    private Integer costoFlete;

    @Named(value = "COSTO_FLETE_TDV")
    private Integer costoFleteTdv;

    @Named(value = "COSTO_EMISARIO")
    private Integer costoEmisario;

    @Named(value = "COSTO_EMISARIO_TDV")
    private Integer costoEmisarioTdv;

    @Named(value = "OTROS_1")
    private Integer otros1;

    @Named(value = "OTROS_2")
    private Integer otros2;

    @Named(value = "OTROS_3")
    private Integer otros3;

    @Named(value = "OTROS_4")
    private Integer otros4;

    @Named(value = "OTROS_5")
    private Integer otros5;

    @Named(value = "SUBTOTAL")
    private BigDecimal subtotal;

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
