package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entidad encargada de manejar la logica de la tabla MENUROL
 * @author cesar.castano
 *
 */
@Table(name = "MENU_ROL")
public class MenuRol {

	@Id
	@Column(name = "ID_MENU")
	private String idMenu;
	
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
