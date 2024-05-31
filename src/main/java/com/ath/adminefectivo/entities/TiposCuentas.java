package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla TiposCuentas
 * @author cesar.castano
 *
 */

@Entity
@Table(name = "TIPOS_CUENTAS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "TiposCuentas.findAll", query = "SELECT t FROM TiposCuentas t")
public class TiposCuentas {

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
