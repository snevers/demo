package com.tgj;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 
 * <p>
 *		部署到tomcat.
 * </p>
 * <p>
 *		支持部署到tomcat
 * </p>
 * @className ServletInitializer
 * @author Server  
 * @date 2018年5月16日 下午4:31:27 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RykjApplication.class);
	}

}
