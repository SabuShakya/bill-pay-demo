package com.billpayment.billpaydemo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host("localhost:9090")
                .groupName("Swagger Test")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.billpayment.billpaydemo.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfo(
                "Bill Pay Demo",
                "A demo project for khanepani api integration.",
                "V2",
                "https://www.f1soft.com//",
                new Contact(
                        "Sabu Shakya",
                        "https://www.f1soft.com/",
                        "sabu.shakya@f1soft.com"),
                "Licensed to F1soft",
                "https://www.f1soft.com//",
                java.util.Collections.emptyList());
    }
}
