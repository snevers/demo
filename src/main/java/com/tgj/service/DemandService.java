package com.tgj.service;

import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tgj.annotion.TargetDataSource;
import com.tgj.base.BaseService;
import com.tgj.config.base.DataSourceType;
import com.tgj.entity.Demand;
import com.tgj.mapper.DemandMapper;
import com.tgj.util.Criteria;
import com.tgj.util.Restrictions;

import tk.mybatis.mapper.entity.Example;

/**
 * 
 * <p>
 *		DemandService.
 * </p>
 * <p>
 *		实现DemandService相关业务处理
 * </p>
 * @className DemandService
 * @author Server  
 * @date 2018年5月16日 下午4:36:36 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
@Service
public class DemandService extends BaseService<Demand> {
	
	@Autowired
	private DemandMapper demandMapper;
	
	@Transactional()
	@TargetDataSource(DataSourceType.readDataSource) 
	public PageInfo<Demand> selectPage(int pageNow, int pageSize) {
		PageHelper.startPage(pageNow, pageSize, true);
		Example example = new Example(Demand.class);
		example.createCriteria().andLike("email", "%@163.com");
		return new PageInfo<>(demandMapper.selectByExample(example));
	}
	
	@Transactional()
	public int insertSelective(Demand demand) {
		return demandMapper.insertSelective(demand);
	}
	
	@Transactional()
	@TargetDataSource(DataSourceType.readDataSource)
	public Page<Demand> findPage(String nameAndContent, String email, String phoneNum, Date createDateStart, Date createDateEnd, Short status, Pageable pageable) {
		Criteria<Demand> criteria = getCriteria();
		if (StringUtils.isNotBlank(nameAndContent)) {
			criteria.add(Restrictions.like("nameAndContent", nameAndContent + "%"));
		}
		if (StringUtils.isNotBlank(email)) {
			criteria.add(Restrictions.eq("email", email));
		}
		if (StringUtils.isNotBlank(phoneNum)) {
			criteria.add(Restrictions.eq("phoneNum", phoneNum));
		}
		if (Objects.nonNull(createDateStart)) {
			criteria.add(Restrictions.ge("createDate", createDateStart));
		}
		if (Objects.nonNull(createDateEnd)) {
			criteria.add(Restrictions.le("createDate", createDateEnd));
		}
		if (Objects.nonNull(status)) {
			criteria.add(Restrictions.eq("status", status));
		}
		return findAll(criteria, pageable);
	}
}
