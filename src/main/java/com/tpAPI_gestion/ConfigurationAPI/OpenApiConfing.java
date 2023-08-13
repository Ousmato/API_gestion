package com.tpAPI_gestion.ConfigurationAPI;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class OpenApiConfing {
    @Bean
    public OpenAPI quizOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("tpApi_getion")
                        .description("API  Gestion de Budget personnel")
                        .version("1.0"));
    }
}
