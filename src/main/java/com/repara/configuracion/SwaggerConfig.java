package com.repara.configuracion;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String nombreEsquema = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("RePara API")
                        .version("1.0.0")
                        .description("API REST para la plataforma RePara - Servicios técnicos a domicilio"))
                .addSecurityItem(new SecurityRequirement().addList(nombreEsquema))
                .components(new Components()
                        .addSecuritySchemes(nombreEsquema,
                                new SecurityScheme()
                                        .name(nombreEsquema)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}