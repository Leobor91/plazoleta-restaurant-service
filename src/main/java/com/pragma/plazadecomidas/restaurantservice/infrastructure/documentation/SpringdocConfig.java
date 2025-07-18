package com.pragma.plazadecomidas.restaurantservice.infrastructure.documentation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Servicio de Autenticación (AuthService)",
                version = "1.0.0",
                description = "Documentación de la API para la gestión de usuarios, roles y autenticación en el sistema de Plazoleta de Comidas.",
                contact = @Contact(
                        name = "Leonel Borja Vargas",
                        email = "sambor9119@gmail.com",
                        url = "https://github.com/Leobor91?tab=repositories"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8082", description = "Servidor Local de Desarrollo")
        }
)
public class SpringdocConfig {
}
