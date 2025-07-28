package com.ath.adminefectivo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.ath.adminefectivo.entities.audit.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad encargada de manejar la logica de la tabla FONDOS
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "FONDOS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Fondos.findAll", query = "SELECT t FROM Fondos t")
public class Fondos extends AuditableEntity {

	@Id
	@Column(name = "CODIGO_PUNTO")
	private Integer codigoPunto;
	
	@OneToOne
    @JoinColumn(name = "CODIGO_PUNTO", insertable = false, unique = false)
    private Puntos punto;
	
	@Column(name = "TDV")
	private String tdv;
	
	@Column(name = "BANCO_AVAL")
	private Integer bancoAVAL;
	
	@Column(name = "NOMBRE_FONDO")
	private String nombreFondo;
	
}
