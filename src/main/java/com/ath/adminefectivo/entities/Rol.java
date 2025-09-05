package com.ath.adminefectivo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.ath.adminefectivo.entities.audit.AuditableEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 *  Entidad encargada de manejar la logica de la tabla ROL
 * @author bayron.perez
 */

@Entity
@Table(name = "ROL")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Rol.findAll", query = "SELECT t FROM Rol t")
public class Rol extends AuditableEntity  {
	
	@Id
	@Column(name = "ID_ROL")
	private String idRol;
	
	@Column(name = "NOMBRE")
	private String nombre;
	
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Column(name = "ESTADO")
	private String estado;
	
	@OneToMany(mappedBy = "rol", cascade = {CascadeType.ALL}, orphanRemoval = true)
	private List<MenuRol> menuRol;

}
