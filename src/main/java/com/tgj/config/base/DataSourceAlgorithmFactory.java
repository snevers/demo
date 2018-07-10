package com.tgj.config.base;

import org.springframework.stereotype.Component;

/**
 * 
 * <p>
 *		切换工厂.
 * </p>
 * @className DataSourceAlgorithmFactory
 * @author tgj  
 * @date 2018年6月8日 下午2:19:32 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
@Component
public class DataSourceAlgorithmFactory {

	public DataSourceAlgorithm getDataSourceAlgorithm(String type, int readDataSourceNum) throws IllegalAccessException {
		if ("polling".equals(type)) {
			return new DataSourceAlgorithmByPolling(readDataSourceNum);
		} else if ("weightedPolling".equals(type)) {
			
		}
		throw new IllegalAccessException("don't have any policy");
	}
}
