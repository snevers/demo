package com.tgj.config.base;

/**
 * 
 * <p>
 *		数据库线程绑定类.
 * </p>
 * @className DynamicDataSourceHolder
 * @author tgj  
 * @date 2018年6月8日 下午2:20:13 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
public final class DynamicDataSourceHolder {
	
    private static final ThreadLocal<DataSourceType> dataSources = new ThreadLocal<>();

    public static void setDataSource(DataSourceType dataSourceName) {
        dataSources.set(dataSourceName);
    }

    public static DataSourceType getDataSource() {
        return dataSources.get();
    }

    public static void clearDataSource() {
        dataSources.remove();
    }
}
