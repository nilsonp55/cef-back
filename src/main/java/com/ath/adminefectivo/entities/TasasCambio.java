package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TASAS_CAMBIO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "TasasCambio.findAll", query = "SELECT t FROM TasasCambio t")
public class TasasCambio {
	
	@EmbeddedId
	private TasasCambioPK tasasCambioPK;
	
	@Column(name = "TASA_CAMBIO")
	private Double tasaCambio;
}
