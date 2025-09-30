package com.ath.adminefectivo.auditoria.config;

import com.ath.adminefectivo.auditoria.context.AuditData;
import com.ath.adminefectivo.auditoria.context.AuditoriaContext;
import com.ath.adminefectivo.auditoria.utils.AuditReadyEvent;
import com.ath.adminefectivo.dto.response.ApiResponseADE;
import com.ath.adminefectivo.dto.response.ApiResponseCode;
import com.ath.adminefectivo.dto.response.ResponseADE;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.context.ApplicationEventPublisher;

@Component
@ControllerAdvice
public class AuditoriaResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaResponseAdvice.class);

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public AuditoriaResponseAdvice(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Interceptar preferentemente respuestas JSON; se puede ajustar según necesidades
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        AuditData audit = AuditoriaContext.get();
        if (audit == null) return body; // nada que hacer

        // Si es ApiResponseADE, intentar inferir estado
        if (body instanceof ApiResponseADE<?>) {
            ApiResponseADE<?> api = (ApiResponseADE<?>) body;
            ResponseADE resp = api.getResponse();
            if (resp != null && ApiResponseCode.SUCCESS.getCode().equals(resp.getCode())) {
                audit.setEstadoOperacion("OK");
            } else {
                audit.setEstadoOperacion("ERROR");
            }
            audit.setMensajeRespuesta(resp != null ? resp.getDescription() : null);
        }

        try {
            String serialized = objectMapper.writeValueAsString(body);
            audit.setRespuesta(serialized);
        } catch (JsonProcessingException e) {
            logger.warn("No se pudo serializar body para auditoría: {}", e.getMessage());
        }

        // Publicar evento para persistir la auditoría después del commit
        try {
            publisher.publishEvent(new AuditReadyEvent(this, audit));
        } catch (Exception e) {
            logger.error("Error publicando AuditReadyEvent: {}", e.getMessage());
        }

        return body;
    }
}
