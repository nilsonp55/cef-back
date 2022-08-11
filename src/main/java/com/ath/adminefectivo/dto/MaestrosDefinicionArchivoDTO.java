package com.ath.adminefectivo.dto;

import java.util.function.Function;

import javax.validation.constraints.NotNull;

import com.ath.adminefectivo.entities.MaestroDefinicionArchivo;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del MaestrosDefinicionArchivo
 * 
 * @author cesar.castano
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaestrosDefinicionArchivoDTO {

	@NotNull(message = "(General.idArchivo)")
	private String idMaestroDefinicionArchivo;

	@NotNull(message = "(MaestrosDefinicionArchivo.descripcionArch)")
	private String descripcionArch;

	@NotNull(message = "(MaestrosDefinicionArchivo.objetivo)")
	private String objetivo;

	private String extension;

	@NotNull(message = "(MaestrosDefinicionArchivo.delimitadorBase)")
	private String delimitadorBase;

	private String delimitadorOtro;

	@NotNull(message = "(MaestrosDefinicionArchivo.multiformato)")
	private boolean multiformato;
	
	@NotNull(message = "(MaestrosDefinicionArchivo.campoMultiformato)")
	private int campoMultiformato;

	@NotNull(message = "(MaestrosDefinicionArchivo.periodicidad)")
	private char periodicidad;

	@NotNull(message = "(MaestrosDefinicionArchivo.cantidad)")
	private short cantidad;

	@NotNull(message = "(MaestrosDefinicionArchivo.mascaraArch)")
	private String mascaraArch;

	@NotNull(message = "(MaestrosDefinicionArchivo.ubicacion)")
	private String ubicacion;

	@NotNull(message = "(MaestrosDefinicionArchivo.validaEstructura)")
	private boolean validaEstructura;

	@NotNull(message = "(MaestrosDefinicionArchivo.validaContenido)")
	private boolean validaContenido;

	@NotNull(message = "(MaestrosDefinicionArchivo.cabecera)")
	private boolean cabecera;

	@NotNull(message = "(MaestrosDefinicionArchivo.controlFinal)")
	private boolean controlFinal;

	@NotNull(message = "(MaestrosDefinicionArchivo.metodo)")
	private String metodo;
	
	private boolean cantidadMinima;
	
	private int numeroCantidadMinima;

	private String agrupador;

	/**
	 * Funcion que convierte el archivo DTO MaestrosDefinicionArchivoDTO a Entity
	 * MaestrosDefinicionArchivo
	 * 
	 * @author cesar.castano
	 */
	public static final Function<MaestrosDefinicionArchivoDTO, MaestroDefinicionArchivo> CONVERTER_ENTITY = (
			MaestrosDefinicionArchivoDTO t) -> {
		var maestrosDefinicionArchivo = new MaestroDefinicionArchivo();
		UtilsObjects.copiarPropiedades(t, maestrosDefinicionArchivo);
		return maestrosDefinicionArchivo;
	};

	/**
	 * Funcion que convierte el archivo MaestrosDefinicionArchivo a Entity
	 * MaestrosDefinicionArchivoDTO
	 * 
	 * @author cesar.castano
	 */
	public static final Function<MaestroDefinicionArchivo, MaestrosDefinicionArchivoDTO> CONVERTER_DTO = (
			MaestroDefinicionArchivo t) -> {
		var maestrosDefinicionArchivoDTO = new MaestrosDefinicionArchivoDTO();
		UtilsObjects.copiarPropiedades(t, maestrosDefinicionArchivoDTO);

		return maestrosDefinicionArchivoDTO;
	};
}
