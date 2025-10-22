package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
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
 * Entidad encargada de manejar la logica de la tabla DOMINIO-MAESTRO
 *
 * @author Bayron Andres Perez Muñoz
 */

@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "DOMINIO_MAESTRO")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "DominioMaestro.findAll", query = "SELECT t FROM DominioMaestro t")
public class DominioMaestro extends AuditableEntity {

	@Id
	@Column(name = "DOMINIO")
	private String dominio;
	
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Column(name = "TIPO_CONTENIDO")
	private char tipoContenido;
	
	@Column(name = "ESTADO")
	private String estado;
	
}
