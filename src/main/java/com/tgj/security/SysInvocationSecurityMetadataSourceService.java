package com.tgj.security;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.tgj.entity.SysPermission;
import com.tgj.service.SysPermissionService;

@Component
public class SysInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private SysPermissionService sysPermissionService;

	// 后期为了启动效率，应当放到缓存里面，并且自己来维护它
	private Map<String, Collection<ConfigAttribute>> map = null;

	public void loadResourceDefine() {
		map = new ConcurrentHashMap<>();
        Collection<ConfigAttribute> sets;
        ConfigAttribute ca;
        List<SysPermission> permissions = sysPermissionService.findAll();
        for(SysPermission permission : permissions) {
        	sets = map.containsKey(permission.getUrl()) ? map.get(permission.getUrl()) : new HashSet<>();
            ca = new SecurityConfig(permission.getName());
            //此处只添加了用户的名字，其实还可以添加更多权限的信息，例如请求方法到ConfigAttribute的集合中去。此处添加的信息将会作为SysAccessDecisionManager类的decide的第三个参数。
            sets.add(ca);
            //用权限的getUrl() 作为map的key，用ConfigAttribute的集合作为 value，
            map.put(permission.getUrl(), sets);
        }
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		if (Objects.isNull(map)) {
			loadResourceDefine();
		}
		// object 中包含用户请求的request 信息
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		System.out.println(request.getRequestURL());
		AntPathRequestMatcher matcher;
		String resUrl;
		for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
			resUrl = iter.next();
			matcher = new AntPathRequestMatcher(resUrl);
			if (matcher.matches(request)) {
				return map.get(resUrl);
			}
		}
		return Collections.emptyList();
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		if (Objects.isNull(map)) {
			loadResourceDefine();
		}
		Collection<ConfigAttribute> allConfigAttributes = new HashSet<>();
		for (Entry<String, Collection<ConfigAttribute>> en : map.entrySet()) {
			allConfigAttributes.addAll(en.getValue());
		}
		return allConfigAttributes;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
