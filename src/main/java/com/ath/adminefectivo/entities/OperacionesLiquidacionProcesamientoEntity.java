package com.ath.adminefectivo.entities;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "v_detalle_liquidacion_procesamiento")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperacionesLiquidacionProcesamientoEntity {
	
	@Id
    @Column(name = "CONSECUTIVO_REGISTRO")
    private Integer recordConsecutive;

    @Column(name = "ID_ARCHIVO_CARGADO")
    private Integer loadedFileId;

    @Column(name = "ID_REGISTRO")
    private Integer recordId;

    @Column(name = "ID_LIQUIDACION")
    private Integer settlementId;

    @Column(name = "TIPO_TRANSACCION")
    private Integer transactionType;

    @Column(name = "ENTIDAD")
    private String entity;

    @Column(name = "FECHA_SERVICIO_TRANSPORTE")
    private LocalDateTime transportServiceDate;

    @Column(name = "IDENTIFICACION_CLIENTE")
    private String clientIdentification;

    @Column(name = "RAZON_SOCIAL")
    private String businessName;

    @Column(name = "CODIGO_PUNTO_CARGO")
    private String cargoPointCode;

    @Column(name = "NOMBRE_PUNTO_CARGO")
    private String cargoPointName;

    @Column(name = "CIUDAD_FONDO")
    private String fundCity;

    @Column(name = "NOMBRE_TIPO_SERVICIO")
    private String serviceTypeName;

    @Column(name = "MONEDA_DIVISA")
    private String currencyExchange;

    @Column(name = "APLICATIVO")
    private Integer application;

    @Column(name = "TDV")
    private Integer stdv;

    @Column(name = "VALOR_PROCESADO_BILLETE")
    private BigDecimal processedBillname;

    @Column(name = "VALOR_PROCESADO_BILLETE_TDV")
    private BigDecimal processedBillnameTdv;

    @Column(name = "VALOR_PROCESADO_MONEDA")
    private BigDecimal processedCoinname;

    @Column(name = "VALOR_PROCESADO_MONEDA_TDV")
    private BigDecimal processedCoinnameTdv;

    @Column(name = "VALOR_TOTAL_PROCESADO")
    private BigDecimal totalProcessedname;

    @Column(name = "VALOR_TOTAL_PROCESADO_TDV")
    private BigDecimal totalProcessednameTdv;

    @Column(name = "SUBTOTAL")
    private BigDecimal processSubtotal;

    @Column(name = "SUBTOTAL_TDV")
    private BigDecimal processSubtotalTdv;

    @Column(name = "IVA")
    private BigDecimal tax;

    @Column(name = "VALOR_TOTAL")
    private BigDecimal totalname;
    
    @Column(name = "CLASIFICACION_FAJADO")
    private BigDecimal classificationBundled;

    @Column(name = "CLASIFICACION_FAJADO_TDV")
    private BigDecimal classificationBundledTdv;
    
    @Column(name = "CLASIFICACION_NO_FAJADO")
    private BigDecimal classificationUnbundled;

    @Column(name = "CLASIFICACION_NO_FAJADO_TDV")
    private BigDecimal classificationUnbundledTdv;
    
    @Column(name = "COSTO_PAQUETEO")
    private BigDecimal packagingCost;
    
    @Column(name = "COSTO_PAQUETEO_TDV")
    private BigDecimal packagingCostTdv;
    
    @Column(name = "MONEDA_RESIDUO")
    private BigDecimal coinResidue;
    
    @Column(name = "MONEDA_RESIDUO_TDV")
    private BigDecimal coinResidueTdv;
    
    @Column(name = "BILLETE_RESIDUO")
    private BigDecimal billResidue;
    
    @Column(name = "BILLETE_RESIDUO_TDV")
    private BigDecimal billResidueTdv;
    
    @Column(name = "VALOR_ALMACENAMIENTO_BILLETE")
    private BigDecimal storageValueBill;

    @Column(name = "VALOR_ALMACENAMIENTO_BILLETE_TDV")
    private BigDecimal storageValueBillTdv;

    @Column(name = "VALOR_ALMACENAMIENTO_MONEDA")
    private BigDecimal storageValueCoin;

    @Column(name = "VALOR_ALMACENAMIENTO_MONEDA_TDV")
    private BigDecimal storageValueCoinTdv;

    @Column(name = "ESTADO")
    private String state;

    @Column(name = "MODULO")
    private String module;
}
