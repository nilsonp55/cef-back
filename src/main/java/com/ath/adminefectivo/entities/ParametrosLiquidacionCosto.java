package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;
import com.ath.adminefectivo.dto.compuestos.EstimadoClasificacionCostosDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla ParametrosLiquidacionCosto
 * 
 * @author cesar.castano
 *
 */
@NamedNativeQuery(name = "ParametrosLiquidacionCosto.consultaEstimadosCostos", 
		query = "SELECT "
		+ "plc.codigo_banco as bancoAval, " + "plc.codigo_tdv as tdv, " + "SUM(plc.numero_fajos) AS estimadaFajos, "
		+ "SUM(plc.numero_bolsas) AS estimadaBolsas " + "FROM " + " parametros_liquidacion_costo plc " + "WHERE "
		+ "codigo_banco = :bancoAval AND " + "plc.codigo_tdv = :transportadora AND "
		+ "plc.entrada_salida = 'ENTRADA' and " + "extract(year from plc.fecha_ejecucion) = :anio and "
		+ "extract(month  from plc.fecha_ejecucion) = :mes "
		+ "GROUP BY plc.codigo_banco, plc.codigo_tdv ", 
		resultSetMapping = "Mapping.EstimadoClasificacionCostosDTO")
@SqlResultSetMapping(name = "Mapping.EstimadoClasificacionCostosDTO", 
		classes = @ConstructorResult(targetClass = EstimadoClasificacionCostosDTO.class, columns = {
		@ColumnResult(name = "bancoAval"), @ColumnResult(name = "tdv"),
		@ColumnResult(name = "estimadaFajos", type = Long.class),
		@ColumnResult(name = "estimadaBolsas", type = Long.class) }))

@Entity
@EntityListeners(AuditoriaEntityListener.class)
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

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "ID_LIQUIDACION", referencedColumnName = "ID_LIQUIDACION")
	@JsonIgnore
	private ValoresLiquidados valoresLiquidados;

	@Column(name = "fecha_concilia")
	private Date fechaConcilia;

	@Column(name = "codigo_propio_tdv")
	private String codigoPropioTdv;

	@Column(name = "nombre_cliente")
	private String nombreCliente;
	
	@Column(name = "TOTAL_FAJOS")
	private Double totalFajos;

	@Column(name = "TOTAL_BOLSAS")
	private Double totalBolsas;

}
