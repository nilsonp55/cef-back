package com.ath.adminefectivo.dto;

import java.util.function.Function;

import com.ath.adminefectivo.entities.Rol;
import com.ath.adminefectivo.entities.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {

	private String idUsuario;

	private String nombres;

	private String apellidos;
	
	private String tipoUsario;

	private String estado;

	private RolDTO rol;

	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<Usuario, UsuarioDTO> CONVERTER_DTO = (Usuario t) -> {
		var usuarioDTO = new UsuarioDTO();
		usuarioDTO.setIdUsuario(t.getIdUsuario());
		usuarioDTO.setNombres(t.getNombres());
		usuarioDTO.setApellidos(t.getApellidos());
		usuarioDTO.setTipoUsario(t.getTipoUsario());
		usuarioDTO.setEstado(t.getEstado());
		
		usuarioDTO.setRol(RolDTO.CONVERTER_DTO.apply(t.getRol()));
		
		return usuarioDTO;
	};
	
	/**
	 * Función encargada de recibir un DTO y retornar un objeto con los mismos datos
	 */
	public static final Function<UsuarioDTO, Usuario> CONVERTER_ENTITY = (UsuarioDTO t) -> {
		var usuario = new Usuario();
		usuario.setIdUsuario(t.getIdUsuario());
		usuario.setNombres(t.getNombres());
		usuario.setApellidos(t.getApellidos());
		usuario.setTipoUsario(t.getTipoUsario());
		usuario.setEstado(t.getEstado());
		
		Rol r = new Rol();
		r.setIdRol(t.getRol().getIdRol());
		usuario.setRol(r);		
		
		return usuario;
	};

}
