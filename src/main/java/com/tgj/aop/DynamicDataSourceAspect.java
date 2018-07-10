package com.tgj.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.tgj.annotion.TargetDataSource;
import com.tgj.config.base.DynamicDataSourceHolder;

/**
 * 
 * <p>
 *		动态切换数据源.
 * </p>
 *	<pre>
 *			根据注解切换数据源
 *	</pre>
 * @className DynamicDataSourceAspect
 * @author tgj  
 * @date 2018年6月8日 下午2:05:46 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Component
public class DynamicDataSourceAspect {
	
	@Around(value = "@annotation(com.tgj.annotion.TargetDataSource) && @annotation(targetDataSource)", argNames = "targetDataSource")
	public Object around(ProceedingJoinPoint proceedingJoinPoint, TargetDataSource targetDataSource) throws Throwable {
		DynamicDataSourceHolder.setDataSource(targetDataSource.value());
		Object result = null;
		try {
			result = proceedingJoinPoint.proceed();
		} finally {
			DynamicDataSourceHolder.clearDataSource();
		}
		return result;
	}
}
