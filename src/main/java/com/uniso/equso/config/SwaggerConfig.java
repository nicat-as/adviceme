package com.uniso.equso.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {
    // Swagger url example: http://localhost:8081/swagger-ui/

    private static final String JWT = "JWT";
    private static final String AUTH = "Authorization";
    private static final String HEADER = "header";
    private static final String SCOPE = "global";
    private static final String DESC = "accessEverything";

    private final ApiInfoConfig apiInfoConfig;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getInfo())
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .paths(Predicate.not(PathSelectors.regex("/error.*")))
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey(JWT, AUTH, HEADER);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(SCOPE, DESC);
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(JWT, authorizationScopes));
    }

    private ApiInfo getInfo(){
        System.out.println(apiInfoConfig);
        return new ApiInfoBuilder()
                .title(apiInfoConfig.getTitle())
                .version(apiInfoConfig.getVersion())
                .description(apiInfoConfig.getDescription())
                .build();
    }



}
