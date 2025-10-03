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
 * Entidad encargada de manejar la logica de la tabla LOG_PROCESO_MENSUAL
 *
 * @author duvan.naranjo
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "LOG_PROCESO_MENSUAL")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "LogProcesoMensual.findAll", query = "SELECT t FROM LogProcesoMensual t")
public class LogProcesoMensual {

	@Id
	@Column(name = "ID_LOG")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idLog;

	@Column(name = "CODIGO_PROCESO")
	private String codigoProceso;

	@Column(name = "ESTADO_PROCESO")
	private String estadoProceso;
	
	@Column(name = "MES")
	private Date mes;

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_CIERRE")
	private Date fechaCierre;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_CREACION")
	private Date fechaCreacion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_MODIFICACION")
	private Date fechaModificacion;

	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacion;

	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacion;
		
	@Column(name = "ESTADO")
	private String estado;

}
