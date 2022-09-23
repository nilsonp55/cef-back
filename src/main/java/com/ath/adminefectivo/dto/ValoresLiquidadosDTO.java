package com.ath.adminefectivo.dto;

import java.util.List;

import com.ath.adminefectivo.entities.ValoresLiquidados;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValoresLiquidadosDTO {
	Integer cantidad1;
	Integer cantidad2;
	List<ValoresLiquidados> valoresLiquidados;
}
