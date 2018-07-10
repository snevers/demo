package com.tgj.api.wechat.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

@RestController
@RequestMapping(value = "/wechat/api/")
public class DragonBoatController {
	
	private static final Map<String, WxMpOAuth2AccessToken> USERS = new HashMap<>();

	@Autowired
	private WxMpService wxMpService;
	
	@Value("${wechat.mp.auth2Success}")
	private String url;
	
	@ApiOperation(value = "网页授权地址")
	@RequestMapping(value = "auth2Url", method = RequestMethod.GET)
	public String getAuth2Url(HttpServletRequest request) {
		return wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
	}
	
	@ApiOperation(value = "返回用户信息")
	@RequestMapping(value = "auth2Success", method = RequestMethod.GET)
	public WxMpUser getWxMpUser(String code, String openId, String state) throws WxErrorException {
		WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
		if (StringUtils.isBlank(code)) {
			wxMpOAuth2AccessToken = USERS.get(openId);
		} else {
			wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
			USERS.put(wxMpOAuth2AccessToken.getOpenId(), wxMpOAuth2AccessToken);
		}
		if (!wxMpService.oauth2validateAccessToken(wxMpOAuth2AccessToken)) {
			wxMpOAuth2AccessToken = wxMpService.oauth2refreshAccessToken(wxMpOAuth2AccessToken.getRefreshToken());
		}
		WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
		return wxMpUser;
	}
	
	@ApiOperation(value = "JsSdk需要的相关信息")
	@RequestMapping(value = "jsSdkConfig", method = RequestMethod.GET)
	public WxJsapiSignature getWxJsapiSignature(String url, HttpServletRequest request) throws WxErrorException {
		WxJsapiSignature wxJsapiSignature = wxMpService.createJsapiSignature(StringUtils.isBlank(url) ? request.getRequestURL().toString() : url);
		return wxJsapiSignature;
	}
	
}
