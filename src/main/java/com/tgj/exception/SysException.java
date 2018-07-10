package com.tgj.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tgj.model.BasicResultModel;

/**
 * 
 * <p>
 * 		统一异常处理器.
 * </p>
 * 
 * @className SysException
 * @author tgj
 * @date 2018年2月12日 上午10:24:30
 * 
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
@ControllerAdvice
public class SysException {

	private static final Logger LOG = LoggerFactory.getLogger(SysException.class);
	
	@Autowired
	private ApplicationContext applicationContext;

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public BasicResultModel handleException(Exception e) {
		LOG.error("系统异常", e);
		BasicResultModel result = applicationContext.getBean(BasicResultModel.class);
		if (e instanceof IllegalArgumentException) {
			result.setStatus("1001");
			result.setMessage("参数错误");
		} else {
			result.setStatus("3001");
			result.setMessage("服务器异常");
		}
		return result;
	}
}
