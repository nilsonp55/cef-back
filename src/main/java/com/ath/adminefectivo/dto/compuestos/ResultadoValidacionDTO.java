package com.ath.adminefectivo.dto.compuestos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoValidacionDTO {

	private String nombreArchivo;
	private String contenidoArchivo;
	
	private List<ErroresCamposDTO> erroresValidacion;

}
