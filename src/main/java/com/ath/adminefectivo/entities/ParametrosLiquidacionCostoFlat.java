package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "PARAMETROS_LIQUIDACION_COSTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ParametrosLiquidacionCostoFlat.findAll", query = "SELECT t FROM ParametrosLiquidacionCostoFlat t")
public class ParametrosLiquidacionCostoFlat {

	@Id
	@Column(name = "ID_LIQUIDACION")
	private Long idLiquidacionFlat;

	@Column(name = "BILLETES")
	private String billetesFlat;

	@Column(name = "CODIGO_BANCO")
	private Integer codigoBancoFlat;

	@Column(name = "CODIGO_TDV")
	private String codigoTdvFlat;

	@Column(name = "ESCALA")
	private String escalaFlat;

	@Column(name = "FAJADO")
	private String fajadoFlat;

	@Column(name = "FECHA_EJECUCION")
	private Date fechaEjecucionFlat;

	@Column(name = "MONEDAS")
	private String monedasFlat;

	@Column(name = "NUMERO_BOLSAS")
	private Integer numeroBolsasFlat;

	@Column(name = "NUMERO_FAJOS")
	private Integer numeroFajosFlat;

	@Column(name = "NUMERO_PARADAS")
	private Integer numeroParadasFlat;

	@Column(name = "PUNTO_DESTINO")
	private Integer puntoDestinoFlat;

	@Column(name = "PUNTO_ORIGEN")
	private Integer puntoOrigenFlat;

	@Column(name = "RESIDUO_BILLETES")
	private Integer residuoBilletesFlat;

	@Column(name = "RESIDUO_MONEDAS")
	private Integer residuoMonedasFlat;

	@Column(name = "SEQ_GRUPO")
	private Integer seqGrupoFlat;

	@Column(name = "TIPO_OPERACION")
	private String tipoOperacionFlat;

	@Column(name = "TIPO_PUNTO")
	private String tipoPuntoFlat;

	@Column(name = "TIPO_SERVICIO")
	private String tipoServicioFlat;

	@Column(name = "VALOR_BILLETES")
	private Double valorBilletesFlat;

	@Column(name = "VALOR_MONEDAS")
	private Double valorMonedasFlat;

	@Column(name = "VALOR_TOTAL")
	private Double valorTotalFlat;

	@Column(name = "ENTRADA_SALIDA")
	private String entradaSalidaFlat;

	//@OneToOne(cascade = CascadeType.REMOVE)
	@OneToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "ID_LIQUIDACION", referencedColumnName = "ID_LIQUIDACION")
	@JsonIgnore
	private ValoresLiquidadosFlatEntity valoresLiquidadosFlatEntity;

	@Column(name = "fecha_concilia")
	private Date fechaConcilia;

	@Column(name = "codigo_propio_tdv")
	private String codigoPropioTdv;

	@Column(name = "nombre_cliente")
	private String nombreCliente;
	
	@Column(name = "TOTAL_FAJOS")
	private Double totalFajosFlat;
	
	@Column(name = "TOTAL_BOLSAS")
	private Double totalBolsasFlat;

}
