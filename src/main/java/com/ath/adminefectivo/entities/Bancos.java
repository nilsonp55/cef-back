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
 * Entidad encargada de manejar la logica de la tabla BANCOS
 * 
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "BANCOS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Bancos.findAll", query = "SELECT t FROM Bancos t")
public class Bancos extends AuditableEntity {

  @Id
  @Column(name = "CODIGO_PUNTO")
  private Integer codigoPunto;
  
  @OneToOne
  @JoinColumn(name = "CODIGO_PUNTO", insertable = false, unique = false)
  private Puntos punto;

  @Column(name = "ES_AVAL")
  private Boolean esAVAL;

  @Column(name = "CODIGO_COMPENSACION")
  private Integer codigoCompensacion;

  @Column(name = "NUMERO_NIT")
  private String numeroNit;

  @Column(name = "ABREVIATURA")
  private String abreviatura;

  @Column(name = "NOMBRE_BANCO")
  private String nombreBanco;

  @Column(name = "COBRA_INTRADAY")
  private Boolean cobraIntraday;

}
