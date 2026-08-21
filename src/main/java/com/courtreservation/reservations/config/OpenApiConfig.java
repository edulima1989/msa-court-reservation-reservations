package com.courtreservation.reservations.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  OpenAPI reservationsOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Reservation API")
            .description("API para creación, consulta y cancelación de reservas")
            .version("v1"));
  }
}
