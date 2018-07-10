package com.tgj.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 
 * <p>
 * 逻辑条件类.
 * </p>
 * 
 * @className LogicalCriterion
 * @author tgj
 * @date 2018年2月12日 上午10:38:24
 * 
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
public class LogicalCriterion implements Criterion {
	private Criterion[] criterions;
	private Operator operator;

	public Criterion[] getCriterions() {
		return criterions;
	}

	public void setCriterions(Criterion[] criterions) {
		this.criterions = criterions;
	}

	public LogicalCriterion(Criterion[] criterions, Operator operator) {
		super();
		this.criterions = criterions;
		this.operator = operator;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder,
			Map<String, Join<?, ?>> record) {
		List<Predicate> predicates = new ArrayList<>();
		for (Criterion criterion : criterions) {
			predicates.add(criterion.toPredicate(root, query, builder, record));
		}
		switch (operator) {
		case or:
			return builder.or(predicates.toArray(new Predicate[predicates.size()]));
		case and:
			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		default:
			throw new IllegalArgumentException("unSupportOperator");
		}
	}

}
