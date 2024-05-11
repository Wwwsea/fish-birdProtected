package com.fish.birdProducted.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API文档")
                        .version("1.0")
                        .description("基于SpringBoot3 + Vue3开发的鸟类信息分享平台")
                        .contact(new Contact().name("Shrike").url("http://127.0.0.1:6060/").email("2735106716@qq.com"))
                );
    }
}