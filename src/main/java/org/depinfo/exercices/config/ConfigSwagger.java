package org.depinfo.exercices.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

// Permet d'exposer automatiquement tous les points d'API avec Swagger
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Exos API",
        description = "API pour les exercices des cours de mobile",
        version = "1.0"
    )
)
public class ConfigSwagger {
}