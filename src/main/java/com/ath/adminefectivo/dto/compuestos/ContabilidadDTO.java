package com.ath.adminefectivo.dto.compuestos;

import com.ath.adminefectivo.dto.CiudadesDTO;

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
	
	private Integer conteoInternasGeneradas;
	
	private boolean estadoInternasGeneradas;
	
	private Integer conteoContablesGeneradas;
	
	private boolean estadoContablesGeneradas;
	
	private Integer conteoErroresContables;
	
	private boolean estadoErroresContables;
	
	private Integer conteoContablesCompletadas;
	
	private boolean estadoContablesCompletadas;
	

}
