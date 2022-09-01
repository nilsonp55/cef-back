package com.ath.adminefectivo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla ParametrosLiquidacionCosto
 * @author cesar.castano
 *
 */

@Entity
@Table(name = "PARAMETROS_LIQUIDACION_COSTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ParametrosLiquidacionCosto.findAll", query = "SELECT t FROM ParametrosLiquidacionCosto t")
public class ParametrosLiquidacionCosto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_LIQUIDACION")
	private Long idLiquidacion;
	
	@Column(name = "BILLETES")
	private String billetes;
	
	@Column(name = "CODIGO_BANCO")
	private Integer codigoBanco;
	
	@Column(name = "CODIGO_TDV")
	private String codigoTdv;
	
	@Column(name = "ESCALA")
	private String escala;
	
	@Column(name = "FAJADO")
	private String fajado;
	
	@Column(name = "FECHA_EJECUCION")
	private Date fechaEjecucion;
	
	@Column(name = "MONEDAS")
	private String monedas;
	
	@Column(name = "NUMERO_BOLSAS")
	private Integer numeroBolsas;
	
	@Column(name = "NUMERO_FAJOS")
	private Integer numeroFajos;
	
	@Column(name = "NUMERO_PARADAS")
	private Integer numeroParadas;
	
	@Column(name = "PUNTO_ORIGEN")
	private Integer puntoOrigen;
	
	@Column(name = "RESIDUO_BILLETES")
	private Integer residuoBilletes;
	
	@Column(name = "RESIDUO_MONEDAS")
	private Integer residuoMonedas;
	
	@Column(name = "SEQ_GRUPO")
	private Integer seqGrupo;
	
	@Column(name = "TIPO_OPERACION")
	private String tipoOperacion;
	
	@Column(name = "TIPO_PUNTO")
	private String tipoPunto;
	
	@Column(name = "TIPO_SERVICIO")
	private String tipoServicio;
	
	@Column(name = "VALOR_BILLETES")
	private Double valorBilletes;
	
	@Column(name = "VALOR_MONEDAS")
	private Double valorMonedas;
	
	@Column(name = "VALOR_TOTAL")
	private Double valorTotal;
	
	@OneToMany(mappedBy = "parametrosLiquidacionCosto", cascade = CascadeType.PERSIST)
	private List<DetallesLiquidacionCosto> detallesLiquidacionCosto;
	
	@OneToOne
	@JoinColumn(name = "ID_LIQUIDACION")
	private ValoresLiquidados valoresLiquidados;
}
