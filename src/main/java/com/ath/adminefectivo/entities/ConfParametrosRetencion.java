package com.ath.adminefectivo.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "conf_parametros_retencion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfParametrosRetencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parametro_retencion")
    private Integer idParametroRetencion;

    @Column(name = "agrupador", nullable = false, length = 10)
    private String agrupador;

    @Column(name = "ubicacion", nullable = false, length = 200)
    private String ubicacion;

    @Column(name = "extension", nullable = false, length = 5)
    private String extension;

    @Column(name = "mascara_arch", nullable = false, length = 100)
    private String mascaraArch;

    @Column(name = "periodo_retencion", nullable = false)
    private Integer periodoRetencion;

    @Default
    @Column(name = "activo")
    private Boolean activo = true;

    @Default
    @Column(name = "sufijo", nullable = false)
    private Boolean sufijo = false;
}
