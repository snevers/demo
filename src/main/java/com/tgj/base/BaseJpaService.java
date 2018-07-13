package com.tgj.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
 *		service基类.
 * </p>
 * <p>
 *		编写Service层，建议都继承此类（基于JPA,要求必须有对应的Repository）
 * </p>
 * @className BaseService
 * @author Server  
 * @date 2018年5月16日 下午4:35:47 
 *  
 * @param <T>  实体
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
public class BaseJpaService<T> {
	
	protected Criteria<T> getCriteria() {
		return Criteria.getInstance();
	}
	
	@Autowired
	private BaseDao<T> baseDao;
	
	@Transactional
	public T save(T entity) {
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
	
}
