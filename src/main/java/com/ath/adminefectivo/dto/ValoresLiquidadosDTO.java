package com.ath.adminefectivo.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValoresLiquidadosDTO {
	
	Integer cantidadOperacionesLiquidadas;
	Integer registrosConError;
	List<ParametrosLiquidacionCostoDTO> respuestaLiquidarCostos;
}
