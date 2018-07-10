package com.tgj.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * <p>
 *		基本MybatisService.
 * </p>
 *	<pre>
 *			建议Mybatis的service层都继承此类（必须要有对应的Mapper）
 *	</pre>
 * @className BaseMybatisService
 * @author tgj  
 * @date 2018年6月8日 下午2:12:52 
 *  
 * @param <T>  实体
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
public class BaseMybatisService<T> {

	@Autowired
	private BaseMapper<T> baseMapper;
	
	@Transactional
	public int deleteByEntity(T record) {
		return baseMapper.delete(record);
	}
	
	@Transactional
	public int deleteByExample(Object example) {
		return baseMapper.deleteByExample(example);
	}
	
	@Transactional
	public int deleteByPrimaryKey(Object key) {
		return baseMapper.deleteByPrimaryKey(key);
	}
	
	@Transactional
	public boolean existsWithPrimaryKey(Object key) {
		return baseMapper.existsWithPrimaryKey(key);
	}
	
	@Transactional
	public int insertEntity(T record) {
		return baseMapper.insert(record);
	}
	
	@Transactional
	public int insertList(List<T> recordList) {
		return baseMapper.insertList(recordList);
	}
	
	@Transactional
	public int insertSelective(T record) {
		return baseMapper.insertSelective(record);
	}
	
	@Transactional
	public int insertUseGeneratedKeys(T record) {
		return baseMapper.insertUseGeneratedKeys(record);
	}
	
	@Transactional(readOnly = true)
	public List<T> selectAll() {
		return baseMapper.selectAll();
	}
	
	@Transactional(readOnly = true)
	public List<T> selectEntity(T record) {
		return baseMapper.select(record);
	}
	
	@Transactional(readOnly = true)
	public T selectByPrimary(Object key) {
		return baseMapper.selectByPrimaryKey(key);
	}
	
	@Transactional(readOnly = true)
	public T selectOne(T t) {
		return baseMapper.selectOne(t);
	}
	
	@Transactional(readOnly = true)
	public T selectOneByExample(Object example) {
		return baseMapper.selectOneByExample(example);
	}
	
	@Transactional(readOnly = true)
	public List<T> selectByExample(Object example) {
		return baseMapper.selectByExample(example);
	}
	
	@Transactional(readOnly = true)
	public List<T> selectByExampleAndRowBounds(Object example, RowBounds rowBounds) {
		return baseMapper.selectByExampleAndRowBounds(example, rowBounds);
	}
	
	@Transactional(readOnly = true)
	public List<T> selectByRowBounds(T record, RowBounds rowBounds) {
		return baseMapper.selectByRowBounds(record, rowBounds);
	}
	
	@Transactional(readOnly = true)
	public int selectCount(T record) {
		return baseMapper.selectCount(record);
	}
	
	@Transactional(readOnly = true)
	public int selectCountByExample(Object example) {
		return baseMapper.selectCountByExample(example);
	}
	
	@Transactional
	public int updateByExample(T record, Object example) {
		return baseMapper.updateByExample(record, example);
	}
	
	@Transactional
	public int updateByExampleSelective(T record, Object example) {
		return baseMapper.updateByExampleSelective(record, example);
	}
	
	@Transactional
	public int updateByPrimaryKey(T record) {
		return baseMapper.updateByPrimaryKey(record);
	}
	
	@Transactional
	public int updateByPrimaryKeySelective(T record) {
		return baseMapper.updateByPrimaryKeySelective(record);
	}
}
