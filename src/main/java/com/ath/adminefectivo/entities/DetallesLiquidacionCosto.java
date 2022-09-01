package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla DetallesLiquidacionCosto
 * @author cesar.castano
 *
 */

@Entity
@Table(name = "DETALLES_LIQUIDACION_COSTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "DetallesLiquidacionCosto.findAll", query = "SELECT t FROM DetallesLiquidacionCosto t")
public class DetallesLiquidacionCosto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_DETALLE_LIQ")
	private Long idDetalleLiq;
	
	@Column(name = "BILLETE_MONEDA")
	private String billeteMoneda;
	
	@Column(name = "CALIDAD")
	private String calidad;
	
	@Column(name = "DENOMINACION")
	private String denominacion;
	
	@Column(name = "NUMERO_FAJOS_BOLSAS")
	private Integer numeroFajosBolsas;
	
	@Column(name = "RESIDUO")
	private Integer residuo;
	
	@Column(name = "VALOR_DENOMINACION")
	private Double valorDenominacion;
	
	@ManyToOne
	@JoinColumn(name = "ID_LIQUIDACION", nullable = false)
	private ParametrosLiquidacionCosto parametrosLiquidacionCosto;
	
}
