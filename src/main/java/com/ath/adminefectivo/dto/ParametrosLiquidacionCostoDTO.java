package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import com.ath.adminefectivo.entities.DetallesLiquidacionCosto;
import com.ath.adminefectivo.entities.ParametrosLiquidacionCosto;
import com.ath.adminefectivo.entities.ValoresLiquidados;
import com.ath.adminefectivo.utils.UtilsObjects;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura de ParametrosLiquidacionCosto
 * @author cesar.castano
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParametrosLiquidacionCostoDTO {

	private Long idLiquidacion;
	
	private String billetes;
	
	private Integer codigoBanco;
	
	private String codigoTdv;
	
	private String escala;
	
	private String fajado;
	
	private Date fechaEjecucion;
	
	private String monedas;
	
	private Integer numeroBolsas;
	
	private Integer numeroFajos;
	
	private Integer numeroParadas;
	
	private Integer puntoDestino;
	
	private Integer puntoOrigen;
	
	private Integer residuoBilletes;
	
	private Integer residuoMonedas;
	
	private Integer seqGrupo;
	
	private String tipoOperacion;
	
	private String tipoPunto;
	
	private String tipoServicio;
	
	private Double valorBilletes;
	
	private Double valorMonedas;
	
	private Double valorTotal;
	
	@JsonIgnore
	private List<DetallesLiquidacionCosto> detallesLiquidacionCosto;
	
	private ValoresLiquidados valoresLiquidados;
	
	private String nombreBanco;
	
	private String nombreTdv;
	
	private String nombrePuntoOrigen;
	
	private String nombrePuntoDestino;
	
	/**
	 * Funcion que convierte el archivo ParametrosLiquidacionCostoDTO a Entity ParametrosLiquidacionCosto
	 * @author cesar.castano
	 */
	public static final Function<ParametrosLiquidacionCostoDTO, ParametrosLiquidacionCosto> 
									CONVERTER_ENTITY = (ParametrosLiquidacionCostoDTO t) -> {

		ParametrosLiquidacionCosto parametrosLiquidacionCosto = new ParametrosLiquidacionCosto();
		UtilsObjects.copiarPropiedades(t, parametrosLiquidacionCosto);		

		return parametrosLiquidacionCosto;
	};
	
	/**
	 * Funcion que convierte la entity ParametrosLiquidacionCosto a archivo DTO ParametrosLiquidacionCostoDTO
	 * @author cesar.castano
	 */
	public static final Function<ParametrosLiquidacionCosto, ParametrosLiquidacionCostoDTO> 
										CONVERTER_DTO = (ParametrosLiquidacionCosto t) -> {

		ParametrosLiquidacionCostoDTO parametrosLiquidacionCostoDTO = new ParametrosLiquidacionCostoDTO();
		UtilsObjects.copiarPropiedades(t, parametrosLiquidacionCostoDTO);		

		return parametrosLiquidacionCostoDTO;
	};
}
