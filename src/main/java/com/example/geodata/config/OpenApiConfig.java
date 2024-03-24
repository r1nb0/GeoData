package com.example.geodata.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "GeoData API",
                description = "convenient search for info about geo objects",
                version = "1.0",
                contact = @Contact(
                        name = "Ilya",
                        email = "ilia.mesha44@gmail.com"
                )
        )
)

public class OpenApiConfig {
}
