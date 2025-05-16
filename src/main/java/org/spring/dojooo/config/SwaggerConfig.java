package org.spring.dojooo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        Info info = new Info()
                .version("v1.0")
                .title("Dojooo API")
                .description("Dojooo API 입니다");
        return new OpenAPI()
                .info(info);
    }
}
