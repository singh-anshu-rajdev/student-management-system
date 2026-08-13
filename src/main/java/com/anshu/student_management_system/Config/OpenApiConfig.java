package com.anshu.student_management_system.Config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.anshu.student_management_system.Utilities.IStaticConstants.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title(OPENAPI_TITLE)
                        .description(
                                OPENAPI_DESCRIPTION
                        )
                        .version(OPENAPI_VERSION)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        OPENAPI_SECURITY_SCHEME,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme(OPENAPI_SCHEME)
                                                .bearerFormat(OPENAPI_BEARER_FORMAT)
                                )
                );
    }
}
