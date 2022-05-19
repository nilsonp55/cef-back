package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.DetallesDefinicionArchivo;
import com.ath.adminefectivo.entities.id.DetallesDefinicionArchivoPK;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del DetallesDefinicionArchivoDTO
 * @author duvan.naranjo
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetallesDefinicionArchivoDTO {
	
	private DetallesDefinicionArchivoPK id;

//	@NotNull(message = "(DetallesDefinicionArchivo.nombreCampo)")
	private String nombreCampo;

//	@NotNull(message = "(DetallesDefinicionArchivo.tipoDato)")
	private String tipoDato;

	private Integer decimales;

//	@NotNull(message = "(DetallesDefinicionArchivo.requeridos)")
	private boolean requeridos;

//	@NotNull(message = "(DetallesDefinicionArchivo.validarReglas)")
	private boolean validarReglas;

//	@NotNull(message = "(DetallesDefinicionArchivo.multiplesReglas)")
	private boolean multiplesReglas;

	private String expresionRegla;

	/**
	 * Funcion que convierte el archivo DTO DetallesDefinicionArchivoDTO a Entity DetallesDefinicionArchivo
	 * @author duvan.naranjo
	 */
	public static final Function<DetallesDefinicionArchivoDTO, DetallesDefinicionArchivo> CONVERTER_ENTITY = (DetallesDefinicionArchivoDTO t) -> {
		DetallesDefinicionArchivo detallesDefinicionArchivo = new DetallesDefinicionArchivo();
		UtilsObjects.copiarPropiedades(t, detallesDefinicionArchivo);	

		return detallesDefinicionArchivo;
	};
	
	/**
	 * Funcion que convierte el archivo DetallesDefinicionArchivo a Entity DetallesDefinicionArchivoDTO
	 * @author duvan.naranjo
	 */
	public static final Function<DetallesDefinicionArchivo, DetallesDefinicionArchivoDTO> CONVERTER_DTO = (DetallesDefinicionArchivo t) -> {
		DetallesDefinicionArchivoDTO detallesDefinicionArchivoDTO = new DetallesDefinicionArchivoDTO();
		UtilsObjects.copiarPropiedades(t, detallesDefinicionArchivoDTO);		

		return detallesDefinicionArchivoDTO;
	};
}
