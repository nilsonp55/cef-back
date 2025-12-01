package com.ath.adminefectivo.auditoria.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Catálogo de descripciones de códigos HTTP para auditoría.
 * Se puede extender fácilmente agregando más códigos según necesidad.
 */
public enum HttpStatusDescripcion {

    // 2xx Success
    OK(200, "EXITOSO"),
    CREATED(201, "CREADO"),
    ACCEPTED(202, "ACEPTADO"),
    NO_CONTENT(204, "SIN CONTENIDO"),

    // 4xx Client errors
    BAD_REQUEST(400, "SOLICITUD INCORRECTA"),
    UNAUTHORIZED(401, "NO AUTORIZADO"),
    FORBIDDEN(403, "PROHIBIDO"),
    NOT_FOUND(404, "NO ENCONTRADO"),
    CONFLICT(409, "CONFLICTO"),
    UNPROCESSABLE_ENTITY(422, "ENTIDAD NO PROCESABLE"),

    // 5xx Server errors
    INTERNAL_SERVER_ERROR(500, "ERROR INTERNO DEL SERVIDOR"),
    NOT_IMPLEMENTED(501, "NO IMPLEMENTADO"),
    BAD_GATEWAY(502, "PUERTA DE ENLACE INCORRECTA"),
    SERVICE_UNAVAILABLE(503, "SERVICIO NO DISPONIBLE"),
    GATEWAY_TIMEOUT(504, "TIEMPO DE ESPERA DE LA PUERTA DE ENLACE"),

    // fallback
    UNKNOWN(-1, "DESCONOCIDO");

    private final int codigo;
    private final String descripcion;

    HttpStatusDescripcion(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static String descripcionDe(int codigo) {
        for (HttpStatusDescripcion status : values()) {
            if (status.codigo == codigo) {
                return status.descripcion + " (" + codigo + ")";
            }
        }
        return "Código HTTP desconocido (" + codigo + ")";
    }
}