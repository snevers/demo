package com.tgj.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tgj.config.base.DataSourceType;

/**
 * 
 * <p>
 *		数据源注解.
 * </p>
 *	<pre>
 *			通过此注解标明使用那个数据源
 *	</pre>
 * @className TargetDataSource
 * @author tgj  
 * @date 2018年6月8日 下午2:04:51 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
	
	/**
	 * 
	 * <p>
	 *		默认为写数据源.
	 * </p>
	 * @title value
	 * @return DataSourceType
	 */
	DataSourceType value() default DataSourceType.writeDataSource;
}
