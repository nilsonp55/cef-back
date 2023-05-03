package com.ath.adminefectivo.dto.compuestos;

import java.util.List;

import com.ath.adminefectivo.dto.TransaccionesInternasDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO encargado de contener el resultado del proceso de procesar errores contables
 * 
 * @author prv_dnaranjo
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcesoErroresContablesDTO {
		
	private List<ResultadoErroresContablesDTO> ErroresContablesActuales;
	
	private List<TransaccionesInternasDTO> transaccionesInternas;
	

}
