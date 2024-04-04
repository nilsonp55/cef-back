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
	private Integer consecutivoRegistro;
	@JsonProperty("id_archivo_cargado")	
	private Integer idArchivoCargado;
	@JsonProperty("id_registro")	
	private Integer idRegistro;
	@JsonProperty("id_liquidacion")
	private Long idLiquidacion;
	@JsonProperty("tipo_trasaccion")
	private Integer tipoTransaccion;	
	@JsonProperty("entidad")
	private String entidad;
	@JsonProperty("fecha_servicio_transporte")
	private LocalDateTime fechaServicioTransporte;
	@JsonProperty("identificacion_cliente")
	private String identificacionCliente;
	@JsonProperty("razon_social")
	private String razonSocial;
	@JsonProperty("codigo_punto_cargo")
	private String codigoPuntoCargo;
	@JsonProperty("nombre_punto_cargo")
	private String nombrePuntoCargo;
	@JsonProperty("ciudad_fondo")
	private String ciudadFondo;
	@JsonProperty("nombre_tipo_servicio")
	private String nombreTipoServicio;
	@JsonProperty("moneda_divisa")
	private String monedaDivisa;
	@JsonProperty("aplicativo")
	private Integer aplicativo;
	@JsonProperty("tdv")
	private Integer tdv;
	@JsonProperty("valor_procesado_billete")
	private Double valorProcesadoBillete;
	@JsonProperty("valor_procesado_billete_tdv")
	private BigDecimal valorProcesadoBilleteTdv;
	@JsonProperty("valor_procesado_moneda")
	private Double valorProcesadoMoneda;
	@JsonProperty("valor_procesado_moneda_tdv")
	private BigDecimal valorProcesadoMonedaTdv;
	@JsonProperty("valor_total_procesado")
	private Double valorTotalProcesado;
	@JsonProperty("valor_total_procesado_tdv")
	private BigDecimal valorTotalProcesadoTdv;
	private Double subtotal;
	private BigDecimal subtotalTdv;
	private BigDecimal iva;
	@JsonProperty("valor_total")
	private BigDecimal valorTotal;
	private String estado;
	private String modulo;
	
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
