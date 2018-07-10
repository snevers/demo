package com.tgj.config.base;

import java.util.concurrent.atomic.LongAdder;

/**
 * 
 * <p>
 *		轮询实现.
 * </p>
 * @className DataSourceAlgorithmByPolling
 * @author tgj  
 * @date 2018年6月8日 下午2:18:52 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
public class DataSourceAlgorithmByPolling implements DataSourceAlgorithm {
	
	private static final LongAdder COUNT = new LongAdder();
	
	static int readDataSourceNum;
	
	public DataSourceAlgorithmByPolling(int readDataSourceNum) {
		DataSourceAlgorithmByPolling.readDataSourceNum = readDataSourceNum;
	}

	@Override
	public String getDataSource() {
		String result = "0";
		synchronized (this) {
			if (COUNT.intValue() >= readDataSourceNum) {
				COUNT.reset();
			} else {
				result = COUNT.toString();
				COUNT.increment();
			}
		}
		return result;
	}
	
}
