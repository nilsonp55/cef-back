package com.ath.adminefectivo.auditoria.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.ath.adminefectivo.auditoria.filter.RequestResponseCachingFilter;

@Configuration
public class AuditoriaFilterConfig {

    @Bean
    public FilterRegistrationBean<RequestResponseCachingFilter> auditingFilter(RequestResponseCachingFilter filter) {
        FilterRegistrationBean<RequestResponseCachingFilter> reg = new FilterRegistrationBean<>(filter);
        reg.setOrder(Ordered.HIGHEST_PRECEDENCE); // asegurar que vaya antes
        reg.addUrlPatterns("/*");
        return reg;
    }
}