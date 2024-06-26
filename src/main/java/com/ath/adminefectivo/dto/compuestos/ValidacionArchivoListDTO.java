package com.ath.adminefectivo.dto.compuestos;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que contiene la lista con información de los archivos validados
 *
 * @author hector.mercado
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidacionArchivoListDTO {
	
	private List<ValidacionArchivoDTO> validacionArchivo;

}
