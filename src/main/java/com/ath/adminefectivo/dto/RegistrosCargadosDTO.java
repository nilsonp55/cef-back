package com.ath.adminefectivo.dto;

import java.util.Date;
import java.util.function.Function;

import javax.validation.constraints.NotNull;

import com.ath.adminefectivo.entities.RegistrosCargados;
import com.ath.adminefectivo.entities.id.RegistrosCargadosPK;
import com.ath.adminefectivo.utils.UtilsObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura de los registros Cargados
 * @author cesar.castano
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrosCargadosDTO {
	
	private RegistrosCargadosPK id;
	
	@NotNull(message = "(General.estadoRegistro)")
	private String estadoRegistro;
	
	private String contenido;
	
	private String tipo;
	
	@NotNull(message = "(General.estado)")
	private String estado;
	
	private String usuarioCreacion;
	
	private Date fechaCreacion;
	
	private String usuarioModificacion;
	
	private Date fechaModificacion;

	/**
     * Funcion que convierte el archivo DTO RegistrosCargadosDTO a Entity RegistrosCargados
     * @author cesar.castano
     */
    public static final Function<RegistrosCargadosDTO, RegistrosCargados> CONVERTER_ENTITY = (RegistrosCargadosDTO t) -> {
        RegistrosCargados registrosCargados = new RegistrosCargados();
        UtilsObjects.copiarPropiedades(t, registrosCargados);       
        return registrosCargados;
    };
   
    /**
     * Funcion que convierte la entity RegistrosCargados a archivo DTO RegistrosCargadosDTO
     * @author cesar.castano
     */
    public static final Function<RegistrosCargados, RegistrosCargadosDTO> CONVERTER_DTO = (RegistrosCargados t) -> {
        RegistrosCargadosDTO registrosCargadosDto = new RegistrosCargadosDTO();
        UtilsObjects.copiarPropiedades(t, registrosCargadosDto);       
        return registrosCargadosDto;
    };
	
}
