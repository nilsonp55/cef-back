package com.ath.adminefectivo.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "V_TARIFAS_ESPECIALES_CLIENTE", schema = "controlefect")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VTarifasEspecialesClienteEntity {

    @Id
    @Column(name = "ID_TARIFA_ESPECIAL")
    private Long idTarifaEspecial;

    @Column(name = "CODIGO_BANCO")
    private Integer codigoBanco;

    @Column(name = "ABREVIATURA")
    private String abreviatura;

    @Column(name = "NOMBRE_BANCO")
    private String nombreBanco;

    @Column(name = "CODIGO_TDV", length = 5)
    private String codigoTdv;

    @Column(name = "NOMBRE_TRANSPORTADORA")
    private String nombreTransportadora;

    @Column(name = "CODIGO_CLIENTE")
    private Integer codigoCliente;

    @Column(name = "IDENTIFICACION")
    private String identificacion;

    @Column(name = "NOMBRE_CLIENTE")
    private String nombreCliente;

    @Column(name = "CODIGO_DANE", length = 5)
    private String codigoDane;

    @Column(name = "NOMBRE_CIUDAD")
    private String nombreCiudad;

    @Column(name = "CODIGO_PUNTO")
    private Integer codigoPunto;

    @Column(name = "NOMBRE_PUNTO")
    private String nombrePunto;

    @Column(name = "TIPO_PUNTO")
    private String tipoPunto;

    @Column(name = "TIPO_OPERACION", length = 50)
    private String tipoOperacion;

    @Column(name = "TIPO_SERVICIO", length = 30)
    private String tipoServicio;

    @Column(name = "TIPO_COMISION", length = 30)
    private String tipoComision;

    @Column(name = "UNIDAD_COBRO", length = 30)
    private String unidadCobro;

    @Column(name = "ESCALA", length = 50)
    private String escala;

    @Column(name = "BILLETES", length = 5)
    private String billetes;

    @Column(name = "MONEDAS", length = 5)
    private String monedas;

    @Column(name = "FAJADO", length = 5)
    private String fajado;

    @Column(name = "VALOR_TARIFA", precision = 16, scale = 6)
    private BigDecimal valorTarifa;

    @Column(name = "FECHA_INICIO_VIGENCIA")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioVigencia;

    @Column(name = "FECHA_FIN_VIGENCIA")
    @Temporal(TemporalType.DATE)
    private Date fechaFinVigencia;

    @Column(name = "LIMITE_COMISION_APLICAR")
    private Integer limiteComisionAplicar;

    @Column(name = "VALOR_COMISION_ADICIONAL")
    private BigDecimal valorComisionAdicional;

    @Column(name = "USUARIO_CREACION", length = 50)
    private String usuarioCreacion;

    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Column(name = "USUARIO_MODIFICACION", length = 50)
    private String usuarioModificacion;

    @Column(name = "FECHA_MODIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    @Column(name = "ESTADO")
    private boolean estado;
    
    @Column(name = "REGLA_EDICION", length = 50)
    private String reglaEdicion;
}
