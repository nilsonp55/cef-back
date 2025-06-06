package com.ath.adminefectivo.cofig;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * Clase encargada de configurar los messages.properties y leerlos 
 * @author Bayron Andres Perez
 */
@Configuration
public class MessageConfig {


    /**
     * Carga los properties
     * @return MessageSource
     * @author BayronPerez
     */
    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        return messageSource;
    }

    /**
     * Establece roperties por defecto
     * @return LocaleResolver
     * @author BayronPerez
     */
    @Bean
    LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ROOT);
        return slr;
    }

    /**
     * Para resolver las variables en messages y poder leerlas "variable.variable"
     * @return LocalValidatorFactoryBean
     * @author BayronPerez
     */
    @Bean
    LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

}
