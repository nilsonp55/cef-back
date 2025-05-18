package com.ath.adminefectivo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura de los archivos Liquidación
 * @author juan.ortizt
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistroOperacionConciliacionDTO {

	    private Long idRegistro;

	    private String operacionEstado;

	    private String observacion;

}
