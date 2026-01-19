package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * Entidad encargada de manejar la logica de la tabla PUNTOS CODIGO TDV
 * @author cesar.castano
 *
 */
@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "PUNTOS_CODIGO_TDV")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "PuntosCodigoTDV.findAll", query = "SELECT t FROM PuntosCodigoTDV t")
public class PuntosCodigoTDV extends AuditableEntity {

	@Id
	@Column(name = "id_punto_codigo_tdv")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPuntoCodigoTdv;
	
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@Column(name = "CODIGO_TDV")
	private String codigoTDV;
	
	@Column(name = "CODIGO_PROPIO_TDV", nullable = false)
	private String codigoPropioTDV;
	
	@ManyToOne
	@JoinColumn(name = "CODIGO_PUNTO", nullable = false, insertable = false, updatable = false)
	private Puntos puntos;
	
	@ManyToOne
	@JoinColumn(name = "codigo_banco", nullable = true)
	private Bancos bancos;
	
	@Column(name = "ciudad_fondo", nullable = true)
	private String ciudadFondo;
	
	
	@Column(name = "ESTADO", nullable = true)
	private int estado;
	
}
