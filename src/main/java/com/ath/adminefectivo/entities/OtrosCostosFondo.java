package com.ath.adminefectivo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;

import com.ath.adminefectivo.auditoria.listener.AuditoriaEntityListener;
import com.ath.adminefectivo.entities.id.OtrosCostosFondoPK;

import java.util.Date;

@Entity
@EntityListeners(AuditoriaEntityListener.class)
@Table(name = "otros_costos_fondo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtrosCostosFondo {

    @EmbeddedId
    private OtrosCostosFondoPK id;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_liquidacion")
    private Date fechaLiquidacion;

    @Column(name = "valor_liquidado")
    private Double valorLiquidado;

    @Column(name = "id_seq_grupo")
    private Integer idSeqGrupo;
}