package com.ath.adminefectivo.delegate;

import java.util.List;

import com.ath.adminefectivo.dto.compuestos.ContabilidadDTO;
import com.ath.adminefectivo.dto.compuestos.ProcesoErroresContablesDTO;
import com.ath.adminefectivo.dto.compuestos.RespuestaGenerarArchivoDTO;
import com.ath.adminefectivo.dto.compuestos.ResultadoErroresContablesDTO;

public interface IContabilidadDelegate {	
	
	/**
	 * Delegate encargado de generar la logica para generar la contabilidad
	 * 
	 * @param tipoContabilidad
	 * @return ContabilidadDTO
	 * @author duvan.naranjo
	 */
	ContabilidadDTO generarContabilidad(String tipoContabilidad);

	/**
	 * Delegate encargado de generar la logica para para consultar 
	 * los errores contables del sistema
	 * 
	 * @return List<ResultadoErroresContablesDTO>
	 * @author duvan.naranjo
	 */
	List<ResultadoErroresContablesDTO> consultarErroresContables();

	/**
	 * Delegate encargado de generar la logica para para procesar 
	 * los errores contables del sistema
	 * 
	 * @return List<ProcesoErroresContablesDTO>
	 * @author duvan.naranjo
	 */
	ProcesoErroresContablesDTO procesarErroresContables();
	
	/**
	 * Generar archivo contable para el tipo de contabilidad AM o PM
	 * 
	 * @param tipoContabilidad literal indicando si es AM o PM
	 * @param codBanco identificador del banco a genrar contabilidad en archivo
	 * @return RespuestaGenerarArchivoDTO 
	 * @author prv_nparra
	 */
	RespuestaGenerarArchivoDTO generarArchivo(String tipoContabilidad, int codBanco);
		
}