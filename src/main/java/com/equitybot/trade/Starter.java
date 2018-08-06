package com.equitybot.trade;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Starter {
		
	
	public static void main(String[] args){
		TimeZone.setDefault(TimeZone.getTimeZone("IST"));
		SpringApplication.run(Starter.class, args);
		
	}
	
	@Bean
	public Docket swaggerGeneralPropertyApi10() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("general-property-api-1.0")
				.select()
					.apis(RequestHandlerSelectors.basePackage("com.equitybot.trade.general.controller"))
					.paths(regex("/api/general/v1.0.*"))
				.build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("General Property API").description("Documentation General Property API v1.0").build());
	}
	
	@Bean
	public Docket swaggerPropertyApi10() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("property-api-1.0")
				.select()
					.apis(RequestHandlerSelectors.basePackage("com.equitybot.trade.ws.controller.property"))
					.paths(regex("/api/property/v1.0.*"))
				.build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("Property API").description("Documentation Property API v1.0").build());
	}
	@Bean
	public Docket swaggerInstrumentApi10() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("instrument-api-1.0")
				.select()
					.apis(RequestHandlerSelectors.basePackage("com.equitybot.trade.ws.controller.instrument"))
					.paths(regex("/api/instrument/v1.0.*"))
				.build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("Instrument API"
						+ "").description("Documentation Instrument Property API v1.0").build());
	}
	
	@Bean
	public Docket swaggerInitiateApi10() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("initiate-process-api-1.0")
				.select()
					.apis(RequestHandlerSelectors.basePackage("com.equitybot.trade.ws.controller.main"))
					.paths(regex("/api/process/v1.0.*"))
				.build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("Initiate Process API").description("Documentation  Initiate Process API v1.0").build());
	}
	
	@Bean
	public Docket swaggerTradeOrderApi10() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("trade-order-api-1.0")
				.select()
					.apis(RequestHandlerSelectors.basePackage("com.equitybot.trade.ws.controller.order"))
					.paths(regex("/api/tradeorder/v1.0.*"))
				.build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("Trade Order API").description("Documentation  Trade Order API v1.0").build());
	}
	
}
