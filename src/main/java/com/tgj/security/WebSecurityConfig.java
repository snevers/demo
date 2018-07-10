package com.tgj.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.tgj.model.BasicResultModel;
import com.tgj.service.SysUserService;

/**
 * 
 * <p>
 * 配置HTTPBASIC权限验证.
 * </p>
 * 
 * @className WebSecurityConfig
 * @author tgj
 * @date 2018年2月11日 下午5:48:19
 * 
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysFilterSecurityInterceptor sysFilterSecurityInterceptor;
	
	@Autowired
	private SysAuthenticationSuccessHandler sysAuthenticationSuccessHandler;
	
	@Autowired
	private SysAuthenticationFailHander sysAuthenticationFailHander;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(sysUserService).passwordEncoder(new BCryptPasswordEncoder(12));
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// 忽略css.jq.img等文件
		// web.ignoring().antMatchers("/**.html", "/**.css", "/images/**", "/**.js");
	}

	@Value("${spring.profiles.active}")
	private String profile;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception { //csrf 有先后顺序，如果是关闭，得在最后配置才行
		List<String> auths = Lists.newArrayList("/admin/**");
		if ("prop".equals(profile)) {
			auths.add("/swagger-ui.html");
			auths.add("/monitoring/**");
		}
		http.addFilterBefore(sysFilterSecurityInterceptor, FilterSecurityInterceptor.class)// .csrf().ignoringAntMatchers("/admin/api/**", "/client/api/**",
			// "/admin/loginProcess", "/admin/loginProcess").and()
				.authorizeRequests()
				.antMatchers("/webjars/**", "/swagger-resources/**", "/v2/api-docs", "/client/api/**").permitAll().and()
				.authorizeRequests().antMatchers(auths.toArray(new String[auths.size()])).authenticated().and().formLogin()
				.loginPage("/admin/login.html").loginProcessingUrl("/admin/loginProcess")
				.successHandler(sysAuthenticationSuccessHandler).failureHandler(sysAuthenticationFailHander).permitAll().and().logout().logoutUrl("/admin/logoutProcess")
				.logoutSuccessHandler((request, response, authentication) -> {
					comsumer(response, "logout");
				}).deleteCookies("JSESSIONID").invalidateHttpSession(true).clearAuthentication(true).permitAll().and()
				.headers().frameOptions().disable().and()
				.csrf().disable().httpBasic();
	}

	protected void comsumer(HttpServletResponse response, String type) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		BasicResultModel basicResultModel = new BasicResultModel();
		if ("success".equals(type)) {
			basicResultModel.setMessage("登录成功");
		} else if ("failure".equals(type)) {
			basicResultModel.setStatus("2001");
			basicResultModel.setMessage("账号或密码错误");
		} else if ("logout".equals(type)) {
			basicResultModel.setMessage("退出成功");
		}
		response.getWriter().print(JSON.toJSONString(basicResultModel));
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(new BCryptPasswordEncoder(12).encode("123456"));
	}
}
