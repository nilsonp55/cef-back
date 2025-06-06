package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

import com.ath.adminefectivo.entities.ErroresContables;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de errores contables
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErroresContablesDTO {

	private Integer idErroresContables;
	
	private TransaccionesInternasDTO transaccionInterna;
	
	private Date fecha;
	
	private String mensajeError;
	
	private int estado;
	
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<ErroresContablesDTO, ErroresContables> CONVERTER_ENTITY = (ErroresContablesDTO t) -> {
		var errores = new ErroresContables();
		UtilsObjects.copiarPropiedades(t, errores);
		if(!Objects.isNull(t.getTransaccionInterna())) {
			errores.setTransaccionInterna(TransaccionesInternasDTO.CONVERTER_ENTITY.apply(t.getTransaccionInterna()));
		}
		return errores;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<ErroresContables, ErroresContablesDTO> CONVERTER_DTO = (ErroresContables t) -> {
		var erroresContablesDTO = new ErroresContablesDTO();
		UtilsObjects.copiarPropiedades(t, erroresContablesDTO);
		if(!Objects.isNull(t.getTransaccionInterna())) {
			erroresContablesDTO.setTransaccionInterna(TransaccionesInternasDTO.CONVERTER_DTO.apply(t.getTransaccionInterna()));
		}
		return erroresContablesDTO;
	};
}
