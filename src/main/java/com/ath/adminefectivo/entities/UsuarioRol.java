package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla USUARIOROL
 * @author bayron.perez
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USUARIO_ROL")
public class UsuarioRol {

	@Id
	@Column(name = "IDUSUARIO")
	private String idUsuario;
	
	@Column(name = "IDROL")
	private String idRol;
	
	@Column(name = "ESTADO")
	private String estado;
	
	@Column(name = "FECHA_CREACION")
	private String fechaCreacion;
	
	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacion;
	
	@Column(name = "FECHA_MODIFICACION")
	private String fechaModificacion;
	
	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;
}
