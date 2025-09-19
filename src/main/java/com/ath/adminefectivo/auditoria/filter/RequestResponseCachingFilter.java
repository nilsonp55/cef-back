package com.ath.adminefectivo.auditoria.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.ath.adminefectivo.auditoria.context.AuditData;
import com.ath.adminefectivo.auditoria.context.AuditoriaContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.UUID;

@Component
public class RequestResponseCachingFilter extends OncePerRequestFilter {

    private static final int MAX_BODY_LOG_LENGTH = 32_768; // configurable

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Si es un GET, no interceptamos: dejamos pasar directamente
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // Para otros métodos (POST, PUT, DELETE, PATCH, etc.) sí interceptamos
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        String codigoProceso = UUID.randomUUID().toString();

        AuditData audit = AuditData.builder()
                .codigoProceso(codigoProceso)
                .fechaHora(LocalDateTime.now())
                .ipOrigen(extractIp(wrappedRequest))
                .metodo(wrappedRequest.getMethod())
                .uri(wrappedRequest.getRequestURI())
                .headers(extractHeaders(wrappedRequest))
                .build();

        AuditoriaContext.set(audit);
        MDC.put("codigoProceso", codigoProceso);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            try {
                // Leer request body desde el wrapper
                byte[] reqBuf = wrappedRequest.getContentAsByteArray();
                if (reqBuf != null && reqBuf.length > 0) {
                    String reqBody = new String(reqBuf, 
                            Charset.forName(wrappedRequest.getCharacterEncoding() != null 
                                    ? wrappedRequest.getCharacterEncoding() : "UTF-8"));
                    audit.setPeticion(truncate(reqBody));
                }

                // Leer response body desde el wrapper
                byte[] respBuf = wrappedResponse.getContentAsByteArray();
                if (respBuf != null && respBuf.length > 0) {
                    String respBody = new String(respBuf,
                            Charset.forName(wrappedResponse.getCharacterEncoding() != null
                                    ? wrappedResponse.getCharacterEncoding() : "UTF-8"));
                    audit.setRespuesta(truncate(respBody));
                }

                audit.setEstadoHttp(wrappedResponse.getStatus());

                // Copiar la respuesta al cliente
                wrappedResponse.copyBodyToResponse();

            } finally {
                MDC.remove("codigoProceso");
                // AuditoriaContext se limpia después en el interceptor
            }
        }
    }

    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        if (names == null) return map;
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            map.put(name, request.getHeader(name));
        }
        return map;
    }

    private String extractIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private String truncate(String s) {
        if (s == null) return null;
        if (s.length() <= MAX_BODY_LOG_LENGTH) return s;
        return s.substring(0, MAX_BODY_LOG_LENGTH) + "...[TRUNCATED]";
    }
}
