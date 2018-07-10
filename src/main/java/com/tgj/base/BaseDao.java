package com.tgj.base;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 
 * <p>
 * 		基类dao.
 * </p>
 * <p>
 * 		所有的dao都需要以接口的形式继承此dao，不需要实现(基于jpa)
 * </p>
 * 
 * @className BaseDao
 * @author tgj
 * @date 2018年2月12日 上午10:18:12
 * 
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
@NoRepositoryBean
public interface BaseDao<T> extends JpaRepository<T, Serializable>, JpaSpecificationExecutor<T> {

}