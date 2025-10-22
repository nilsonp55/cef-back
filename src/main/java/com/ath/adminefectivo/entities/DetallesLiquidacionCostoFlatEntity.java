package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla DetallesLiquidacionCosto
 * @author jose.pabon
 *
 */

@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "DETALLES_LIQUIDACION_COSTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "DetallesLiquidacionCostoFlatEntity.findAll", query = "SELECT t FROM DetallesLiquidacionCostoFlatEntity t")
public class DetallesLiquidacionCostoFlatEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_DETALLE_LIQ")
	private Long idDetalleLiqFlat;
	
	@Column(name = "BILLETE_MONEDA")
	private String billeteMonedaFlat;
	
	@Column(name = "CALIDAD")
	private String calidadFlat;
	
	@Column(name = "DENOMINACION")
	private String denominacionFlat;

	@Column(name = "ID_LIQUIDACION")
	private Long idLiquidacionFlat;
	
	@Column(name = "NUMERO_FAJOS_BOLSAS")
	private Integer numeroFajosBolsasFlat;
	
	@Column(name = "RESIDUO")
	private Integer residuoFlat;
	
	@Column(name = "VALOR_DENOMINACION")
	private Double valorDenominacionFlat;	
}
