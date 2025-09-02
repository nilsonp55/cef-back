package com.ath.adminefectivo.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.ath.adminefectivo.entities.audit.AuditableEntity;
import com.ath.adminefectivo.entities.id.DominioPK;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad encargada de manejar la logica de la tabla DOMINIO
 *
 * @author CamiloBenavides
 */
@Entity
@Table(name = "DOMINIO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Dominio.findAll", query = "SELECT t FROM Dominio t")
public class Dominio extends AuditableEntity {

	@EmbeddedId
	private DominioPK dominioPK;

	@Column(name = "DESCRIPCION")
	private String descripcion;

	@Column(name = "TIPO")
	private String tipo;

	@Column(name = "VALOR_TEXTO")
	private String valorTexto;

	@Column(name = "VALOR_NUMERO")
	private Double valorNumero;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "VALOR_FECHA")
	private Date valorFecha;

	@Column(name = "ESTADO")
	private String estado;

}
