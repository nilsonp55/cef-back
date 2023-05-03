package com.ath.adminefectivo.entities;

/**
 * Entidad encargada de manejar la logica de la tabla TARIFAS_GENERALES
 * @author cesar.castano
 *
 
@Entity
@Table(name = "TARIFAS_GENERALES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "TarifasGenerales.findAll", query = "SELECT t FROM TarifasGenerales t")
*/
public class TarifasGenerales {

	/**
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TARIFAS_GENERALES")
	private Long idTarifasGenerales;
	
	@ManyToOne
	@JoinColumn(name = "CODIGO_BANCO", nullable = false)
	private Bancos banco;

	@ManyToOne
	@JoinColumn(name = "CODIGO_TDV", nullable = false)
	private Transportadoras transportadora;
	
	@Column(name = "TIPO_PUNTO")
	private String tipoPunto;
	
	@Column(name = "CONCEPTO")
	private String concepto;
	
	@Column(name = "VALOR_MIN_RANGO")
	private String vlorMinimoRan;
	
	@Column(name = "VALOR_MAX_RANGO")
	private String valorMaximoRango;
	
	@Column(name = "VALOR_TARIFA")
	private String valorTarifa;
	
	@Column(name = "FECHA_VIGENCIA")
	private String fehaVigencia;
	*/
}
