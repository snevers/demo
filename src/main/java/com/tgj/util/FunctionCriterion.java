package com.tgj.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 
 * <p>
 * 函数构造.
 * </p>
 * 
 * @className FunctionCriterion
 * @author tgj
 * @date 2018年2月12日 上午10:34:16
 * 
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
public class FunctionCriterion implements Criterion {
	private List<Object> list = new ArrayList<>();
	private String funcname;
	private Class<?> expectType;
	private int fileNameIndex;
	private Operator operator;
	private Object value;

	public FunctionCriterion(List<Object> list, String funcname, Class<?> expectType, int fileNameIndex,
			Operator operator, Object value) {
		super();
		if (list.size() <= fileNameIndex)
			throw new ArrayIndexOutOfBoundsException(fileNameIndex);
		if (list.isEmpty())
			throw new ArrayIndexOutOfBoundsException("List is empty");
		this.list = list;
		this.funcname = funcname;
		this.expectType = expectType;
		this.fileNameIndex = fileNameIndex;
		this.operator = operator;
		this.value = value;
	}

	public String getFuncname() {
		return funcname;
	}

	public void setFuncname(String funcname) {
		this.funcname = funcname;
	}

	public Class<?> getExpectType() {
		return expectType;
	}

	public void setExpectType(Class<?> expectType) {
		this.expectType = expectType;
	}

	public Operator getOperator() {
		return operator;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	public int getFileNameIndex() {
		return fileNameIndex;
	}

	public void setFileNameIndex(int fileNameIndex) {
		this.fileNameIndex = fileNameIndex;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder,
			Map<String, Join<?, ?>> record) {
		Path path = null;
		String fieldName = list.get(fileNameIndex).toString();
		if (fieldName.contains(".")) {
			String[] paras = fieldName.split("\\.");
			int size = paras.length;
			StringBuilder sb = new StringBuilder();
			String tempKey = "";
			Join join = null;
			for (int i = 0; i < size; i++) {
				sb.append(paras[i] + ".");
				tempKey = sb.substring(0, sb.length() - 1);
				if (record.containsKey(tempKey)) {
					join = record.get(tempKey);
				} else if (i == 0) {
					join = root.join(tempKey);
					record.put(tempKey, join);
				} else if (i <= size - 2) {
					join = join.join(paras[i]);
					record.put(tempKey, join);
				} else {
					path = join.get(paras[i]);
				}
			}
		} else {
			path = root.get(fieldName);
		}
		switch (operator) {
		case gt:
			validateComparable(value);
			return builder.greaterThan(initExpression(builder, path), (Comparable) value);
		case ge:
			validateComparable(value);
			return builder.greaterThanOrEqualTo(initExpression(builder, path), (Comparable) value);
		case lt:
			validateComparable(value);
			return builder.lessThan(initExpression(builder, path), (Comparable) value);
		case le:
			validateComparable(value);
			return builder.lessThanOrEqualTo(initExpression(builder, path), (Comparable) value);
		case eq:
			return builder.equal(initExpression(builder, path), value);
		case notin:
			In in = builder.in(initExpression(builder, path));
			Collection<?> collecitons = null;
			if (value instanceof Collection<?>) {
				collecitons = (Collection<?>) value;
			} else if (value.getClass().isArray()) {
				collecitons = Arrays.asList(value);
			}
			for (Object object : collecitons) {
				in.value(object);
			}
			return builder.not(in);
		case ne:
			return builder.notEqual(initExpression(builder, path), value);
		case isnull:
			return builder.isNotNull(initExpression(builder, path));
		case between:
			if (value.getClass().isArray()) {
				Object[] objs = (Object[]) value;
				validateComparable(objs[0]);
				validateSize(Arrays.asList(objs));
				return builder.between(initExpression(builder, path), (Comparable) objs[0], (Comparable) objs[1]);
			} else if (value instanceof List<?>) {
				List<?> objs = (List<?>) value;
				validateSize(objs);
				return builder.between(initExpression(builder, path), (Comparable) objs.get(0),
						(Comparable) objs.get(1));
			} else if (value instanceof Set<?>) {
				Set<?> objs = (Set<?>) value;
				validateSize(objs);
				Iterator<?> it = objs.iterator();
				Object start = it.next();
				Object end = it.next();
				return builder.between(initExpression(builder, path), (Comparable) start, (Comparable) end);
			} else {
				throw new IllegalArgumentException("value should be array or list or set");
			}
		case in:
			if (!(value instanceof Collection<?>))
				throw new IllegalArgumentException("value should be <? T Collection>");
			return initExpression(builder, path).in((Collection) value);
		case like:
			if (null == value || "".equals(value.toString().trim()))
				throw new IllegalArgumentException("value is empty");
			return builder.like(initExpression(builder, path), (String) value);
		case notnull:
			return builder.isNotNull(initExpression(builder, path));
		default:
			throw new IllegalArgumentException("unSupportOperator");
		}
	}
	
	private void validateSize(Collection<?> objs) {
		if (objs.size() != 2)
			throw new IllegalArgumentException("value[T] length must be 2");
	}
	
	private void validateComparable(Object value) {
		if (!(value instanceof Comparable<?>))
			throw new IllegalArgumentException("value should be <T ? Comparable>");
	}

	@SuppressWarnings("rawtypes")
	private Expression initExpression(CriteriaBuilder builder, Path<?> path) {
		int len = list.size();
		Expression<?>[] exps = new Expression[len];
		for (int i = 0; i < len; i++) {
			exps[i] = i != fileNameIndex ? builder.literal(list.get(i)) : path;
		}
		return builder.function(funcname, expectType, exps);
	}

}
