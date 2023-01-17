package com.ath.adminefectivo.entities;

import java.util.Date;
import java.util.List;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad encargada de manejar la logica de la tabla DETALLES_OPERACIONES_PROGRAMADAS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "DETALLES_OPERACIONES_PROGRAMADAS")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "DetalleOperacionesProgramadas.findAll", query = "SELECT t FROM DetalleOperacionesProgramadas t")
public class DetalleOperacionesProgramadas {

	@Id
	@Column(name = "ID_DETALLE_OPERACION_PROGRAMADA")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idDetalleOperacion;
	
	@Column(name = "DENOMINACION")
	private String denominacion;
	
	@Column(name = "CALIDAD")
	private Integer calidad;
	
	@Column(name = "FAMILIA")
	private String familia;
	
	@Column(name = "VALOR")
	private Double valorDetalle;
	
	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;
	
	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_CREACION")
	private Date fechaCreacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_MODIFICACION")
	private Date fechaModificacion;
	
	@ManyToOne
	@JoinColumn(name = "ID_OPERACION", insertable = true, updatable = true)
	private OperacionesProgramadas operacionesProgramadas;
	
}
