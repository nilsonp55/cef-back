package com.ath.adminefectivo.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla COSTO_BOLSA_MONEDA_ADICIONAL
 * @author hector.mercado
 *
 */
@Entity
@Table(name = "COSTO_BOLSA_MONEDA_ADICIONAL")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "CostoBolsaMonedaAdicional.findAll", query = "SELECT t FROM CostoBolsaMonedaAdicional t")
public class CostoBolsaMonedaAdicional {
	
	@Id
	@Column(name = "ID_COSTO_BOLSA_ADICIONAL")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCostoBolsaAdicional;

	@ManyToOne
	@JoinColumn(name = "CODIGO_TDV", nullable = false)
	private Transportadoras transportadora; 

	@Column(name = "LIMITE_BOLSAS_MONEDA")
	private Integer limiteBolsasMoneda; 

	@Column(name = "VALOR_BOLSA_ADICIONAL")
	private BigDecimal valorBolsasAdicional;

	@Column(name = "ESTADO", nullable = true)
	private Integer estadoCostoBolsaMonedaAdicional;
	
	@Column(name = "USUARIO_CREACION")
	private String usuarioCreacionCostoBolsaMonedaAdicional;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_CREACION")
	private Date fechaCreacionCostoBolsaMonedaAdicional;

	@Column(name = "USUARIO_MODIFICACION")
	private String usuarioModificacionCostoBolsaMonedaAdicional;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_MODIFICACION")
	private Date fechaModificacionCostoBolsaMonedaAdicional;


}
