package com.ath.adminefectivo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ath.adminefectivo.entities.CostoBolsaMonedaAdicional;

import com.ath.adminefectivo.utils.UtilsObjects;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CostoBolsaMonedaAdicionalDTO {
	

	private Integer idCostoBolsaAdicional;

	private TransportadorasDTO transportadoraDTO; 

	private Integer limiteBolsasMoneda; 

	private BigDecimal valorBolsasAdicional;

	private Integer estadoCostoBolsaMonedaAdicional;
	
	private String usuarioCreacionCostoBolsaMonedaAdicional;
	
	private Date fechaCreacionCostoBolsaMonedaAdicional;

	private String usuarioModificacionCostoBolsaMonedaAdicional;
	
	private Date fechaModificacionCostoBolsaMonedaAdicional;
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<CostoBolsaMonedaAdicionalDTO, CostoBolsaMonedaAdicional> CONVERTER_ENTITY = (CostoBolsaMonedaAdicionalDTO t) -> {
		var costoBolsaMonedaAdicional = new CostoBolsaMonedaAdicional();
		UtilsObjects.copiarPropiedades(t, costoBolsaMonedaAdicional);
		if(!Objects.isNull(t.getTransportadoraDTO())) {
			costoBolsaMonedaAdicional.setTransportadora(TransportadorasDTO.CONVERTER_ENTITY.apply(t.getTransportadoraDTO()));
		}
		return costoBolsaMonedaAdicional;
	};
	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<CostoBolsaMonedaAdicional, CostoBolsaMonedaAdicionalDTO> CONVERTER_DTO = (CostoBolsaMonedaAdicional t) -> {
		var costoBolsaMonedaAdicionalDTO = new CostoBolsaMonedaAdicionalDTO();
		UtilsObjects.copiarPropiedades(t, costoBolsaMonedaAdicionalDTO);
		if(!Objects.isNull(t.getTransportadora())) {
			costoBolsaMonedaAdicionalDTO.setTransportadoraDTO(TransportadorasDTO.CONVERTER_DTO.apply(t.getTransportadora()));
		}
		return costoBolsaMonedaAdicionalDTO;
	};


}
