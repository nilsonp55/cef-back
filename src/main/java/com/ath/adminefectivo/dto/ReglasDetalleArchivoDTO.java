package com.ath.adminefectivo.dto;

import java.util.Objects;
import java.util.function.Function;

import javax.validation.constraints.NotNull;

import com.ath.adminefectivo.entities.ReglasDetalleArchivo;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del ReglasDetalleArchivo
 * @author cesar.castano
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReglasDetalleArchivoDTO {

	@NotNull(message = "(ReglasDetalleArchivo.idRegla)")
	private Integer idRegla;

	@NotNull(message = "(ReglasDetalleArchivo.descripcionRegla)")
	private String descripcionRegla;

	@NotNull(message = "(ReglasDetalleArchivo.tipoRegla)")
	private String tipoRegla;

	private String valorFijo;

	private String valorIncluidos;

	private String valorExcluidos;
	
	private String tablasAUsar;

	private String expresionSQL;

	private MensajesDTO mensajes;

	/**
	 * Funcion que convierte el archivo DTO DetallesDefinicionArchivoDTO a Entity
	 * DetallesDefinicionArchivo
	 * 
	 * @author duvan.naranjo
	 */
	public static final Function<ReglasDetalleArchivoDTO, ReglasDetalleArchivo> CONVERTER_ENTITY = (
			ReglasDetalleArchivoDTO t) -> {
	
		ReglasDetalleArchivo reglasDetalleArchivo = new ReglasDetalleArchivo();
		UtilsObjects.copiarPropiedades(t, reglasDetalleArchivo);
		return reglasDetalleArchivo;
	};

	/**
	 * Funcion que convierte el archivo DetallesDefinicionArchivo a 
	 * DetallesDefinicionArchivoDTO
	 * 
	 * @author duvan.naranjo
	 */
	public static final Function<ReglasDetalleArchivo, ReglasDetalleArchivoDTO> CONVERTER_DTO = (
			ReglasDetalleArchivo t) -> {
			
		ReglasDetalleArchivoDTO reglasDetalleArchivoDTO = new ReglasDetalleArchivoDTO();
		UtilsObjects.copiarPropiedades(t, reglasDetalleArchivoDTO);
		
		if(Objects.nonNull(t.getMensajes())) {
			reglasDetalleArchivoDTO.setMensajes(MensajesDTO.builder().idMensaje(t.getIdMensaje())
					.mensaje(t.getMensajes().getMensaje()).build() );
			
		}
		return reglasDetalleArchivoDTO;
	};
}
