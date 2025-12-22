package com.ath.adminefectivo.dto;

import java.time.LocalDateTime;
import java.util.function.Function;

import com.ath.adminefectivo.entities.audit.AuditoriaLogEntity;
import com.ath.adminefectivo.utils.UtilsObjects;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaLogDTO {

    private Long idLogAdmin;
    private LocalDateTime fechaHora;
    private String ipOrigen;
    private String usuario;
    private String opcionMenu;
    private String accionHttp;
    private String codigoProceso;
    private String nombreProceso;
    private String estadoHttp;
    private String mensajeRespuesta;

    private String peticion;
    private String respuesta;
    private String valorAnterior;
    private String valorNuevo;
    private String tabla;
    private String idTabla;
    private String eventosInternos;

    public static final Function<AuditoriaLogEntity, AuditoriaLogDTO> CONVERTER_DTO =
            (AuditoriaLogEntity entity) -> {
                ObjectMapper mapper = new ObjectMapper();
                AuditoriaLogDTO dto = new AuditoriaLogDTO();
                UtilsObjects.copiarPropiedades(entity, dto);

                try {
                    if (entity.getPeticion() != null) {
                        dto.setPeticion(
                            mapper.readTree(entity.getPeticion()).toString()
                        );
                    }
                    if (entity.getRespuesta() != null) {
                        dto.setRespuesta(
                            mapper.readTree(entity.getRespuesta()).toString()
                        );
                    }
                    if (entity.getValorAnterior() != null) {
                        dto.setValorAnterior(
                            mapper.readTree(entity.getValorAnterior()).toString()
                        );
                    }
                    if (entity.getValorNuevo() != null) {
                        dto.setValorNuevo(
                            mapper.readTree(entity.getValorNuevo()).toString()
                        );
                    }
                } catch (Exception e) {
                    // fallback: lo envía tal cual si algo falla
                    dto.setPeticion(entity.getPeticion());
                    dto.setRespuesta(entity.getRespuesta());
                    dto.setValorAnterior(entity.getValorAnterior());
                    dto.setValorNuevo(entity.getValorNuevo());
                }
                return dto;
            };
}