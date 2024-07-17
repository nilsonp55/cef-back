package com.ath.adminefectivo.dto.compuestos;

import java.util.List;

import com.ath.adminefectivo.dto.RegistroAceptarRechazarDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO tipo lista para el transporte de informacion correspondiente a los registros para operaciones de conciliacion
 * con el campo idLiquidacion para aceptar o rechazar
 *
 * @author jose.pabon
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrosAceptarRechazarListDTO {
	
	private List<RegistroAceptarRechazarDTO> registroOperacion;
	
    private String operacion;

	private String observacion;
	
}
