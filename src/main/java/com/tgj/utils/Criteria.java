package com.tgj.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

/**
 * 
 * <p>
 * 构造查询条件.
 * </p>
 * 
 * @className Criteria
 * @author tgj
 * @date 2018年2月12日 上午10:32:50
 * 
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
public class Criteria<T> implements Specification<T>, Serializable, Cloneable {

	private static final long serialVersionUID = -8552397827484021514L;
	private List<Criterion> criterions = new ArrayList<>(0);

	private Map<String, Join<?, ?>> record = new HashMap<>();

	private Criteria() {
	}

	public static <T> Criteria<T> getInstance() {
		return new Criteria<>();
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (!criterions.isEmpty()) {
			List<Predicate> predicates = new ArrayList<>();
			for (Criterion criterion : criterions) {
				predicates.add(criterion.toPredicate(root, query, cb, record));
			}
			if (!predicates.isEmpty()) {
				record.clear();
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}
		return cb.conjunction();
	}

	public Criteria<T> add(Criterion criterion) {
		if (criterion != null) {
			criterions.add(criterion);
		}
		return this;
	}

}
