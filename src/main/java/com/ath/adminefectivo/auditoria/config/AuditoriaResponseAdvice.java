package com.ath.adminefectivo.auditoria.config;

import com.ath.adminefectivo.auditoria.context.AuditData;
import com.ath.adminefectivo.auditoria.context.AuditoriaContext;
import com.ath.adminefectivo.auditoria.utils.AuditReadyEvent;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.context.ApplicationEventPublisher;

@ControllerAdvice
public class AuditoriaResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaResponseAdvice.class);

    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;

    public AuditoriaResponseAdvice(ApplicationEventPublisher publisher, ObjectMapper objectMapper) {
        this.publisher = publisher;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        AuditData audit = AuditoriaContext.get();
        if (audit == null) {
            return body;
        }

        // Determinar estado de la operación
        if (body instanceof ApiResponseADE<?>) {
            ApiResponseADE<?> api = (ApiResponseADE<?>) body;
            ResponseADE resp = api.getResponse();
            if (resp != null && ApiResponseCode.SUCCESS.getCode().equals(resp.getCode())) {
                audit.setEstadoOperacion(Constantes.EXITOSO);
            } else {
                audit.setEstadoOperacion(Constantes.ERROR_GENERAL);
            }
            audit.setMensajeRespuesta(resp != null ? resp.getDescription() : null);
        }

        // Serializar body de respuesta
        try {
            String serialized = objectMapper.writeValueAsString(body);
            audit.setRespuesta(serialized);
        } catch (JsonProcessingException e) {
            logger.warn("No se pudo serializar body para auditoría: {}", e.getMessage());
        }

        // Publicar snapshot del AuditData (deep copy)
        try {
            AuditData snapshot = deepCopyAudit(audit);
            publisher.publishEvent(new AuditReadyEvent(this, snapshot));
        } catch (Exception e) {
            logger.error("Error publicando AuditReadyEvent: {}", e.getMessage(), e);
        }

        return body;
    }

    private AuditData deepCopyAudit(AuditData audit) {
        try {
            String json = objectMapper.writeValueAsString(audit);
            return objectMapper.readValue(json, AuditData.class);
        } catch (Exception e) {
            logger.warn("deepCopyAudit: no se pudo clonar AuditData, devolviendo original. error={}", e.getMessage());
            return audit;
        }
    }
}