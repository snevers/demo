package com.tgj.base;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 
 * <p>
 *		基本Mapper.
 * </p>
 *	<pre>
 *			建议所有Mapper都继承此类（基于mapper）
 *	</pre>
 * @className BaseMapper
 * @author tgj  
 * @date 2018年6月8日 下午2:11:52 
 *  
 * @param <T>  实体
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
