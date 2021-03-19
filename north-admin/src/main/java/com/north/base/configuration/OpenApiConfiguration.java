package com.north.base.configuration;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-11-23
 */
@OpenAPIDefinition(
        info = @Info(
                title = "north", version = "1.0",
                description = "swagger-ui3.0描述信息",
                contact = @Contact(name="North",email = "zhangszi0291@gmail.com"),
                license = @License(name = "",url = "")
        ),
        externalDocs = @ExternalDocumentation(description = "参考文档",
                url = "https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations"
        )
)
@Configuration
public class OpenApiConfiguration {


}
