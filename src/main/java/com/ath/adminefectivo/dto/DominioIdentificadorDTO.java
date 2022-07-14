package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;
import com.ath.adminefectivo.entities.DominioIdentificador;
import com.ath.adminefectivo.entities.DominioMaestro;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que contiene el DTO de dominio identificador
 *
 * @author Bayron Andres Perez Muñoz
 */

@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DominioIdentificadorDTO {

	private Integer codigo;
	
	private DominioMaestro dominio;
	
	private char tipo_contenido;
	
	private String valorTexto;
	
	private String valorNumero;
	
	private Date valorFecha;
	
	private Boolean estado;
	
	
	/**
	 * Funcion que retorna la entidad recibiendo un DTO 
	 */
	public static final Function<DominioIdentificadorDTO, DominioIdentificador> CONVERTER_ENTITY = (DominioIdentificadorDTO t) -> {
		var dominioIdentificador = new DominioIdentificador();
		UtilsObjects.copiarPropiedades(t, dominioIdentificador);
		return dominioIdentificador;
	};

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<DominioIdentificador, DominioIdentificadorDTO> CONVERTER_DTO = (DominioIdentificador t) -> {
		var dominioIdentificadorDTO = new DominioIdentificadorDTO();
		UtilsObjects.copiarPropiedades(t, dominioIdentificadorDTO);
		return dominioIdentificadorDTO;
	};
}
