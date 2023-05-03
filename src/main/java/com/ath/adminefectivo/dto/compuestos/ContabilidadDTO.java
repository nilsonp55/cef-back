package com.ath.adminefectivo.dto.compuestos;

import java.util.List;

import com.ath.adminefectivo.dto.ErroresContablesDTO;
import com.ath.adminefectivo.dto.RespuestaContableDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO encargado de retornar al front de contabilidad la informacion
 * 
 * @author prv_dnaranjo
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContabilidadDTO {
	
	private String mensaje;
	
	private ConteoContabilidadDTO conteoContabilidadDTO;
	
	private List<ErroresContablesDTO> erroresContablesDTO;
	
	private List<RespuestaContableDTO> respuestasContablesDTO;
	

}
