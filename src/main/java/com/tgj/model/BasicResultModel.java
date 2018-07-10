package com.tgj.model;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * <p>
 *		返回数据通用类型.
 * </p>
 * @className BasicResultModel
 * @author tgj  
 * @date 2018年6月8日 下午2:21:08 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
@Component
@Scope("prototype")
public class BasicResultModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String status = "200";
	
	private String message = "操作成功";
	
	private Long timestamp = System.currentTimeMillis();

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
