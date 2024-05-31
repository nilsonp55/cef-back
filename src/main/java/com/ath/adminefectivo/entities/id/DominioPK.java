package com.ath.adminefectivo.entities.id;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Llave compuesta de la Entidad Dominio
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DominioPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "DOMINIO", nullable = false)
	private String dominio;

	@Column(name = "CODIGO", nullable = false)
	private String codigo;
}
