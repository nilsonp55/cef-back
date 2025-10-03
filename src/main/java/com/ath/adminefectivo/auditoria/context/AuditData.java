package com.ath.adminefectivo.auditoria.context;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ath.adminefectivo.entities.audit.EntityChange;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuditData {
    private String codigoProceso;
    private LocalDateTime fechaHora;
    private String ipOrigen;
    private String usuario;
    private String uri;
    private String metodo;
    private String nombreProceso;
    private String opcionMenu;
    private String idMenu;
    private Map<String, String> headers;
    private String peticion;      // JSON string truncated/masked
    private String respuesta;     // JSON string truncated/masked
    private Integer estadoHttp;
    private String mensajeRespuesta;
    private Boolean isProcess;
    private String estadoOperacion; // OK | ERROR
    @Builder.Default
    private List<EntityChange> cambios = new ArrayList<>();

    public void addChange(EntityChange change) {
        if (change != null) this.cambios.add(change);
    }
}
