package com.ath.adminefectivo.entities;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;
import com.ath.adminefectivo.entities.audit.AuditableEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 *  Entidad encargada de manejar la logica de la tabla USUARIO
 * @author bayron.perez
 */

@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "USUARIO")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Usuario.findAll", query = "SELECT t FROM Usuario t")
public class Usuario extends AuditableEntity{
	
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

}
