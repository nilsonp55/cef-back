package com.ath.adminefectivo.dto.compuestos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del DetalleOperacionesDTO
 * @author duvan.naranjo
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetalleOperacionesDTO {

	private Integer idOperacion;
	
	private String denominacion;
	
	private Integer calidad;
	
	private String familia;
	
	private Double valorDetalle;
}
