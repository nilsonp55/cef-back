package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.FallasArchivo;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene el DTO de la entidad FallasArchivo
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FallasArchivoDTO {

	private Long idFallasArchivo;	
	private Long idArchivo;	
	private String descripcionError;	
	private String estado;	
	private String usuarioCreacion;	
	private Date fechaCreacion;
	private String usuarioModificacion;	
	private Date fechaModificacion;	

	/**
	 * Funcion que retorna la entidad recibiendo un DTO *
	 */
	public static final Function<FallasArchivoDTO, FallasArchivo> CONVERTER_ENTITY = (FallasArchivoDTO t) -> {
		var fallasArchivo = new FallasArchivo();
		UtilsObjects.copiarPropiedades(t, fallasArchivo);
		return fallasArchivo;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<FallasArchivo, FallasArchivoDTO> CONVERTER_DTO = (FallasArchivo t) -> {
		var fallasArchivoDTO = new FallasArchivoDTO();
		UtilsObjects.copiarPropiedades(t, fallasArchivoDTO);
		return fallasArchivoDTO;
	};
}
