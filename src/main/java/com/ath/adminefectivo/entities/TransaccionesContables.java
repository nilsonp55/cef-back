package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.compuestos.ConteoContabilidadDTO;
import com.ath.adminefectivo.dto.compuestos.OperacionIntradiaDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla transacciones contables
 * @author duvan.naranjo
 *
 */

@NamedNativeQuery(name = "TransaccionesContables.cierreContablebyBanco", 
query = "SELECT tc.banco_aval as bancoAval, b.nombre_banco as nombreBancoAval, b.abreviatura as abreviaturaBancoAval, "
		+ "		tc.naturaleza as naturalezaContable, tc.cuenta_contable as  cuentaMayor, tc.cuenta_auxiliar as subAuxiliar, "
		+ "		tc.tipo_identificacion as tipoIdentificacion, tc.codigo_moneda as codigoMoneda, tc.valor as valor, tc.codigo_centro as centroCosto, "
		+ "		'' as centroBeneficio, '' as ordenCo, '' as areaFuncional, "
		+ "		tc.identificador as identificador, tc.descripcion as descripcionTransaccion, tc.id_tercero as terceroGL, tc.nombre_tercero as nombreTerceroGL, "
		+ "		tc.fecha as fechaConversion, tc.referencia1 as claveReferencia1, tc.referencia2 as claveReferencia2 "
		+ "		FROM transacciones_contables tc, cuentas_puc cp, transacciones_internas ti, bancos b"
		+ "		WHERE tc.cuenta_contable = cp.CUENTA_CONTABLE AND "
		+ "		tc.banco_aval = cp.banco_aval AND "
		+ "		cp.nombre_cuenta not like 'Transitoria%' AND "
		+ "		tc.id_transacciones_internas = ti.id_transacciones_internas AND "
		+ "		ti.estado = :estado AND "
		+ "		tc.fecha = :fecha AND "
		+ "		ti.tipo_proceso = :tipoContabilidad AND "
		+ "		tc.banco_aval = :codBanco AND "
		+ "		tc.banco_aval = b.codigo_punto ", 
resultSetMapping = "Mapping.RespuestaContableDTO")

@NamedNativeQuery(name = "TransaccionesContables.cierreContableAllBancos", 
query = "SELECT tc.banco_aval as bancoAval, b.nombre_banco as nombreBancoAval, b.abreviatura as abreviaturaBancoAval, "
		+ " tc.naturaleza as naturalezaContable, tc.cuenta_contable as  cuentaMayor, tc.cuenta_auxiliar as subAuxiliar, "
		+ "tc.tipo_identificacion as tipoIdentificacion, tc.codigo_moneda as codigoMoneda, tc.valor as valor, tc.codigo_centro as centroCosto, "
		+ "'' as centroBeneficio, '' as ordenCo, '' as areaFuncional, "
		+ " tc.identificador as identificador, tc.descripcion as descripcionTransaccion, tc.id_tercero as terceroGL, tc.nombre_tercero as nombreTerceroGL,"
		+ " tc.fecha as fechaConversion, tc.referencia1 as claveReferencia1, tc.referencia2 as claveReferencia2 "
		+ "FROM transacciones_contables tc, cuentas_puc cp, transacciones_internas ti, bancos b "
		+ "WHERE tc.cuenta_contable = cp.CUENTA_CONTABLE AND "
		+ " tc.banco_aval = cp.banco_aval AND "
		+ " cp.nombre_cuenta not like '%Transitoria%' AND "
		+ " tc.id_transacciones_internas = ti.id_transacciones_internas AND "
		+ " ti.estado = :estado AND "
		+ " tc.fecha = :fecha AND "
		+ " ti.tipo_proceso = :tipoContabilidad  AND "
		+ " tc.banco_aval = b.codigo_punto ", 
resultSetMapping = "Mapping.RespuestaContableDTO")
@SqlResultSetMapping(name = "Mapping.RespuestaContableDTO", classes = @ConstructorResult(targetClass = RespuestaContableDTO.class, columns = {
		@ColumnResult(name = "bancoAval"), @ColumnResult(name = "nombreBancoAval"), @ColumnResult(name = "abreviaturaBancoAval"),
		@ColumnResult(name = "naturalezaContable"),	@ColumnResult(name = "cuentaMayor"),	
		@ColumnResult(name = "subAuxiliar"),	@ColumnResult(name = "tipoIdentificacion"),	@ColumnResult(name = "codigoMoneda"),
		@ColumnResult(name = "valor", type = Long.class),	@ColumnResult(name = "centroCosto"),@ColumnResult(name = "centroBeneficio"),	
		@ColumnResult(name = "ordenCo"),	@ColumnResult(name = "areaFuncional"),	@ColumnResult(name = "identificador"),	
		@ColumnResult(name = "descripcionTransaccion"),	@ColumnResult(name = "terceroGL"),	@ColumnResult(name = "nombreTerceroGL"),	
		@ColumnResult(name = "fechaConversion"),	@ColumnResult(name = "claveReferencia1"),	@ColumnResult(name = "claveReferencia2") }))

@NamedNativeQuery(name = "TransaccionesContables.conteoContabilidad", 
query = "SELECT * from fnc_generar_resultado_contable(:fechaInicio, :fechaFin, :tipoProceso) ", 
resultSetMapping = "Mapping.ConteoContabilidadDTO")
@SqlResultSetMapping(name = "Mapping.ConteoContabilidadDTO", classes = @ConstructorResult(targetClass = ConteoContabilidadDTO.class, columns = {
		@ColumnResult(name = "conteoInternasGeneradas"),	@ColumnResult(name = "estadoInternasGeneradas"),	
		@ColumnResult(name = "conteoContablesGeneradas"),	@ColumnResult(name = "estadoContablesGeneradas"),
		@ColumnResult(name = "conteoErroresContables"),	@ColumnResult(name = "estadoErroresContables"),	
		@ColumnResult(name = "conteoContablesCompletadas"),	@ColumnResult(name = "estadoContablesCompletadas"),		
		}))
@Entity
@Table(name = "TRANSACCIONES_CONTABLES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "TransaccionesContables.findAll", query = "SELECT t FROM TransaccionesContables t")
public class TransaccionesContables {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TRANSACCIONES_CONTABLES")
	private Long idTransaccionesContables;
	
	
	@ManyToOne
	@JoinColumn(name = "ID_OPERACION", nullable = true)
	private OperacionesProgramadas idOperacion;
	
	@Column(name = "ID_GENERICO")
	private Integer idGenerico;
	
	@Column(name = "FECHA")
	private Date fecha;
	
	@Column(name = "CONSECUTIVO_DIA")
	private String consecutivoDia;
	
	@Column(name = "TIPO_TRANSACCION")
	private Integer tipoTransaccion;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_AVAL", nullable = true)
	private Bancos bancoAval;
	
	@Column(name = "CODIGO_CENTRO")
	private String codigoCentro;
	
	@Column(name = "NATURALEZA")
	private String naturaleza;
	
	@Column(name = "CUENTA_CONTABLE", nullable = true)
	private String cuentaContable;
	
	@Column(name = "CODIGO_MONEDA")
	private String codigoMoneda;
	
	@Column(name = "VALOR")
	private Long valor;
	
	@Column(name = "TIPO_PROCESO")
	private String tipoProceso;
	
	@Column(name = "NUMERO_COMPROBANTE")
	private String numeroComprobante;

	@Column(name = "TIPO_IDENTIFICACION")
	private String tipoIdentificacion;
	
	@Column(name = "ID_TERCERO")
	private Integer idTercero;
	
	@Column(name = "NOMBRE_TERCERO")
	private String nombreTercero;
	
	@Column(name = "IDENTIFICADOR")
	private String identificador;
	
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Column(name = "REFERENCIA1")
	private String referencia1;
	
	@Column(name = "REFERENCIA2")
	private String referencia2;
	
	@Column(name = "CUENTA_AUXILIAR")
	private String cuentaAuxiliar;
	
	@ManyToOne
	@JoinColumn(name = "ID_TRANSACCIONES_INTERNAS", nullable = true)
	private TransaccionesInternas idTransaccionesInternas;

	
}
