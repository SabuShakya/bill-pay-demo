package com.billpayment.billpaydemo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
//                .host("localhost:9090")
//                .groupName("Swagger Test")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.billpayment.billpaydemo.controller"))
                .paths(PathSelectors.any())
                .build()
//                .securitySchemes(Lists.newArrayList(apiKey()))
//                .securityContexts(Lists.newArrayList(securityContext()))
                .apiInfo(metaData())
                .globalOperationParameters(
                        Collections.singletonList(new ParameterBuilder()
                                .name("Authorization")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(true)
                                .build())
                );
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

//    @Bean
//    SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(defaultAuth())
//                .forPaths(PathSelectors.any())
//                .build();
//    }
//
//    List<SecurityReference> defaultAuth() {
//        AuthorizationScope authorizationScope
//                = new AuthorizationScope("global", "accessEverything");
//        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
//        authorizationScopes[0] = authorizationScope;
//        return Lists.newArrayList(
//                new SecurityReference("JWT", authorizationScopes));
//    }
//
//    private ApiKey apiKey() {
//        return new ApiKey("JWT", "Authorization", "header");
//    }
}


/*
 * https://stackoverflow.com/a/53862554/11709663
 *
 * */