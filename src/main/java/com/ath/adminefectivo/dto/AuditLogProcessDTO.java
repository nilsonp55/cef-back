package com.ath.adminefectivo.dto;

import java.time.LocalDateTime;
import java.util.function.Function;

import com.ath.adminefectivo.entities.audit.AuditLogProcessEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogProcessDTO {

    private Long idLogProcessProc;
    private LocalDateTime fechaHoraProc;
    private String ipOrigenProc;
    private String usuarioProc;
    private String opcionMenuProc;
    private String accionHttpProc;
    private String codigoProcesoProc;
    private String nombreProcesoProc;
    private String estadoHttpProc;
    private String estadoOperacionProc;
    private String mensajeRespuestaProc;

    private String peticionProc;
    private String respuestaProc;
    private String valorAnteriorProc;
    private String valorNuevoProc;
    private String tablaProc;
    private String idTablaProc;
    private String eventosInternosProc;

    public static final Function<AuditLogProcessEntity, AuditLogProcessDTO> CONVERTER_DTO =
            (AuditLogProcessEntity entity) -> {
                ObjectMapper mapper = new ObjectMapper();
                AuditLogProcessDTO dto = new AuditLogProcessDTO();

                dto.setIdLogProcessProc(entity.getIdLogProcessProc());
                dto.setFechaHoraProc(entity.getFechaHoraProc());
                dto.setIpOrigenProc(entity.getIpOrigenProc());
                dto.setUsuarioProc(entity.getUsuarioProc());
                dto.setOpcionMenuProc(entity.getOpcionMenuProc());
                dto.setAccionHttpProc(entity.getAccionHttpProc());
                dto.setCodigoProcesoProc(entity.getCodigoProcesoProc() != null ? entity.getCodigoProcesoProc().toString() : null);
                dto.setNombreProcesoProc(entity.getNombreProcesoProc());
                dto.setEstadoHttpProc(entity.getEstadoHttpProc());
                dto.setEstadoOperacionProc(entity.getEstadoOperacionProc());
                dto.setMensajeRespuestaProc(entity.getMensajeRespuestaProc());
                
                dto.setTablaProc(entity.getTablaProc());
                dto.setIdTablaProc(entity.getIdTablaProc());
                dto.setEventosInternosProc(entity.getEventosInternosProc());

                try {
                    if (entity.getPeticionProc() != null) {
                        dto.setPeticionProc(mapper.readTree(entity.getPeticionProc()).toString());
                    }
                    if (entity.getRespuestaProc() != null) {
                        dto.setRespuestaProc(mapper.readTree(entity.getRespuestaProc()).toString());
                    }
                    if (entity.getValorAnteriorProc() != null) {
                        dto.setValorAnteriorProc(mapper.readTree(entity.getValorAnteriorProc()).toString());
                    }
                    if (entity.getValorNuevoProc() != null) {
                        dto.setValorNuevoProc(mapper.readTree(entity.getValorNuevoProc()).toString());
                    }
                } catch (Exception e) {
                    // fallback: devolver el texto tal cual
                    dto.setPeticionProc(entity.getPeticionProc());
                    dto.setRespuestaProc(entity.getRespuestaProc());
                    dto.setValorAnteriorProc(entity.getValorAnteriorProc());
                    dto.setValorNuevoProc(entity.getValorNuevoProc());
                }

                return dto;
            };
}