package com.ath.adminefectivo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la estructura para las reglas SQM
 *
 * @author jchaparro
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListaDetalleDTO {
	
    private String nombreCampo;
    private String tipoDato;
    private String valor;

}
