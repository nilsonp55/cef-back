package com.ath.adminefectivo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;
import com.ath.adminefectivo.entities.id.RegistrosCargadosPK;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  Entidad encargada de manejar la logica de la tabla REGISTROS_CARGADOS
 *
 * @author CamiloBenavides
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "REGISTROS_CARGADOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "RegistrosCargados.findAll", query = "SELECT t FROM RegistrosCargados t")
public class RegistrosCargados {
	
	@EmbeddedId
	private RegistrosCargadosPK id;
	
	@Column(name = "ESTADO_REGISTRO")
	private String estadoRegistro;
	
	@Column(name = "CONTENIDO")
	private String contenido;
	
	@Column(name = "TIPO")
	private Integer tipoRegistro;
	
	@Column(name = "ESTADO")
	private String estado;
	
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
	@JoinColumn(name = "ID_ARCHIVO", insertable = false, updatable = false)
	private ArchivosCargados archivosCargados;
	
	@OneToMany(mappedBy = "registrosCargados", cascade = CascadeType.PERSIST)
	private List<FallasRegistro> fallasRegistro;


}
