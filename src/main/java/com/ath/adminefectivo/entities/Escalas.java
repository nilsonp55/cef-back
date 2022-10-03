package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ath.adminefectivo.dto.BancosDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla Escalas
 * @author duvan.naranjo
 *
 */
@Entity
@Table(name = "ESCALAS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Escalas.findAll", query = "SELECT t FROM Escalas t")
public class Escalas {

	@Id
	@Column(name= "id_escalas")
	private int idEscala;

	@ManyToOne
	@JoinColumn(name = "codigo_banco", nullable = false)
	private Bancos bancos;
	
	@ManyToOne
	@JoinColumn(name = "tdv_origen", nullable = false)
	private Transportadoras transportadoraOrigen;
	
	@ManyToOne
	@JoinColumn(name = "ciudad_origen", nullable = false)
	private Ciudades ciudadOrigen;
	
	@ManyToOne
	@JoinColumn(name = "tdv_destino", nullable = false)
	private Transportadoras transportadoraDestino;
	
	@ManyToOne
	@JoinColumn(name = "ciudad_destino", nullable = false)
	private Ciudades ciudadDestino;

	@Column(name= "escala")
	private String escala;
	
	@Column(name = "estado")
	private int estado;
}
