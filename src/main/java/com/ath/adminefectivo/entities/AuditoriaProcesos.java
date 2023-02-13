package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ath.adminefectivo.entities.id.AuditoriaProcesosPK;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla AuditoriaProcesos
 * @author duvan.naranjo
 *
 */
@Entity
@Table(name = "AUDITORIA_PROCESOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "AuditoriaProcesos.findAll", query = "SELECT t FROM AuditoriaProcesos t")
public class AuditoriaProcesos {
	
	@EmbeddedId
	private AuditoriaProcesosPK id;
	
	@Column(name = "ESTADO_PROCESO")
	private String estadoProceso;
	
	@Column(name = "MENSAJE")
	private String mensaje;
		
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
	
}
