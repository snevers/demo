package com.tgj.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class SysAuthenticationFailHander extends ExceptionMappingAuthenticationFailureHandler {
	private static final Logger LOG = LoggerFactory.getLogger(SysAuthenticationFailHander.class);

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		LOG.error("登录失败", exception);
		
		super.setDefaultFailureUrl("/failure.html");
		super.onAuthenticationFailure(request, response, exception);
	}
}