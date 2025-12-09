package com.ath.adminefectivo.auditoria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuditoriaInterceptor auditoriaInterceptor;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
    	log.info("Registrando AuditoriaInterceptor en InterceptorConfig");
        registry.addInterceptor(auditoriaInterceptor)
        .addPathPatterns("/**"); // Esto intercepta todas las rutas del proyecto
    }
}