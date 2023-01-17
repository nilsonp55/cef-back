package com.ath.adminefectivo.dto.compuestos;

import java.util.List;

import com.ath.adminefectivo.dto.CiudadesDTO;
import com.ath.adminefectivo.dto.ErroresContablesDTO;
import com.ath.adminefectivo.dto.TransaccionesContablesDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO encargado de contener la informacion del del retorno de la funcion dinamica
 * 
 * @author prv_dnaranjo
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoFuncionDinamicaDTO {
		
	private Integer id_funcion;
	
	private Integer consecutivo;
	
	private String resultado;
	

}
