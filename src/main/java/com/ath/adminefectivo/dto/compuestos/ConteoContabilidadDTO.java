package com.ath.adminefectivo.dto.compuestos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO encargado de contener la informacion del conteo del proceso de contabilidad
 * 
 * @author prv_dnaranjo
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConteoContabilidadDTO {
		
	private Integer conteoInternasGeneradas;
	
	private boolean estadoInternasGeneradas;
	
	private Integer conteoContablesGeneradas;
	
	private boolean estadoContablesGeneradas;
	
	private Integer conteoErroresContables;
	
	private boolean estadoErroresContables;
	
	private Integer conteoContablesCompletadas;
	
	private boolean estadoContablesCompletadas;
	

}
