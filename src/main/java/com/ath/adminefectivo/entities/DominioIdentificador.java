package com.ath.adminefectivo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad encargada de manejar la logica de la tabla DOMINIO-MAESTRO
 *
 * @author Bayron Andres Perez Muñoz
 */

@Entity
@Table(name = "DOMINIO_IDENTIFICADOR")
@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "DominioIdentificador.findAll", query = "SELECT t FROM DominioIdentificador t")
public class DominioIdentificador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer codigo;
	
	@ManyToOne
	@JoinColumn(name = "ID_DOMINIO", nullable = false)
	private DominioMaestro dominio;
	
	@Column(name = "TIPO_CONTENIDO")
	private char tipo_contenido;
	
	@Column(name = "VALOR_TEXTO")
	private String valorTexto;
	
	@Column(name = "VALOR_NUMERO")
	private String valorNumero;
	
	@Column(name = "VALOR_FECHA")
	private Date valorFecha;
	
	@Column(name = "ESTADO")
	private Boolean estado;
}
