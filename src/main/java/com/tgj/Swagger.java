package com.tgj;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * <p>
 *		接口配置.
 * </p>
 *	<pre>
 *		只在test和dev环境启用	
 *	</pre>
 * @className Swagger
 * @author tgj  
 * @date 2018年6月8日 下午2:04:18 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
@Configuration  
@EnableSwagger2  
//@Profile(value = {"dev", "test"})  
public class Swagger {

	/**
	 * 
	 * <p>
	 *		swagger2配置.
	 * </p>
	 * @title createRestApi
	 * @return Docket
	 */
	@Bean
	public Docket createRestApi() {
		List<SecurityScheme> securitySchemes = new ArrayList<>();
		securitySchemes.add(new BasicAuth("basicAuth"));
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).securitySchemes(securitySchemes).select()
				.apis(RequestHandlerSelectors.basePackage("com.rykj.api")).paths(PathSelectors.any())
				.build();
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Spring Boot中使用Swagger2构建RESTful APIs").description("目前仅用来测试")
				.contact(new Contact("RYKJ", "https://www.rongyaotech.com/", "tonggangjin2@163.com")).version("1.0").build();
	}
}
