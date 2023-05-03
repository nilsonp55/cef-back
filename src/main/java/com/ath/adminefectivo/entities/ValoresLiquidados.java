package com.ath.adminefectivo.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla ValoresLiquidados
 * @author cesar.castano
 *
 */

@Entity
@Table(name = "VALORES_LIQUIDADOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ValoresLiquidados.findAll", query = "SELECT t FROM ValoresLiquidados t")
public class ValoresLiquidados {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_VALORES_LIQ")
	private Long idValoresLiq;
	
	@Column(name = "CLASIFICACION_FAJADO")
	private Double clasificacionFajado;
	
	@Column(name = "CLASIFICACION_NO_FAJADO")
	private Double clasificacionNoFajado;
	
	@Column(name = "COSTO_CHARTER")
	private Double costoCharter;
	
	@Column(name = "COSTO_EMISARIO")
	private Double costoEmisario;
	
	@Column(name = "COSTO_FIJO_PARADA")
	private Double costoFijoParada;
	
	@Column(name = "COSTO_MONEDA")
	private Double costoMoneda;
	
	@Column(name = "COSTO_PAQUETEO")
	private Double costoPaqueteo;
		
	@Column(name = "MILAJE_POR_RUTEO")
	private Double milajePorRuteo;
	
	@Column(name = "MILAJE_VERIFICACION")
	private Double milajeVerificacion;
	
	@Column(name = "MONEDA_RESIDUO")
	private Double modenaResiduo;
	
	@Column(name = "TASA_AEROPORTUARIA")
	private Double tasaAeroportuaria;
	
	@Column(name = "ID_SEQ_GRUPO")
	private Integer idSeqGrupo;
	
//	@JsonIgnore
//	@OneToOne(mappedBy = "valoresLiquidados", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//	private ParametrosLiquidacionCosto parametrosLiquidacionCosto;
	
	@OneToOne(mappedBy = "valoresLiquidados", cascade = CascadeType.PERSIST)
	private ParametrosLiquidacionCosto parametrosLiquidacionCosto;
	
}
