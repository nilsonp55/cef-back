package com.ath.adminefectivo.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla COSTOS_TRANSPORTE
 * @author hector.mercado
 *
 */
@Entity
@Table(name = "ESTADO_CONCILIACION_PARAMETROS_LIQUIDACION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "EstadoConciliacionParametrosLiquidacion.findAll", query = "SELECT t FROM EstadoConciliacionParametrosLiquidacion t")
public class EstadoConciliacionParametrosLiquidacion {
	
	@Id
	@Column(name = "ID_LIQUIDACION")
	private Long idLiquidacion;

	@Column(name = "DATOS_PARAMETROS_LIQUIDACION_COSTOS")
	private String datosParametrosLiquidacionCostos;
	
	@Column(name = "DATOS_VALORES_LIQUIDADOS")
	private String datosValoresLiquidados;
	
	@Column(name = "DATOS_DETALLES_LIQUIDACION_COSTO")
	private String datosDetallesLiquidados;

	@Column(name = "ESTADO")
	private Integer estado;
	
	@Column(name = "DATOS_VALORES_LIQUIDADOS_PROC")
	private String datosValoresLiquidadosProc;
	
	@Column(name = "DATOS_OTROS_COSTOS_FONDO")
	private String datosOtrosCostosFondo;


}
