package com.ath.adminefectivo.auditoria.filter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.ath.adminefectivo.auditoria.context.AuditData;
import com.ath.adminefectivo.auditoria.context.AuditoriaContext;
import com.ath.adminefectivo.auditoria.utils.AuditReadyEvent;
import com.ath.adminefectivo.constantes.Constantes;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class RequestResponseCachingFilter extends OncePerRequestFilter {

    @Autowired
    private ApplicationEventPublisher publisher;

    private static final int MAX_BODY_LOG_LENGTH = 32_768;
    

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
    	
    	// Solo auditar si es POST, PUT o DELETE
        String method = request.getMethod();
        if (!(Constantes.POST.equalsIgnoreCase(method) || 
        	  Constantes.PUT.equalsIgnoreCase(method) || 
        	  Constantes.DELETE.equalsIgnoreCase(method))) {
            filterChain.doFilter(request, response);
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        String codigoProceso = UUID.randomUUID().toString();
        String usuario = getUserRequest(request);
        String opcionMenu = getMenuIdHeader(request);

        AuditData audit = AuditData.builder()
                .codigoProceso(codigoProceso)
                .fechaHora(LocalDateTime.now())
                .ipOrigen(extractIp(wrappedRequest))
                .metodo(wrappedRequest.getMethod())
                .uri(wrappedRequest.getRequestURI())
                .headers(extractHeaders(wrappedRequest))
                .usuario(usuario)
                .opcionMenu(opcionMenu)
                .build();

        AuditoriaContext.set(audit);
        MDC.put("codigoProceso", codigoProceso);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            try {
                byte[] reqBuf = wrappedRequest.getContentAsByteArray();
                if (reqBuf.length > 0) {
                    String reqBody = new String(reqBuf, Charset.forName(wrappedRequest.getCharacterEncoding() != null
                            ? wrappedRequest.getCharacterEncoding() : "UTF-8"));
                    audit.setPeticion(truncate(reqBody));
                }

                byte[] respBuf = wrappedResponse.getContentAsByteArray();
                if (respBuf.length > 0) {
                    String respBody = new String(respBuf, Charset.forName(wrappedResponse.getCharacterEncoding() != null
                            ? wrappedResponse.getCharacterEncoding() : "UTF-8"));
                    audit.setRespuesta(truncate(respBody));
                }

                audit.setEstadoHttp(wrappedResponse.getStatus());

                // Publicar el evento con el AuditData COMPLETO
                publisher.publishEvent(new AuditReadyEvent(this, audit));

                wrappedResponse.copyBodyToResponse();

            } finally {
                MDC.remove("codigoProceso");
                AuditoriaContext.clear();
            }
        }
    }

    private String getMenuIdHeader(HttpServletRequest request) {
        String menuId = request.getHeader("X-Menu-Id");
        return (menuId != null && !menuId.isBlank()) ? menuId : "N/A";
    }
    
    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names != null && names.hasMoreElements()) {
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
        return s.length() <= MAX_BODY_LOG_LENGTH ? s : s.substring(0, MAX_BODY_LOG_LENGTH) + "...[TRUNCATED]";
    }
    
    private String getUserRequest(HttpServletRequest request) {
    	String nameUser = "System";
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            return nameUser;
        }
        try {
            String[] partsToken = headerAuth.replace("Bearer ", "").split("\\.");
            if (partsToken.length < 2) {
                return nameUser;
            }
            String tokenAuth = partsToken[0] + "." + partsToken[1] + ".";
            DecodedJWT jwt = JWT.decode(tokenAuth);
            return jwt.getClaims().get("name").asString();
        } catch (Exception e) {
            return nameUser;
        }
    }
}