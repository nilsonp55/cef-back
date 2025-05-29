package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  Entidad encargada de manejar la logica de la tabla USUARIO
 * @author bayron.perez
 */

@Entity
@Table(name = "USUARIO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Usuario.findAll", query = "SELECT t FROM Usuario t")
public class Usuario {
	
	@Id
	@Column(name = "IDUSUARIO")
	private String idUsuario;
	
	@Column(name = "NOMBRES")
	private String nombres;
	
	@Column(name = "APELLIDOS")
	private String apellidos;
	
	@Column(name = "TIPO_USUARIO")
	private String tipoUsuario;
	
	@Column(name = "ESTADO")
	private String estado;
	
	@ManyToOne
	@JoinColumn(name = "ROL", referencedColumnName = "ID_ROL", nullable = false)
	private Rol rol;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_CREACION")
	private Date fechaCreacion;
	
	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacion;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_MODIFICACION")
	private Date fechaModificacion;
	
	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;

}
