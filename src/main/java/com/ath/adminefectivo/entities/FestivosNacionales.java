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
 * Entidad encargada de manejar la logica de la tabla FESTIVOS_NACIONALES
 *
 * @author CamiloBenavides
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "FESTIVOS_NACIONALES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "FestivosNacionales.findAll", query = "SELECT t FROM FestivosNacionales t")
public class FestivosNacionales {

	@Id
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA")
	private Date fecha;

	@Column(name = "DESCRIPCION")
	private String descripcion;

}
