package com.ath.adminefectivo.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla ValoresLiquidados
 * @author hector.mercado
 *
 */

@Entity
@Table(name = "VALORES_LIQUIDADOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ValoresLiquidadosFlatEntity.findAll", query = "SELECT t FROM ValoresLiquidadosFlatEntity t")
public class ValoresLiquidadosFlatEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_VALORES_LIQ")
	private Long idValoresLiqFlat;
	
	@Column(name = "CLASIFICACION_FAJADO")
	private Double clasificacionFajadoFlat;
	
	@Column(name = "CLASIFICACION_NO_FAJADO")
	private Double clasificacionNoFajadoFlat;
	
	@Column(name = "COSTO_CHARTER")
	private Double costoCharterFlat;
	
	@Column(name = "COSTO_EMISARIO")
	private Double costoEmisarioFlat;
	
	@Column(name = "COSTO_FIJO_PARADA")
	private Double costoFijoParadaFlat;
	
	@Column(name = "COSTO_MONEDA")
	private Double costoMonedaFlat;
	
	@Column(name = "COSTO_PAQUETEO")
	private Double costoPaqueteoFlat;
	
	@Column(name = "ID_LIQUIDACION")
	private Long idLiquidacionFlat;	
		
	@Column(name = "MILAJE_POR_RUTEO")
	private Double milajePorRuteoFlat;
	
	@Column(name = "MILAJE_VERIFICACION")
	private Double milajeVerificacionFlat;
	
	@Column(name = "MONEDA_RESIDUO")
	private Double modenaResiduoFlat;
	
	@Column(name = "TASA_AEROPORTUARIA")
	private Double tasaAeroportuariaFlat;
	
	@Column(name = "ID_SEQ_GRUPO")
	private Integer idSeqGrupoFlat;
	
	@Column(name = "BILLETE_RESIDUO")
	private Double billeteResiduoFlat;
	
	@Column(name = "CLASIFICACION_MONEDA")
	private Double clasificacionMonedaFlat;
	
	//@OneToOne(mappedBy = "valoresLiquidadosFlatEntity", cascade = CascadeType.REMOVE)
	@OneToOne(mappedBy = "valoresLiquidadosFlatEntity", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "ID_LIQUIDACION")
	private ParametrosLiquidacionCostoFlat parametrosLiquidacionCostoFlat;
}
