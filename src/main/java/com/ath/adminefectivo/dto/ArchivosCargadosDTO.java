package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.validation.constraints.NotNull;
import com.ath.adminefectivo.entities.ArchivosCargados;
import com.ath.adminefectivo.utils.UtilsObjects;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura de los archivos Cargados
 * @author cesar.castano
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArchivosCargadosDTO {

	@NotNull(message = "(General.idArchivo)")
	private Long idArchivo;
	
	@NotNull(message = "(General.fechaInicioCargue)")
	private Date fechaInicioCargue;
	
	@NotNull(message = "(General.estadoCargue)")
	private String estadoCargue;
	
	@NotNull(message = "(General.nombreArchivo)")
	private String nombreArchivo;
	
	private String nombreArchivoUpper;
	
	@NotNull(message = "(General.fechaArchivo)")
	private Date fechaArchivo;
	
	@NotNull(message = "(General.fechaFinCargue)")
	private Date fechaFinCargue;
	
	private String url;
	
	private String contentType;
	
	private Integer numeroRegistros;
	
	private Integer numeroErrores;
	
	private String vigencia;
	
	private String idModeloArchivo;
	
	@NotNull(message = "(General.estado)")
	private String estado;
	
	private String usuarioCreacion;
	
	private Date fechaCreacion;
	
	private String usuarioModificacion;
	
	private Date fechaModificacion;
	
	private List<RegistrosCargadosDTO> registrosCargadosDto;
	
	/**
	 * Funcion que convierte el archivo DTO ArchivosCargadosDto a Entity ArchivosCargados
	 * @author cesar.castano
	 */
	public static final Function<ArchivosCargadosDTO, ArchivosCargados> CONVERTER_ENTITY = (ArchivosCargadosDTO t) -> {

		ArchivosCargados archivosCargados = new ArchivosCargados();
		UtilsObjects.copiarPropiedades(t, archivosCargados);		

		return archivosCargados;
	};
	
	/**
	 * Funcion que convierte la entity ArchivosCargados a archivo DTO ArchivosCargadosDto
	 * @author cesar.castano
	 */
	public static final Function<ArchivosCargados, ArchivosCargadosDTO> CONVERTER_DTO = (ArchivosCargados t) -> {

		ArchivosCargadosDTO archivosCargadosDto = new ArchivosCargadosDTO();
		UtilsObjects.copiarPropiedades(t, archivosCargadosDto);		

		return archivosCargadosDto;
	};
	
}
