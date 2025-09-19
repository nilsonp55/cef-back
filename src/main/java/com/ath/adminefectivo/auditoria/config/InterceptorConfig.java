package com.ath.adminefectivo.auditoria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private AuditoriaInterceptor auditoriaInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	System.out.println("Registrando AuditoriaInterceptor en InterceptorConfig");
        registry.addInterceptor(auditoriaInterceptor)
                .addPathPatterns("/v1.0.1/ade/tarifas-especiales-cliente/**");
        //.addPathPatterns("/**"); // Esto intercepta todas las rutas del proyecto
    }
}