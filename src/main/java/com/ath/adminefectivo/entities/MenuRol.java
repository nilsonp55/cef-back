package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla MENUROL
 * @author bayron.perez
 *
 */
@Entity
@Table(name = "MENU_ROL")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "MenuRol.findAll", query = "SELECT t FROM MenuRol t")
public class MenuRol {

	@Id
	@Column(name = "CODIGO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codigo;

	@ManyToOne
	@JoinColumn(name = "ID_MENU", nullable = false)
	private Menu menu;
	
	@ManyToOne
	@JoinColumn(name = "ID_ROL", nullable = false)
	private Rol rol;
	
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
