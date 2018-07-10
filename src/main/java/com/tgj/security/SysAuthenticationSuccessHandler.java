package com.tgj.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgj.model.BasicResultModel;

@Component
public class SysAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private static final Logger LOG = LoggerFactory.getLogger(SysAuthenticationSuccessHandler.class);
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		LOG.warn("认证成功:" + objectMapper.writeValueAsString(authentication));
		response.setContentType("application/json;charset=UTF-8");
		BasicResultModel basicResultModel = new BasicResultModel();
		basicResultModel.setMessage("登录成功");
		response.getWriter().print(JSON.toJSONString(basicResultModel));
//		super.setDefaultTargetUrl("/home.html");
//		super.onAuthenticationSuccess(request, response, authentication);
	}
}