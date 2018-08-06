package com.tgj.utils;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 
 * <p>
 * 条件构造基类.
 * </p>
 * 
 * @className Criterion
 * @author tgj
 * @date 2018年2月12日 上午10:36:48
 * 
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
public interface Criterion {

	public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder,
			Map<String, Join<?, ?>> record);

}
