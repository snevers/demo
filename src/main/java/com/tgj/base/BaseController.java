package com.tgj.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.tgj.model.BasicResultModel;

/**
 * 
 * <p>
 *		Controller基类，建议所有controller都继承此类.
 * </p>
 *	<pre>
 *			以后可以在此类添加一些公共方法
 *	</pre>
 * @className BaseController
 * @author tgj  
 * @date 2018年6月8日 下午2:09:07 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
public class BaseController {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	protected BasicResultModel getBasicResultModel() {
		return applicationContext.getBean(BasicResultModel.class);
	}

}
