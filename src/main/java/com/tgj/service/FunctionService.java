package com.tgj.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tgj.base.BaseJpaService;
import com.tgj.entity.Function;
import com.tgj.utils.Criteria;
import com.tgj.utils.Restrictions;

/**
 * 
 * <p>
 *		FunctionService.
 * </p>
 * <p>
 *		实现FunctionService相关业务处理
 * </p>
 * @className FunctionService
 * @author Server  
 * @date 2018年5月16日 下午4:36:36 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
@Service
public class FunctionService extends BaseJpaService<Function> {

	public Page<Function> findByPid(Integer pid, Pageable pageable) {
		Criteria<Function> criteria = getCriteria();
		criteria.add(Restrictions.eq("model.id", pid));
		return findAll(criteria, pageable);
	}
}
