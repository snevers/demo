package com.tgj.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 
 * <p>
 *		自己注册Repositories.
 * </p>
 *	<pre>
 *			暂时不需要
 *	</pre>
 * @className BasicRepositories
 * @author tgj  
 * @date 2018年6月8日 下午2:16:47 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
//@Configuration
//@EnableJpaRepositories(entityManagerFactoryRef = "writeEntityManagerFactory", transactionManagerRef = "writeTransactionManager", value = "basicRepositories", basePackages = "com.rykj.dao")
public class BasicRepositories {

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	@Qualifier("dynamicDataSource")
	private DataSource dynamicDataSource;

	@Primary
	@Bean(name = "writeEntityManagerFactory")
	public EntityManagerFactory entityManagerFactory(EntityManagerFactoryBuilder builder) {
		LocalContainerEntityManagerFactoryBean factory = builder.dataSource(dynamicDataSource).properties(jpaProperties.getProperties())
				.packages("com.rykj.entity").persistenceUnit("writePersistenceUnit").build();
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Primary
	@Bean(name = "writeTransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("writeEntityManagerFactory") EntityManagerFactory writeEntityManagerFactory) {
		return new JpaTransactionManager(writeEntityManagerFactory);
	}
}
