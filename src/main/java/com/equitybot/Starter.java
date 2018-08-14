package com.equitybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.TimeZone;

import static springfox.documentation.builders.PathSelectors.regex;

@SpringBootApplication
@EnableSwagger2
public class Starter {


    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("IST"));
        SpringApplication.run(Starter.class, args);
    }

    @Bean
    public Docket swaggerKiteApi10() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("kite-api-1.0")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.equitybot"))
                .paths(regex("/api/.*"))
                .build()
                .apiInfo(new ApiInfoBuilder().version("1.0").title("Kite API").description("Documentation Kite API v1.0").build());
    }

}
