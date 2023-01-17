package com.ath.adminefectivo.entities.id;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Llave compuesta de la Entidad DetallesDefinicionArchivoPK
 * @author cesar.castano
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DetallesDefinicionArchivoPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "ID_ARCHIVO", nullable = false)
	private String idArchivo;
	
	@Column(name = "ID_TIPO_REGISTRO", nullable = false)
	private Integer tipoRegistro;

	@Column(name = "NUMERO_CAMPO", nullable = false)
	private Integer numeroCampo;
}
