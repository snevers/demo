package com.tgj.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * 条件构造类.
 * </p>
 * 
 * @className Restrictions
 * @author tgj
 * @date 2018年2月12日 上午10:42:02
 * 
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
public class Restrictions {
	
	private static final Logger LOG = LoggerFactory.getLogger(Restrictions.class);
	
	private Restrictions() {}

	public static Criterion eq(String fieldName, Object value) {
		return new SampleCriterion(fieldName, Operator.eq, value);
	}

	public static Criterion gt(String fieldName, Object value) {
		return new SampleCriterion(fieldName, Operator.gt, value);
	}

	public static Criterion ge(String fieldName, Object value) {
		return new SampleCriterion(fieldName, Operator.ge, value);
	}

	public static Criterion lt(String fieldName, Object value) {
		return new SampleCriterion(fieldName, Operator.lt, value);
	}

	public static Criterion le(String fieldName, Object value) {
		return new SampleCriterion(fieldName, Operator.le, value);
	}

	public static Criterion ne(String fieldName, Object value) {
		return new SampleCriterion(fieldName, Operator.ne, value);
	}

	public static Criterion isnull(String fieldName) {
		return new SampleCriterion(fieldName, Operator.isnull, null);
	}

	public static Criterion between(String fieldName, Object value) {
		return new SampleCriterion(fieldName, Operator.between, value);
	}

	public static Criterion notIn(String fieldName, Object value) {
		return new SampleCriterion(fieldName, Operator.notin, value);
	}

	public static Criterion in(String fieldName, Object value) {
		return new SampleCriterion(fieldName, Operator.in, value);
	}

	public static Criterion like(String fieldName, Object value) {
		return new SampleCriterion(fieldName, Operator.like, value);
	}

	public static Criterion notnull(String fieldName) {
		return new SampleCriterion(fieldName, Operator.notnull, null);
	}

	public static Criterion and(Criterion[] criterions) {
		return new LogicalCriterion(criterions, Operator.and);
	}

	public static Criterion or(Criterion[] criterions) {
		return new LogicalCriterion(criterions, Operator.or);
	}

	public static Criterion add(Map<String, Object> map) {
		validateNull(map);
		List<Criterion> list = new ArrayList<>();
		for (Entry<String, Object> en : map.entrySet()) {
			list.add(new SampleCriterion(en.getKey(), Operator.eq, en.getValue()));
		}
		return and(list.toArray(new Criterion[0]));
	}
	
	private static void validateNull(Object paras) {
		if (paras == null)
			throw new IllegalArgumentException("paramater is not allow null");
	}

	public static Criterion add(Object paras) {
		validateNull(paras);
		List<Criterion> list = new ArrayList<>();
		try {
			parasEntity(paras, new StringBuilder(), list);
		} catch (IllegalAccessException | NoSuchMethodException | SecurityException e) {
			LOG.error("parse entity error", e);
		}
		return and(list.toArray(new Criterion[0]));
	}

	public static Criterion function(List<Object> list, String funcname, Class<?> expectType, int fileNameIndex,
			Operator operator, Object value) {
		return new FunctionCriterion(list, funcname, expectType, fileNameIndex, operator, value);
	}

	private static void parasEntity(Object paras, StringBuilder sb, List<Criterion> list)
			throws IllegalAccessException, NoSuchMethodException {
		Field[] fields = paras.getClass().getDeclaredFields();
		String basePath = sb.toString();
		for (Field field : fields) {
			field.setAccessible(true);
			Object obj = field.get(paras);
			if (obj == null || !"private".equals(Modifier.toString(field.getModifiers())))
				continue;
			Annotation[] annos = field.getAnnotations();
			if (validateTransient(annos))
				continue;
			Method method = getMehtod(paras.getClass(), field.getName());
			if (method != null) {
				annos = method.getAnnotations();
			}
			if (validateTransient(annos))
				continue;

			if (obj instanceof Set<?>) {
				Set<?> sets = (Set<?>) obj;
				if (sets.isEmpty())
					continue;
			}
			sb.append(field.getName() + ".");
			Class<?> cls = field.getType();
			boolean isJunction = false;
			if (cls.isAssignableFrom(Set.class)) {
				Type type = field.getGenericType();
				if (type instanceof ParameterizedType) {
					ParameterizedType parameterizedType = (ParameterizedType) type;
					try {
						annos = Class.forName(parameterizedType.getActualTypeArguments()[0].getTypeName())
								.getAnnotations();
						for (Annotation annotation : annos) {
							if (annotation.annotationType().isAssignableFrom(Entity.class)) {
								isJunction = true;
								Set<?> sets = (Set<?>) obj;
								Set<Criterion> orList = new HashSet<>();
								for (Object object : sets) {
									List<Criterion> andList = new ArrayList<>();
									parasEntity(object, sb, andList);
									sb.delete(0, sb.length());
									sb.append(basePath);
									orList.add(and(andList.toArray(new Criterion[0])));
								}
								list.addAll(orList);
								sb.delete(0, sb.length());
								sb.append(basePath);
								break;
							}
						}
					} catch (ClassNotFoundException e) {
						LOG.error("class not found", e);
					}
				}
			} else {
				annos = cls.getAnnotations();
				for (Annotation annotation : annos) {
					if (annotation.annotationType().isAssignableFrom(Entity.class)) {
						isJunction = true;
						parasEntity(obj, sb, list);
						sb.delete(0, sb.length());
						sb.append(basePath);
						break;
					}
				}
			}
			if (isJunction)
				continue;
			list.add(eq(sb.substring(0, sb.length() - 1), obj));
			sb.delete(0, sb.length());
			sb.append(basePath);
		}

	}

	private static boolean validateTransient(Annotation[] annos) {
		for (Annotation annotation : annos) {
			if (annotation.annotationType().isAssignableFrom(Transient.class)) {
				return true;
			}
		}
		return false;
	}

	private static Method getMehtod(Class<?> cls, String fieldName) {
		try {
			return cls.getDeclaredMethod("get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1));
		} catch (NoSuchMethodException | SecurityException e) {
			LOG.error("don't have get method", e);
		}
		return null;
	}

}
