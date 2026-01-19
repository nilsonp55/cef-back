package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla LOG_PROCESO_DIARIO
 *
 * @author CamiloBenavides
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "LOG_PROCESO_DIARIO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "LogProcesoDiario.findAll", query = "SELECT t FROM LogProcesoDiario t")
public class LogProcesoDiario {

	@Id
	@Column(name = "ID_LOG_PROCESO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idLogProceso;

	@Column(name = "CODIGO_PROCESO")
	private String codigoProceso;

	@Column(name = "ESTADO_PROCESO")
	private String estadoProceso;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_FINALIZACION")
	private Date fechaFinalizacion;

	@Column(name = "ESTADO")
	private String estado;

	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_CREACION")
	private Date fechaCreacion;

	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_MODIFICACION")
	private Date fechaModificacion;

}
