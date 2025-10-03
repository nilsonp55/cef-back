package com.ath.adminefectivo.entities.audit;

import java.time.LocalDateTime;

import com.ath.adminefectivo.constantes.Constantes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EntityChange {
    private String entidad;
    private String idEntidad;
    private String valorAnterior;
    private String valorNuevo;
    private String operacion; // CREATE | UPDATE | DELETE

    public String getOperacion() {
        if (operacion == null) return null;
        return switch (operacion.toUpperCase()) {
	        case Constantes.POST   -> Constantes.CREAR;
	        case Constantes.PUT    -> Constantes.ACTUALIZAR;
	        case Constantes.DELETE -> Constantes.ELIMINAR;
            default -> operacion; // si viene algo inesperado
        };
    }
}