package com.tgj.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * <p>
 *		使mybatis和jpa统一.
 * </p>
 *	<pre>
 *			支持@PrePersist和@PreUpdate注解
 *	</pre>
 * @className MapperUpdateOrPersistAspect
 * @author tgj  
 * @date 2018年6月8日 下午2:06:35 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
@Aspect
@Component
public class MapperUpdateOrPersistAspect {

	private static final Logger LOG = LoggerFactory.getLogger(MapperUpdateOrPersistAspect.class);

	@Before(value = "execution(public * com.tgj.base.BaseService.insert*(*)) || execution(public * com.tgj.base.BaseService.update*(..)) || execution(public * com.tgj.base.BaseMybatisService.insert*(*)) || execution(public * com.tgj.base.BaseMybatisService.update*(..))")
	public void saveOrUpdateBefore(JoinPoint joinPoint) throws Exception {
		String methodName = joinPoint.getSignature().getName();
		boolean update = false;
		if (methodName.startsWith("update")) {
			update = true;
		}
		Object obj = joinPoint.getArgs()[0];
		if (obj instanceof List<?>) {
			for (Object object : (List<?>) obj) {
				Method[] methods = object.getClass().getDeclaredMethods();
				process(update, methods, object);
			}
		} else {
			Method[] methods = obj.getClass().getDeclaredMethods();
			process(update, methods, obj);
		}
	}

	private void process(boolean update, Method[] methods, Object obj) throws Exception {
		try {
			for (Method method : methods) {
				Object flag = update ? method.getDeclaredAnnotation(PreUpdate.class)
						: method.getDeclaredAnnotation(PrePersist.class);
				if (null != flag) {
					method.invoke(obj);
					break;
				}
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOG.error("update or insert failure", e);
			throw new Exception(e);
		}
	}
}
