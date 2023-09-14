package com.ath.adminefectivo.entities;

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
 * Entidad encargada de manejar la logica de la tabla ClasificacionCostos
 * @author duvan.naranjo
 *
 */

@Entity
@Table(name = "COSTOS_CLASIFICACION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ClasificacionCostos.findAll", query = "SELECT t FROM ClasificacionCostos t")
public class ClasificacionCostos {

	@Id
	@Column(name= "id_costos_clasificacion")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idCostosClasificacion;
	
	@Column(name= "banco_aval")
	private int bancoAval;
	
	@Column(name= "tdv")
	private String transportadora;
	
	@Column(name= "mes_año")
	private String mesAnio;
	
	@Column(name = "fecha_modificacion")
	private Date fechaModificacion;
	
	@Column(name = "usuario_modificacion")
	private String usuarioModificacion;
	
	@Column(name = "fajos_estimados")
	private int fajosEstimados;
	
	@Column(name = "bolsas_estimadas")
	private int bolsasEstimadas;
	
	@Column(name = "cantidad_fajos")
	private int cantidadFajos;
	
	@Column(name = "cantidad_rem")
	private int cantidadRem;
	
	@Column(name = "cantidad_monedas")
	private int cantidadMonedas;
	
	@Column(name = "valor_clasif_fajos")
	private int valorClasifFajos;
	
	@Column(name = "valor_clasif_rem")
	private int valorClasifRem;
	
	@Column(name = "valor_clasif_monedas")
	private int valorClasifMonedas;
		
	@Column(name = "estado")
	private int estado;
}
