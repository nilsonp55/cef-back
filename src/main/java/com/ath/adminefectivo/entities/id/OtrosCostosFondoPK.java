package com.ath.adminefectivo.entities.id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.io.Serializable;
import java.util.Date;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class OtrosCostosFondoPK implements Serializable {

    @Column(name = "codigo_punto_fondo")
    private Integer codigoPuntoFondo;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_saldo")
    private Date fechaSaldo;

    @Column(name = "concepto")
    private String concepto;
}