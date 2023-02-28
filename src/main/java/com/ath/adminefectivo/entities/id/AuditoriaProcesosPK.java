package com.ath.adminefectivo.entities.id;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Llave compuesta de la Entidad AuditoriaProcesos
 *
 * @author duvan.naranjo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AuditoriaProcesosPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "CODIGO_PROCESO", nullable = false)
	private String codigoProceso;
	
	@Column(name = "FECHA_PROCESO", nullable = false)
	private Date fechaProceso;
}
