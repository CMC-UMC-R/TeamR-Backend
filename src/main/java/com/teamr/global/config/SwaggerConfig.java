package com.teamr.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("로컬 개발 서버"),
                        new Server().url("https://api.teamr.com").description("프로덕션 서버")
                ))
                .components(new Components());
    }

    private Info apiInfo() {
        return new Info()
                .title("TeamR API Documentation")
                .description("TeamR 프로젝트의 REST API 문서입니다.")
                .version("v1.0.0")
                .contact(new Contact()
                        .name("TeamR Development Team"));
    }
}

