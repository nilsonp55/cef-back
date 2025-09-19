package com.ath.adminefectivo.entities.audit;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntityChange {
    private String entidad;
    private String idEntidad;
    private String valorAnterior; // JSON truncated
    private String valorNuevo;    // JSON truncated
    private String operacion;     // CREATE | UPDATE | DELETE
}