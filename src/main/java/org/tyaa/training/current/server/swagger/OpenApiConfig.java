package org.tyaa.training.current.server.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition
@SecurityScheme(
        name = "jsessionid",
        in = SecuritySchemeIn.COOKIE,
        type = SecuritySchemeType.APIKEY,
        paramName = "JSESSIONID"
)
public class OpenApiConfig {}
