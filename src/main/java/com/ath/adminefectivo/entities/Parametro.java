package com.ath.adminefectivo.entities;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
 * Entidad encargada de manejar la logica de la tabla PARAMETRO
 *
 * @author CamiloBenavides
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "PARAMETRO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Parametro.findAll", query = "SELECT t FROM Parametro t")
public class Parametro {

	@Id
	@Column(name = "CODIGO")
	private String codigo;

	@Column(name = "DESCRIPCION")
	private String descripcion;

	@Column(name = "AREA_APLICATIVO")
	private String areaAplicativo;

	@Column(name = "VALOR")
	private String valor;

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

}
