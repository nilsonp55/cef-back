package com.ath.adminefectivo.dto.compuestos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.OperacionesLiquidacionProcesamientoEntity;
import com.ath.adminefectivo.utils.UtilsObjects;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperacionesLiquidacionProcesamientoDTO {

	@JsonProperty("consecutivo_registro")
	private Integer recordConsecutive;
	@JsonProperty("id_archivo_cargado")	
	private Integer loadedFileId;
	@JsonProperty("id_registro")	
	private Integer recordId;
	@JsonProperty("id_liquidacion")
	private Integer settlementId;
	@JsonProperty("tipo_trasaccion")
	private Integer transactionType;	
	@JsonProperty("entidad")
	private String entity;
	@JsonProperty("fecha_servicio_transporte")
	private LocalDateTime transportServiceDate;
	@JsonProperty("identificacion_cliente")
	private String clientIdentification;
	@JsonProperty("razon_social")
	private String businessName;
	@JsonProperty("codigo_punto_cargo")
	private String cargoPointCode;
	@JsonProperty("nombre_punto_cargo")
	private String cargoPointName;
	@JsonProperty("ciudad_fondo")
	private String fundCity;
	@JsonProperty("nombre_tipo_servicio")
	private String serviceTypeName;
	@JsonProperty("moneda_divisa")
	private String currencyExchange;
	@JsonProperty("aplicativo")
	private Integer application;
	@JsonProperty("tdv")
	private Integer stdv;
	@JsonProperty("valor_procesado_billete")
	private BigDecimal processedBillname;
	@JsonProperty("valor_procesado_billete_tdv")
	private BigDecimal processedBillnameTdv;
	@JsonProperty("valor_procesado_moneda")
	private BigDecimal processedCoinname;
	@JsonProperty("valor_procesado_moneda_tdv")
	private BigDecimal processedCoinnameTdv;
	@JsonProperty("valor_total_procesado")
	private BigDecimal totalProcessedname;
	@JsonProperty("valor_total_procesado_tdv")
	private BigDecimal totalProcessednameTdv;
	@JsonProperty("subtotal")
	private BigDecimal processSubtotal;
	@JsonProperty("subtotalTdv")
	private BigDecimal processSubtotalTdv;
	@JsonProperty("iva")
	private BigDecimal tax;
	@JsonProperty("valor_total")
	private BigDecimal totalname;
	@JsonProperty("clasificacion_fajado")
	private BigDecimal classificationBundled;
	@JsonProperty("clasificacion_fajado_tdv")
	private BigDecimal classificationBundledTdv;
	@JsonProperty("clasificacion_no_fajado")
	private BigDecimal classificationUnbundled;
	@JsonProperty("clasificacion_no_fajado_tdv")
	private BigDecimal classificationUnbundledTdv;
	@JsonProperty("costo_paqueteo")
	private BigDecimal packagingCost;
	@JsonProperty("costo_paqueteo_tdv")
	private BigDecimal packagingCostTdv;
	@JsonProperty("moneda_residuo")
	private BigDecimal coinResidue;
	@JsonProperty("moneda_residuo_tdv")
	private BigDecimal coinResidueTdv;
	@JsonProperty("billete_residuo")
	private BigDecimal billResidue;
	@JsonProperty("billete_residuo_tdv")
	private BigDecimal billResidueTdv;
	@JsonProperty("estado")
	private String state;
	@JsonProperty("modulo")
	private String module;
	
	/**
	 * Funcion que convierte la entity a archivo DTO  
	 * @author jorge.capera
	 */
	public static final Function<OperacionesLiquidacionProcesamientoEntity, OperacionesLiquidacionProcesamientoDTO> CONVERTER_DTO = (OperacionesLiquidacionProcesamientoEntity t) -> {

		var OperacionesLiquidacionDTO = new OperacionesLiquidacionProcesamientoDTO();
		UtilsObjects.copiarPropiedades(t, OperacionesLiquidacionDTO);		

		return OperacionesLiquidacionDTO;
	};
}
