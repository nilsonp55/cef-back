package com.ath.adminefectivo.swagger;


import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.ath.adminefectivo.constantes.SwaggerConstants;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;



/**
 * Clase de configuración de swagger
 *
 * @author CamiloBenavides
 */
@Configuration
public class SwaggerConfiguration {
	
	 public static final String TAG_1 = "tag1";

    /**
     * Configuracion de la documentación swagger a nivel de aplicativo y su propietario
     * 
     * @return OpenAPI
     * @author CamiloBenavides
     */
    @Bean
    OpenAPI customOpenAPI() {
        final Info info = new Info()
                .title(SwaggerConstants.TITLE)
                .description(SwaggerConstants.DESCRIPTION)
                .version(SwaggerConstants.CODE_VERSION)
                .contact(new Contact().email(SwaggerConstants.MAIL_CONTACT).name(SwaggerConstants.NAME_CONTACT));

        return new OpenAPI().components(new Components())
                .addTagsItem(createTag(TAG_1, "Tag principal."))
                .info(info);
    }

	    private Tag createTag(String name, String description) {
	        final Tag tag = new Tag();
	        tag.setName(name);
	        tag.setDescription(description);
	        return tag;
	    }


    /**
     * Method used by swagger to set API's general configuration
     * 
     * @return Docket
     * @author cpalacios
     */

    /**
     * Clase de configuracion, en donde se definen los paquetes a documetnar y las respuestas 
     * genéricas
     * 
     * @return Docket
     * @author CamiloBenavides
     */
    @Bean
    Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(SwaggerConstants.PACKAGE))
                .paths(PathSelectors.any()).build()
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET,  globalResponses)
                .globalResponses(HttpMethod.POST,  globalResponses);
    }
	


	/**
	 * Parametrización de las listas de respuestas globales y transverales del apicativo
	 */
	final List<Response> globalResponses = Arrays.asList(    
            new ResponseBuilder().code("403")
                                        .description(SwaggerConstants.RESPONSE_MESSAGE_FORBIDDEN)
                                        .build(),
            new ResponseBuilder().code("500")
                                        .description(SwaggerConstants.RESPONSE_MESSAGE_INTERNAL_SERVER_ERROR)
                                        .build()
    );
}
