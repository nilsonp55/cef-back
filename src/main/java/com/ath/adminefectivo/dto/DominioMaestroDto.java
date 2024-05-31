package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.DominioMaestro;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que contiene el DTO de dominio maestro
 *
 * @author Bayron Andres Perez Muñoz
 */

@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DominioMaestroDto {

	private String dominio;
	
	private String descripcion;
	
	private char tipoContenido;
	
	private String estado;
	
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO 
	 */
	public static final Function<DominioMaestroDto, DominioMaestro> CONVERTER_ENTITY = (DominioMaestroDto t) -> {
		var dominioMaestro = new DominioMaestro();
		UtilsObjects.copiarPropiedades(t, dominioMaestro);
		return dominioMaestro;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<DominioMaestro, DominioMaestroDto> CONVERTER_DTO = (DominioMaestro t) -> {
		var dominioMaestroDto = new DominioMaestroDto();
		UtilsObjects.copiarPropiedades(t, dominioMaestroDto);
		return dominioMaestroDto;
	};

}
