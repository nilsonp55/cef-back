package com.ath.adminefectivo.entities.audit;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_log_process", schema = "controlefect")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogProcessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log_process")
    private Long idLogProcessProc;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHoraProc;

    @Column(name = "ip_origen")
    private String ipOrigenProc;

    @Column(name = "usuario")
    private String usuarioProc;

    @Column(name = "opcion_menu")
    private String opcionMenuProc;

    @Column(name = "accion_http")
    private String accionHttpProc;

    @Column(name = "codigo_proceso", columnDefinition = "uuid")
    private UUID codigoProcesoProc;

    @Column(name = "nombre_proceso")
    private String nombreProcesoProc;

    @Column(name = "estado_http")
    private String estadoHttpProc;

    @Column(name = "estado_operacion")
    private String estadoOperacionProc;

    @Column(name = "mensaje_respuesta")
    private String mensajeRespuestaProc;

    @Column(name = "peticion", columnDefinition = "text")
    private String peticionProc;

    @Column(name = "respuesta", columnDefinition = "text")
    private String respuestaProc;

    @Column(name = "valor_anterior", columnDefinition = "text")
    private String valorAnteriorProc;

    @Column(name = "valor_nuevo", columnDefinition = "text")
    private String valorNuevoProc;
}