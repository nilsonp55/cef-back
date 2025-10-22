package com.ath.adminefectivo.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

/**
 * Entidad encargada de manejar la logica de la tabla MENUROL
 * @author bayron.perez
 *
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "MENU_ROL")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "MenuRol.findAll", query = "SELECT t FROM MenuRol t")
public class MenuRol extends AuditableEntity {

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
	
}
