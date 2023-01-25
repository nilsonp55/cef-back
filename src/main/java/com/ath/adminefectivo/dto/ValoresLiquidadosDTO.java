package com.ath.adminefectivo.dto;

import java.util.List;

import com.ath.adminefectivo.dto.compuestos.RespuestaLiquidarCostosDTO;
import com.ath.adminefectivo.entities.ValoresLiquidados;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValoresLiquidadosDTO {
	
	Integer cantidadOperacionesLiquidadas;
	Integer registrosConError;
	List<ParametrosLiquidacionCostoDTO> respuestaLiquidarCostos;
}
