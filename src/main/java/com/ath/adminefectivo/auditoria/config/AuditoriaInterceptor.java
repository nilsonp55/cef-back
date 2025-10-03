package com.ath.adminefectivo.auditoria.config;

import com.ath.adminefectivo.auditoria.context.AuditData;
import com.ath.adminefectivo.auditoria.context.AuditoriaContext;
import com.ath.adminefectivo.constantes.Constantes;
import com.ath.adminefectivo.constantes.Dominios;
import com.ath.adminefectivo.repositories.MenuRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuditoriaInterceptor implements HandlerInterceptor {
	
    private static final Logger logger = LoggerFactory.getLogger(AuditoriaInterceptor.class);

    private final MenuRepository menuRepository;

    @Autowired
    public AuditoriaInterceptor(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Asumimos que RequestResponseCachingFilter ya creó el AuditData y lo puso en el contexto
        AuditData audit = AuditoriaContext.get();
        if (audit == null) {
            logger.warn("AuditoriaContext no inicializado para la petición {}", request.getRequestURI());
            return true; // permitir continuar
        }

        // Obtiene el nombre del procesos a partir del nombre del menu.
        obtenerNombreProceso(request, audit);

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

    private void obtenerNombreProceso(HttpServletRequest request, AuditData audit) {
    	String nombreMenu = audit.getOpcionMenu();

        // 1️ Si aún no hay nombre en audit, intentar con header
        if (nombreMenu == null || nombreMenu.isBlank()) {
            nombreMenu = request.getHeader("X-Menu-Id");
        }

        // 2️ Si tampoco hay header, asignar valores por defecto y salir
        if (nombreMenu == null || nombreMenu.isBlank()) {
            audit.setNombreProceso(Constantes.SIN_PROCESO);
            audit.setIsProcess(false);
            return;
        }

        // 3️ Consultar en base de datos
        menuRepository.findByNombre(nombreMenu).ifPresentOrElse(menu -> {
            audit.setNombreProceso(menu.getCodigoProceso());
            audit.setIsProcess(menu.getEsProceso());
            audit.setIdMenu(menu.getIdMenu());
        }, () -> {
            // Si no se encuentra en BD, también valores por defecto
            audit.setNombreProceso(Constantes.DESCONOCIDO);
            audit.setIsProcess(false);
        });
    }
}