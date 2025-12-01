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
 * Entidad encargada de manejar la logica de la tabla TiposCuentas
 * @author cesar.castano
 *
 */

@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "TIPOS_CUENTAS")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "TiposCuentas.findAll", query = "SELECT t FROM TiposCuentas t")
public class TiposCuentas extends AuditableEntity {

	@Id
	@Column(name = "TIPO_CUENTA")
	private String tipoCuenta;
	
	@Column(name = "CUENTA_AUXILIAR")
	private String cuentaAuxiliar;
	
	@Column(name = "TIPO_ID")
	private String tipoId;
	
	@Column(name = "IDENTIFICADOR")
	private String identificador;
	
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Column(name = "REFERENCIA1")
	private String referencia1;
	
	@Column(name = "REFERENCIA2")
	private String referencia2;
	
}
