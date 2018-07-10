package com.tgj.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tgj.base.BaseJpaService;
import com.tgj.entity.Model;
import com.tgj.util.Criteria;
import com.tgj.util.Restrictions;

/**
 * 
 * <p>
 *		ModelService.
 * </p>
 * <p>
 *		实现ModelService相关业务处理
 * </p>
 * @className ModelService
 * @author Server  
 * @date 2018年5月16日 下午4:36:36 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
@Service
public class ModelService extends BaseJpaService<Model> {

	public Page<Model> findByPid(Integer pid, Pageable pageable) {
		Criteria<Model> criteria = getCriteria();
		criteria.add(Restrictions.eq("classify.id", pid));
		return findAll(criteria, pageable);
	}

}
