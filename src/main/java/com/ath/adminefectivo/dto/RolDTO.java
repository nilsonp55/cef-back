package com.ath.adminefectivo.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Function;

import com.ath.adminefectivo.entities.Rol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que contiene la estructura del archivo Dto correspondiente al ROL
 * @author bayron.perez
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolDTO {
	
	private String idRol;
	
	private String nombre;
	
	private String descripcion;
	
	private String estado;
	
	private String usuarioCreacion;
	
	private LocalDateTime fechaCreacion;

	private String usuarioModificacion;
	
	private LocalDateTime fechaModificacion;	
	
	/**
	 * Funcion Conversor de ROL de Dto a Entity
	 * @author cesar.castano
	 */
	public static final Function<RolDTO, Rol> CONVERTER_ENTITY = (RolDTO t) -> {
		var rol = new Rol();
		rol.setIdRol(t.getIdRol());
		rol.setNombre(t.getNombre());
		rol.setDescripcion(t.getDescripcion());
		rol.setEstado(t.getEstado());
		rol.setUsuarioCreacion(t.getUsuarioCreacion());
		rol.setFechaCreacion(t.getFechaCreacion());
		rol.setUsuarioModificacion(t.getUsuarioModificacion());
		rol.setFechaModificacion(t.getFechaModificacion());

		return rol;
	};
	
	/**
	 * Funcion Conversor de ROL de Entity a Dto
	 * @author cesar.castano
	 */
	public static final Function<Rol, RolDTO> CONVERTER_DTO = (Rol t) -> {
		var rolDto = new RolDTO();
		rolDto.setIdRol(t.getIdRol());
		rolDto.setNombre(t.getNombre());
		rolDto.setDescripcion(t.getDescripcion());
		rolDto.setEstado(t.getEstado());
		rolDto.setUsuarioCreacion(t.getUsuarioCreacion());
		rolDto.setFechaCreacion(t.getFechaCreacion());
		rolDto.setUsuarioModificacion(t.getUsuarioCreacion());
		rolDto.setFechaModificacion(t.getFechaModificacion());

		return rolDto;
	};
	
}
