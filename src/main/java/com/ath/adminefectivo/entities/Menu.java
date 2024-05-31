package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  Entidad encargada de manejar la logica de la tabla MENU
 *
 * @author CamiloBenavides
 */
@Entity
@Table(name = "MENU")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Menu.findAll", query = "SELECT t FROM Menu t")
public class Menu {

		@Id
		@Column(name = "ID_MENU")
		private String idMenu;
		
		@Column(name = "ID_MENU_PADRE")
		private String idMenuPadre;
		
		@Column(name = "NOMBRE")
		private String nombre;
		
		@Column(name = "TIPO")
		private String tipo;
		
		@Column(name = "ICONO")
		private String icono;
		
		@Column(name = "URL")
		private String url;
		
		@Column(name = "ESTADO")
		private String estado;
		
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
