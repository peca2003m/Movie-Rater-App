package com.example.movie_rater.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI customSwagger(){
        return new OpenAPI()
                .info(new Info()
                        .title("Movie Rater App")
                        .version("1.0")
                        .description("App for rating movies"))

                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))

                .components(new Components().addSecuritySchemes("basicAuth", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")));
    }

}
