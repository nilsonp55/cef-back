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
	private Long settlementId;
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
	private Double processedBillValue;
	@JsonProperty("valor_procesado_billete_tdv")
	private BigDecimal processedBillValueTdv;
	@JsonProperty("valor_procesado_moneda")
	private Double processedCoinValue;
	@JsonProperty("valor_procesado_moneda_tdv")
	private BigDecimal processedCoinValueTdv;
	@JsonProperty("valor_total_procesado")
	private Double totalProcessedValue;
	@JsonProperty("valor_total_procesado_tdv")
	private BigDecimal totalProcessedValueTdv;
	@JsonProperty("subtotal")
	private Double processSubtotal;
	@JsonProperty("subtotalTdv")
	private BigDecimal processSubtotalTdv;
	@JsonProperty("iva")
	private BigDecimal tax;
	@JsonProperty("valor_total")
	private BigDecimal totalValue;
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
