package com.ath.adminefectivo.auditoria.config;

import com.ath.adminefectivo.auditoria.context.AuditData;
import com.ath.adminefectivo.auditoria.context.AuditoriaContext;
import com.ath.adminefectivo.constantes.Dominios;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuditoriaInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Asumimos que RequestResponseCachingFilter ya creó el AuditData y lo puso en el contexto
        AuditData audit = AuditoriaContext.get();
        if (audit == null) {
            logger.warn("AuditoriaContext no inicializado para la petición {}", request.getRequestURI());
            return true; // permitir continuar
        }

        // Determinar opcion menu / codigo de proceso según path (si no viene ya)
        String path = request.getRequestURI();
        String codigoProceso = obtenerCodigoProcesoDesdePath(path);
        audit.setCodigoProceso(codigoProceso);

        // Usuario: si ya no está, intentar extraer de encabezado o SecurityContext (no incluido aquí)
        if (audit.getUsuario() == null) {
            String userHeader = request.getHeader("X-User");
            audit.setUsuario(userHeader);
        }

        logger.debug("Iniciando auditoría - codigoProceso={} uri={}", audit.getCodigoProceso(), audit.getUri());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Limpiar para evitar fugas de memoria
        AuditoriaContext.clear();
    }

    private String obtenerCodigoProcesoDesdePath(String path) {
        if (path == null) return Dominios.CODIGO_PROCESO_LOG_LIQUIDACION;
        if (path.contains("/v1.0.1/ade/conciliacion-transporte/consultar-conciliadas")) {
            return Dominios.CODIGO_PROCESO_LOG_CERTIFICACION;
        }
        if (path.contains("/archivos-liquidacion/eliminar")) {
            return Dominios.CODIGO_PROCESO_LOG_CONCILIACION;
        }
        return Dominios.CODIGO_PROCESO_LOG_LIQUIDACION;
    }
}