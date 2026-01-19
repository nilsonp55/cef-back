package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla Detalles Saldos Fondos
 * @author duvan.naranjo
 *
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "DETALLES_SALDOS_FONDOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "DetallesSaldosFondos.findAll", query = "SELECT t FROM DetallesSaldosFondos t")
public class DetallesSaldosFondos {

	@Id
	@Column(name= "id_detalle_saldos_fondos")
	private Long idDetallesSaldosFondos;

	@ManyToOne
	@JoinColumn(name="id_saldos_fondos")
	private SaldosFondos saldosFondos;

	@Column(name= "calidad")
	private String calidad;

	@Column(name= "denominacion")
	private Integer denominacion;

	@Column(name= "tipo_moneda")
	private String tipoMoneda;

	@Column(name= "familia")
	private String familia;

	@Column(name= "tipo_clasifica")
	private String tipoClasifica;

	@Column(name= "valor")
	private Double valor;
	
	@Column(name = "estado")
	private int estado;
}
