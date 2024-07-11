package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "PARAMETROS_LIQUIDACION_COSTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ParametrosLiquidacionCostoFlat.findAll", query = "SELECT t FROM ParametrosLiquidacionCostoFlat t")
public class ParametrosLiquidacionCostoFlat {

	@Id
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

	@Column(name = "PUNTO_DESTINO")
	private Integer puntoDestino;

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

	@Column(name = "ENTRADA_SALIDA")
	private String entradaSalida;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "ID_LIQUIDACION", referencedColumnName = "ID_LIQUIDACION")
	@JsonIgnore
	private ValoresLiquidadosFlatEntity valoresLiquidadosFlatEntity;

	@Column(name = "fecha_concilia")
	private Date fechaConcilia;

	@Column(name = "codigo_propio_tdv")
	private String codigoPropioTdv;

	@Column(name = "nombre_cliente")
	private String nombreCliente;

}
