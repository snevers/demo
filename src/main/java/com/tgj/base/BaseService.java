package com.tgj.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.tgj.util.Criteria;

/**
 * 
 * <p>
 *		Mybatis和jpa综合基类.
 * </p>
 *	<pre>
 *			如果一个service要同时具备这两种其它方法，需要继承此类
 *	</pre>
 * @className BaseService
 * @author tgj  
 * @date 2018年6月8日 下午2:15:26 
 *  
 * @param <T>  实体
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
public class BaseService<T> {

	protected Criteria<T> getCriteria() {
		return Criteria.getInstance();
	}
	
	@Autowired
	private BaseDao<T> baseDao;
	
	@Transactional
	public T saveOrUpdate(T entity) {
		return baseDao.save(entity);
	}
	
	@Transactional
	public T saveAndFlush(T entity) {
		return baseDao.saveAndFlush(entity);
	}
	
	@Transactional
	public List<T> saveOrUpdate(Collection<T> entities) {
		return baseDao.save(entities);
	}
	
	@Transactional(readOnly = true)
	public List<T> findAll() {
		return baseDao.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<T> findAll(Example<T> example) {
		return baseDao.findAll(example);
	}
	
	@Transactional(readOnly = true)
	public List<T> findAll(Collection<Serializable> ids) {
		return baseDao.findAll(ids);
	}
	
	@Transactional(readOnly = true)
	public Page<T> findAll(Pageable pageable) {
		return baseDao.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public List<T> findAll(Sort sort) {
		return baseDao.findAll(sort);
	}
	
	@Transactional(readOnly = true)
	public List<T> findAll(Specification<T> criteria) {
		return baseDao.findAll(criteria);
	}
	
	@Transactional(readOnly = true)
	public List<T> findAll(Specification<T> criteria, Sort sort) {
		return baseDao.findAll(criteria, sort);
	}
	
	@Transactional(readOnly = true)
	public Page<T> findAll(Specification<T> criteria, Pageable pageable) {
		return baseDao.findAll(criteria, pageable);
	}
	
	@Transactional(readOnly = true)
	public List<T> findAll(Example<T> example, Sort sort) {
		return baseDao.findAll(example, sort);
	}
	
	@Transactional(readOnly = true)
	public Page<T> findAll(Example<T> example, Pageable pageable) {
		return baseDao.findAll(example, pageable);
	}
	
	@Transactional(readOnly = true)
	public long count() {
		return baseDao.count();
	}
	
	@Transactional(readOnly = true)
	public long count(Example<T> example) {
		return baseDao.count(example);
	}
	
	@Transactional(readOnly = true)
	public long count(Specification<T> criteria) {
		return baseDao.count(criteria);
	}
	
	@Transactional
	public void delete(Collection<T> entities) {
		baseDao.delete(entities);
	}
	
	@Transactional
	public void delete(Serializable id) {
		baseDao.delete(id);
	}
	
	@Transactional
	public void delete(T entity) {
		baseDao.delete(entity);
	}
	
	@Transactional
	public void deleteAllInBatch() {
		baseDao.deleteAllInBatch();
	}
	
	@Transactional
	public void deleteInBatch(Collection<T> entities) {
		baseDao.deleteInBatch(entities);
	}
	
	@Transactional(readOnly = true)
	public boolean exists(Example<T> example) {
		return baseDao.exists(example);
	}
	
	@Transactional(readOnly = true)
	public boolean exists(Serializable id) {
		return baseDao.exists(id);
	}
	
	@Transactional
	public void flush() {
		baseDao.flush();
	}
	
	@Transactional(readOnly = true)
	public T findOne(Serializable id) {
		return baseDao.findOne(id);
	}
	
	@Transactional(readOnly = true)
	public T findOne(Example<T> example) {
		return baseDao.findOne(example);
	}
	
	@Transactional(readOnly = true)
	public T findOne(Specification<T> criteria) {
		return baseDao.findOne(criteria);
	}
	
	@Transactional
	public T getOne(Serializable id) {
		return baseDao.getOne(id);
	}
	
	/**
	 * mybatis
	 */
	
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
