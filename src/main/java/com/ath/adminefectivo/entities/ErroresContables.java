package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.ath.adminefectivo.dto.RespuestaContableDTO;
import com.ath.adminefectivo.dto.compuestos.ResultadoErroresContablesDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla ErroresContables
 * @author duvan.naranjo
 *
 */
@NamedNativeQuery(name = "ErroresContables.consultarErroresContables", 
query = "SELECT abreviatura_banco as abreviaturaBancoAval, abreviatura_transportadora as abreviaturaTransportadora,fecha as fecha, tipo_transaccion as tipoTransaccion, tipo_operacion as tipoOperacion, tipo_proceso as tipoProceso, nombre_punto_origen as nombrePuntoOrigen, nombre_punto_destino as nombrePuntoDestino, codigo_comision as codigoComision, codigo_impuesto as codigoImpuesto, valor as valor, mensaje_error as mensajeError from fnc_consultar_errores_contables()", 
resultSetMapping = "Mapping.ResultadoErroresContablesDTO")
@SqlResultSetMapping(name = "Mapping.ResultadoErroresContablesDTO", classes = @ConstructorResult(targetClass = ResultadoErroresContablesDTO.class, columns = {
		@ColumnResult(name = "abreviaturaBancoAval"),	@ColumnResult(name = "abreviaturaTransportadora"),	@ColumnResult(name = "fecha"),	
		@ColumnResult(name = "tipoTransaccion"),	@ColumnResult(name = "tipoOperacion"),	@ColumnResult(name = "tipoProceso"),
		@ColumnResult(name = "nombrePuntoOrigen"),	@ColumnResult(name = "nombrePuntoDestino"),@ColumnResult(name = "codigoComision"),	
		@ColumnResult(name = "codigoImpuesto"),	@ColumnResult(name = "valor"),	@ColumnResult(name = "mensajeError") }))
@Entity
@Table(name = "ERRORES_CONTABLES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ErroresContables.findAll", query = "SELECT t FROM ErroresContables t")
public class ErroresContables {
	
	@Id
	@Column(name = "ID_ERRORES_CONTABLES")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idErroresContables;
	
	@Column(name = "ID_TRANSACCIONES_INTERNAS")
	private Long transaccionInterna;
	
	@Column(name = "FECHA")
	private Date fecha;
	
	@Column(name = "MENSAJE_ERROR")
	private String mensajeError;
	
	@Column(name = "ESTADO")
	private int estado;
	
}
