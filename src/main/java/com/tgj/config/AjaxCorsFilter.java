package com.tgj.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * <p>
 *	通过过滤支持crsf.
 * </p>
 * <p>
 *	暂时支持所有网站的crsf(注意Security配置里面有无支持哦，目前不启用，通过nginx统一设置)
 * </p>
 * @className AjaxCorsFilter
 * @author Server  
 * @date 2018年5月21日 上午9:38:02 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class AjaxCorsFilter extends CorsFilter {
	public AjaxCorsFilter() {
		super(configurationSource());
	}

	private static UrlBasedCorsConfigurationSource configurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		List<String> allowedHeaders = Arrays.asList("x-auth-token", "content-type", "X-Requested-With",
				"XMLHttpRequest");
		List<String> exposedHeaders = Arrays.asList("x-auth-token", "content-type", "X-Requested-With",
				"XMLHttpRequest");
		List<String> allowedMethods = Arrays.asList("POST", "GET", "DELETE", "PUT", "OPTIONS");
		List<String> allowedOrigins = Arrays.asList("*");
		corsConfig.setAllowedHeaders(allowedHeaders);
		corsConfig.setAllowedMethods(allowedMethods);
		corsConfig.setAllowedOrigins(allowedOrigins);
		corsConfig.setExposedHeaders(exposedHeaders);
		corsConfig.setMaxAge(36000L);
		corsConfig.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}
}
