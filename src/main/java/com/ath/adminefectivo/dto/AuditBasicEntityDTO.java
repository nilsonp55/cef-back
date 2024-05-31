/**
 * File AuditBasicEntityDto.java
 * 
 * @project MiMutualProtecciones. Coomeva MiMutual
 * @date 23/09/2019  
 * @package co.com.coomeva.mimutualprotecciones.dto
 */

package com.ath.adminefectivo.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import com.ath.adminefectivo.constantes.Constantes;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

/**
 * Clase que contiene las propiedades de auditoria de las entidades
 *
 * @author BayronPerez
 */
@JsonInclude(Include.NON_NULL)
@Data
@JsonIgnoreProperties(allowGetters = true, value = { "fechaCreacion", "usuarioCreacion", "fechaModificacion", "usuarioModificacion" })
public class AuditBasicEntityDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constantes.FECHA_HORA_PATTERN_DD_MM_YYYY_HH_MM_SS, timezone = Constantes.TIMEZONE)
	@JsonSerialize(as = Date.class)
	private Date fechaCreacion;

	@Size(min = 1, max = 45)
	private String usuarioCreacion;

	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constantes.FECHA_HORA_PATTERN_DD_MM_YYYY_HH_MM_SS, timezone = Constantes.TIMEZONE)
	@JsonSerialize(as = Date.class)
	private Date fechaModificacion;

	@Size(min = 1, max = 45)
	private String usuarioModificacion;

}