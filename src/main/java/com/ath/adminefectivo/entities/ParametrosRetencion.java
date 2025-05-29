package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CONF_PARAMETROS_RETENCION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "ParametrosRetencion.findAll", query = "SELECT t FROM ParametrosRetencion t")
public class ParametrosRetencion {
	
	
	@Id
	@Column(name = "ID_PARAMETRO_RETENCION")
	private Integer idParametroRetencion;

	@Column(name = "AGRUPADOR")
	private String agrupador;
	
	@Column(name = "UBICACION")
	private String ubicacion;
	
	@Column(name = "EXTENSION")
	private String extension;
	
	@Column(name = "MASCARA_ARCH")
	private String marcaraArch;
	
	@Column(name = "PERIODO_RETENCION")
	private Integer periodoRetencion;
	
	@Column(name = "ACTIVO")
	private Boolean activo;
	
	@Column(name = "SUFIJO")
    private Boolean sufijo;
	
}
