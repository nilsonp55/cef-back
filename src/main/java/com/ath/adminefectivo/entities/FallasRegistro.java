package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;
import com.ath.adminefectivo.entities.id.FallasRegistroPK;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entidad encargada de manejar la logica de la tabla 
 *
 * @author CamiloBenavides
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "FALLAS_REGISTRO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "FallasRegistro.findAll", query = "SELECT t FROM FallasRegistro t")
public class FallasRegistro {
	
	@EmbeddedId
	private FallasRegistroPK id;
	
	@Column(name = "CONTENIDO")
	private String contenido;
	
	@Column(name = "ESTADO")
	private String estado;
	
	@Column(name = "DESCRIPCION_ERROR")
	private String descripcionError;
	
	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_CREACION")
	private Date fechaCreacion;

	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_MODIFICACION")
	private Date fechaModificacion;	
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "ID_ARCHIVO", referencedColumnName = "ID_ARCHIVO", insertable = false, updatable = false),
		@JoinColumn(name = "CONSECUTIVO_REGISTRO", referencedColumnName = "CONSECUTIVO_REGISTRO", insertable = false, updatable = false)
	})
	private RegistrosCargados registrosCargados;

}
