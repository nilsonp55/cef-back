package com.ath.adminefectivo.dto.compuestos;

import java.util.List;

import com.ath.adminefectivo.dto.ArchivosLiquidacionDTO;
import com.ath.adminefectivo.dto.RegistroOperacionConciliacionDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO tipo lista para el transporte de informacion correspondiente a los registros para operaciones de conciliacion
 *
 * @author hector mercado
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrosConciliacionListDTO {
	
	private List<RegistroOperacionConciliacionDTO> registroOperacion;
	
    private String operacion;

	private String observacion;
	
}
