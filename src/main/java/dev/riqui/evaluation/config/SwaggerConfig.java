package dev.riqui.evaluation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Usuarios")
                        .version("1.0")
                        .description("API para la gestión de usuarios con autenticación JWT")
                        .contact(new Contact()
                                .name("Ricardo Quiroga")
                                .email("ricardo.s.quiroga@gmail.com")))
                .addServersItem(new Server().url("http://localhost:8080"));
    }
}
