package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla SERVICIOS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "CONCILIACION_SERVICIOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ConciliacionServicios.findAll", query = "SELECT t FROM ConciliacionServicios t")
public class ConciliacionServicios {

	@Id
	@Column(name = "ID_CONCILIACION")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idConciliacion;
	
	@Column(name = "FECHA_CONCILIACION")
	private Date fechaConciliacion;
	
	@Column(name = "TIPO_CONCILIACION")
	private String tipoConciliacion;
	
	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_MODIFICACION")
	private Date fechaModificacion;
	
	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacion;
	
	@ManyToOne
	@JoinColumn(name = "ID_OPERACION")
	private OperacionesProgramadas operacionesProgramadas;
	
	@ManyToOne
	@JoinColumn(name = "ID_CERTIFICACION")
	private OperacionesCertificadas operacionesCertificadas;
	
}
