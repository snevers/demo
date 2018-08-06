package com.tgj.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.ClassUtils;

/**
 * 
 * @author TGJ
 *
 */
public class BeanUtil {
	
	public static void copyProperties(Object orig, Object dest, boolean ignoreEmpty, String... ignoreProperties) {
		if(null == orig || null == dest) throw new IllegalArgumentException("参数不能为null");
		Class<?> actualEditable = dest.getClass();
		PropertyDescriptor[] targetPds = PropertyUtils.getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);
		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
				PropertyDescriptor sourcePd = null;
				try {
					sourcePd = PropertyUtils.getPropertyDescriptor(orig, targetPd.getName());
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					e.printStackTrace();
				}
				try {
					if (sourcePd != null && (!ignoreEmpty || sourcePd.getReadMethod().invoke(orig) != null)) {
						Method readMethod = sourcePd.getReadMethod();
						if (readMethod != null && ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
							if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
								readMethod.setAccessible(true);
							}
							Object value = readMethod.invoke(orig);
							if (value instanceof Collection<?>) {
								Class<?> clazz = value.getClass();
								Method method = clazz.getDeclaredMethod("isEmpty");
								if ((boolean) method.invoke(value)) continue;
							}
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(dest, value);
						}
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
