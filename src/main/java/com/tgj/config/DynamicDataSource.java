package com.tgj.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import com.tgj.config.base.DataSourceAlgorithmFactory;
import com.tgj.config.base.DataSourceType;
import com.tgj.config.base.DynamicDataSourceHolder;

/**
 * 
 * <p>
 *		数据源实现.
 * </p>
 *	<pre>
 *			由此数据源实现切换
 *	</pre>
 * @className DynamicDataSource
 * @author tgj  
 * @date 2018年6月8日 下午2:17:43 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
@Primary
@Component(value = "dynamicDataSource")
@DependsOn("dataSourceInitializer")
@ConfigurationProperties(prefix = "sys")
public class DynamicDataSource extends AbstractRoutingDataSource {
	
	private static final Logger LOG = LoggerFactory.getLogger(DynamicDataSource.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * 读数据库基名
	 */
	private String basicReadName;

	public String getBasicReadName() {
		return basicReadName;
	}

	public void setBasicReadName(String basicReadName) {
		this.basicReadName = basicReadName;
	}

	@Autowired
	@Qualifier("writeDataSource")
	private DataSource writeDataSource;
	
	@Autowired
	private DataSourceAlgorithmFactory dataSourceAlgorithmFactory;
	
	/**
	 * 算法名：polling、weightedPolling
	 */
	private String dataSourceAlgorithm;
	
	public String getDataSourceAlgorithm() {
		return dataSourceAlgorithm;
	}

	public void setDataSourceAlgorithm(String dataSourceAlgorithm) {
		this.dataSourceAlgorithm = dataSourceAlgorithm;
	}

	@Override
	protected Object determineCurrentLookupKey() {
		DataSourceType lookupKey = DynamicDataSourceHolder.getDataSource();
		if (LOG.isDebugEnabled()) {
			LOG.debug("lookupkey = " + lookupKey);
		}
		if (DataSourceType.writeDataSource == lookupKey) {
			return lookupKey;
		} else if (DataSourceType.readDataSource == lookupKey) {
			try {
				StringBuilder stringBuilder = new StringBuilder(basicReadName);
				stringBuilder.append(dataSourceAlgorithmFactory.getDataSourceAlgorithm(dataSourceAlgorithm, readDataSourceNum).getDataSource());
				return stringBuilder.toString();
			} catch (IllegalAccessException e) {
				LOG.error("paras exception", e);
			}
		}
		return null;
	}
	
	@PostConstruct
	public void init() {
		initDataSource();
		initReadDataSourceNum();
	}
	
	void initDataSource() {
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceType.writeDataSource, writeDataSource);
		Map<String, DataSource> map = applicationContext.getBeansOfType(DataSource.class);
		int i = 0;
		for (Entry<String, DataSource> en : map.entrySet()) {
			if (en.getValue() != writeDataSource && !en.getKey().equals("dynamicDataSource")) {
				targetDataSources.put(basicReadName + i, en.getValue());
			}
		}
		setTargetDataSources(targetDataSources);
		setDefaultTargetDataSource(writeDataSource);
	}
	
	private int readDataSourceNum = 0;
	
	void initReadDataSourceNum() {
		readDataSourceNum = applicationContext.getBeansOfType(DataSource.class).size() - 2;
	}
	
}
