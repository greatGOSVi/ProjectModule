package com.projectmodule.configuration;

import java.util.Collections;

import org.springdoc.webmvc.ui.SwaggerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Import(SwaggerConfig.class)
@EnableSwagger2
public class SwaggerConfiguration {
	
	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.projectmodule.controller"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(getApiInfo());
	}
	
	private ApiInfo getApiInfo() {
		return new ApiInfo(
				"Project Module",
				"An API module to manage company projects, like their names, description, personnel involved, and cost.",
				"1.0",
				"https://theksquaregroup.com",
				new Contact("The Ksquare Group", "https://theksquaregroup.com", "info@theksquaregroup.com"),
				"LICENSE",
				"LICENSE URL",		
				Collections.emptyList());
	}

}
