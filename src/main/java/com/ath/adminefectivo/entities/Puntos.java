package com.ath.adminefectivo.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.ath.adminefectivo.entities.audit.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad encargada de manejar la logica de la tabla PUNTOS
 * 
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "PUNTOS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Puntos.findAll", query = "SELECT t FROM Puntos t")
public class Puntos extends AuditableEntity {

  @Id
  @Column(name = "CODIGO_PUNTO")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer codigoPunto;

  @Column(name = "TIPO_PUNTO")
  private String tipoPunto;

  @Column(name = "NOMBRE_PUNTO")
  private String nombrePunto;

  @Column(name = "CODIGO_CIUDAD")
  private String codigoCiudad;

  @Column(name = "ESTADO")
  private String estado;

  @OneToOne(mappedBy = "punto", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  private Oficinas oficinas;

  @OneToOne(mappedBy = "punto", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  private SitiosClientes sitiosClientes;

  @OneToMany(mappedBy = "puntos", fetch = FetchType.LAZY)
  private List<PuntosCodigoTDV> puntosCodigoTDV;

  @OneToOne(mappedBy = "punto", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  private Fondos fondos;

  @OneToOne(mappedBy = "punto", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  private CajerosATM cajeroATM;

  @OneToOne(mappedBy = "punto", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  private Bancos bancos;
}
