package com.ath.adminefectivo.entities.id;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Llave compuesta de la Entidad RegistrosCargados
 *
 * @author CamiloBenavides
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RegistrosCargadosPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ID_ARCHIVO")
	private Long idArchivo;

	@Column(name = "CONSECUTIVO_REGISTRO")
	private Integer consecutivoRegistro;

}
