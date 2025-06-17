package com.ath.adminefectivo.entities;

import java.util.List;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad encargada de manejar la logica de la tabla PUNTOS
 * 
 * @author cesar.castano
 *
 */
@Entity
@Table(name = "PUNTOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Puntos.findAll", query = "SELECT t FROM Puntos t")
public class Puntos {

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

  @OneToOne(mappedBy = "puntos", orphanRemoval = true, fetch = FetchType.LAZY)
  private Oficinas oficinas;

  @OneToOne(mappedBy = "punto", orphanRemoval = true, fetch = FetchType.LAZY)
  private SitiosClientes sitiosClientes;

  @OneToMany(mappedBy = "puntos", fetch = FetchType.LAZY)
  private List<PuntosCodigoTDV> puntosCodigoTDV;

  @OneToOne(mappedBy = "puntos", orphanRemoval = true, fetch = FetchType.LAZY)
  private Fondos fondos;

  @OneToOne(mappedBy = "puntos", orphanRemoval = true, fetch = FetchType.LAZY)
  private CajerosATM cajeroATM;

  @OneToOne(mappedBy = "puntos", orphanRemoval = true, fetch = FetchType.LAZY)
  private Bancos bancos;
}
