package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

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
@Table(name = "FALLAS_ARCHIVO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "FallasArchivo.findAll", query = "SELECT t FROM FallasArchivo t")
public class FallasArchivo {
	
	@Id
	@Column(name = "ID_FALLAS_ARCHIVO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idFallasArchivo;
	
	@Column(name = "ID_ARCHIVO")
	private Long idArchivo;
	
	@Column(name = "DESCRIPCION_ERROR")
	private String descripcionError;
	
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
	
	@OneToOne(mappedBy = "fallasArchivos", cascade = CascadeType.PERSIST)
	private ArchivosCargados archivosCargados;

}
