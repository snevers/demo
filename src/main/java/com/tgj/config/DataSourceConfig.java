package com.tgj.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * <p>
 *	系统配置.
 * </p>
 * <p>
 *	配置相关的Bean
 * </p>
 * @className Config
 * @author Server  
 * @date 2018年5月21日 上午9:44:49 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
@Configuration
public class DataSourceConfig {
	
	/**
	 * 
	 * <p>
	 *		写据源.
	 * </p>
	 * @title writeDruidDataSource
	 * @return DataSource
	 */
    @Bean(name = "writeDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dbcp2.write")
    public DataSource writeDruidDataSource() {
    	DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		return dataSourceBuilder.build();
    }
	
    /**
	 * 
	 * <p>
	 *		读据源.
	 * </p>
	 * @title readDataSource
	 * @return DataSource
	 */
	@Bean(name = "readDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dbcp2.read")
    public DataSource readDruidDataSource() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		return dataSourceBuilder.build();
    }
	
}
