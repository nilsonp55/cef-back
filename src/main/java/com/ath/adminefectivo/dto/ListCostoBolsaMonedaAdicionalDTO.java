package com.ath.adminefectivo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListCostoBolsaMonedaAdicionalDTO {

	List<CostoBolsaMonedaAdicionalDTO> list;

}
