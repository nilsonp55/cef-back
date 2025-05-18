package com.ath.adminefectivo.dto.compuestos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

import javax.inject.Named;

import com.ath.adminefectivo.entities.OperacionesLiquidacionTransporteEntity;
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
public class OperacionesLiquidacionTransporteDTO {
	
	
	

	@JsonProperty("consecutivo_registro")
	private Integer consecutivoRegistro;
	@JsonProperty("id_archivo_cargado") 
	private Integer idArchivoCargado;
	@JsonProperty("id_registro") 
	private Integer idRegistro;
	@JsonProperty("id_liquidacion")
	private Integer idLiquidacion;
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
	private String codigoPuntoCargo; //Integer
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
	@JsonProperty("tipo_pedido")
	private String tipoPedido;
	@JsonProperty("tipo_pedido_tdv")
	private String tipoPedidoTdv;
	@JsonProperty("escala")
	private String escala;
	@JsonProperty("escala_tdv")
	private String escalaTdv;
	@JsonProperty("valor_transportado_billetes")
	private BigDecimal valorTransportadoBilletes;
	@JsonProperty("valor_transportado_billetes_tdv")
	private BigDecimal valorTransportadoBilletesTdv;
	@JsonProperty("valor_transportado_monedas")
	private BigDecimal valorTransportadoMonedas;
	@JsonProperty("valor_transportado_monedas_tdv")
	private BigDecimal valorTransportadoMonedasTdv;
	@JsonProperty("valor_total_transportado")
	private BigDecimal valorTotalTransportado;
	@JsonProperty("valor_total_transportado_tdv")
	private BigDecimal valorTotalTransportadoTdv;
	@JsonProperty("numero_bolsas")
	private BigDecimal numeroBolsas;
	@JsonProperty("numero_bolsas_tdv")
	private BigDecimal numeroBolsasTdv;
	@JsonProperty("costo_fijo")
	private Integer costoFijo;
	@JsonProperty("costo_fijo_tdv")
	private Integer costoFijoTdv;
	@JsonProperty("costo_milaje")
	private BigDecimal costoMilaje;
	@JsonProperty("costo_milaje_tdv")
	private BigDecimal costoMilajeTdv;
	@JsonProperty("costo_bolsa")
	private BigDecimal costoBolsa;
	@JsonProperty("costo_bolsa_tdv")
	private BigDecimal costoBolsaTdv;
	@JsonProperty("costo_flete")
	private Integer costoFlete;
	@JsonProperty("costo_flete_tdv")
	private Integer costoFleteTdv;
	@JsonProperty("costo_emisario")
	private Integer costoEmisario; 
	@JsonProperty("costo_emisario_tdv")
	private Integer costoEmisarioTdv; 
	@JsonProperty("otros_1")
	private Integer otros1; 
	@JsonProperty("otros_2")
	private Integer otros2;
	@JsonProperty("otros_3")
	private Integer otros3;
	@JsonProperty("otros_4")
	private Integer otros4;
	@JsonProperty("otros_5")
	private Integer otros5; 
	private BigDecimal subtotal;
	private BigDecimal iva;
	@JsonProperty("valor_total")
	private BigDecimal valorTotal;
	private String estado;
	private String modulo;
	
	
	
	
	

	/**
	 * Funcion que convierte la entity  a archivo DTO  
	 * @author jorge.capera
	 */
	public static final Function<OperacionesLiquidacionTransporteEntity, OperacionesLiquidacionTransporteDTO> CONVERTER_DTO = (OperacionesLiquidacionTransporteEntity t) -> {

		var operacionesLiquidacionTransporteDTO = new OperacionesLiquidacionTransporteDTO();
		UtilsObjects.copiarPropiedades(t, operacionesLiquidacionTransporteDTO);		

		return operacionesLiquidacionTransporteDTO;
	};
}
