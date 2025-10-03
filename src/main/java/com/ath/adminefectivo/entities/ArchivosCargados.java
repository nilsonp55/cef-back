package com.ath.adminefectivo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla ARCHIVOS_CARGADOS
 *
 * @author CamiloBenavides
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "ARCHIVOS_CARGADOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ArchivosCargados.findAll", query = "SELECT t FROM ArchivosCargados t")
public class ArchivosCargados {
	
	@Id
	@Column(name = "ID_ARCHIVO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idArchivo;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_INICIO_CARGUE")
	private Date fechaInicioCargue;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_FIN_CARGUE")
	private Date fechaFinCargue;
	
	@Column(name = "ESTADO_CARGUE")
	private String estadoCargue;
	
	@Column(name = "NOMBRE_ARCHIVO")
	private String nombreArchivo;
	
	@Column(name = "NOMBRE_ARCHIVO_UPPER")
	private String nombreArchivoUpper;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_ARCHIVO")
	private Date fechaArchivo;	
	
	@Column(name = "NUMERO_REGISTROS")
	private Integer numeroRegistros;	
	
	@Column(name = "NUMERO_ERRORES")
	private Integer numeroErrores;
	
	@Column(name = "VIGENCIA")
	private String vigencia;
	
	@Column(name = "ID_MODELO_ARCHIVO")
	private String idModeloArchivo;
	
	@Column(name = "URL")
	private String url;
	
	@Column(name = "CONTENT_TYPE")
	private String contentType;
	
	@Column(name = "ESTADO")
	private String estado;
	
	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_CREACION")
	private Date fechaCreacion;

	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;
	
	@Column(name = "OBSERVACION")
	private String observacion;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_MODIFICACION")
	private Date fechaModificacion;		
	
	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "ID_ARCHIVO")
	private FallasArchivo fallasArchivos;
	
	@OneToMany(mappedBy = "archivosCargados", cascade = CascadeType.PERSIST)
	@OrderBy("CONSECUTIVO_REGISTRO ASC")
	private List<RegistrosCargados> registrosCargados;
	
}
