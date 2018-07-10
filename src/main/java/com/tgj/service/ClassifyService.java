package com.tgj.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tgj.base.BaseJpaService;
import com.tgj.entity.Classify;
import com.tgj.entity.Project;

/**
 * 
 * <p>
 *		ClassifyService.
 * </p>
 * <p>
 *		实现ClassifyService相关业务处理
 * </p>
 * @className ClassifyService
 * @author Server  
 * @date 2018年5月16日 下午4:36:36 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
@Service
public class ClassifyService extends BaseJpaService<Classify> {

	@Transactional
	public Page<Classify> findByPid(Integer pid, Pageable pageable) {
		Classify classify = new Classify();
		Project project = new Project();
		project.setId(pid);
		classify.setProject(project);
		return findAll(Example.of(classify), pageable);
	}
}
