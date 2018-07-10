package com.tgj.api.admin.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tgj.base.BaseController;
import com.tgj.entity.Demand;
import com.tgj.model.BasicResultModel;
import com.tgj.service.DemandService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

/**
 * 
 * <p>
 *		Demand控制器.
 * </p>
 * <p>
 *		demand相关接口
 * </p>
 * @className DemandController
 * @author Server  
 * @date 2018年5月16日 下午4:32:25 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
@RestController
@Api(value = "后台Demand信息", produces = "application/json", consumes = "application/json", authorizations = {@Authorization(value = "basicAuth")})
@RequestMapping(value = "admin/api/demand/", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminDemandController extends BaseController {

	@Autowired
	private DemandService demandService;
	
	@ApiImplicitParams({
		@ApiImplicitParam(value = "时间(格式yyyy-MM-dd HH:mm:SS)", name = "createDateStart", paramType = "query"),
		@ApiImplicitParam(value = "时间(格式yyyy-MM-dd HH:mm:SS)", name = "createDateEnd", paramType = "query"),
		@ApiImplicitParam(value = "0表示未处理，1表示处理过", name = "status", paramType = "query", dataType = "int"),
		@ApiImplicitParam(value = "true表示升序，默认按createDate降序", name = "asc", paramType = "query", dataType = "boolean", defaultValue = "false"),
		@ApiImplicitParam(value = "当前页(1表示第一页)", name = "pageNow", paramType = "query", dataType = "int", required = true, defaultValue = "1"),
		@ApiImplicitParam(value = "数据条数", name = "pageSize", paramType = "query", dataType = "int", required = true, defaultValue = "10"),
	})
	@ApiOperation(value = "查询及排序")
	@RequestMapping(method = RequestMethod.GET, value = "findPage")
	public Page<Demand> findPage(String nameAndContent, String email, String phoneNum, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS") Date createDateStart, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:SS") Date createDateEnd, Short status, boolean asc, int pageNow, int pageSize) {
		pageNow--;
		Pageable pageable = new PageRequest(pageNow, pageSize, new Sort(asc ? Direction.ASC : Direction.DESC, "createDate"));
		return demandService.findPage(nameAndContent, email, phoneNum, createDateStart, createDateEnd, status, pageable);
	}
	
	@ApiOperation(value = "按主键ID删除")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "主键", name = "id", paramType = "query", required = true)
	})
	@RequestMapping(method = RequestMethod.DELETE, value = "delete")
	public BasicResultModel delete(Long id) {
		Assert.notNull(id, "id is null");
		demandService.delete(id);
		return getBasicResultModel();
	}
	
	@ApiOperation(value = "按主键ID批量删除")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "主键数组(格式：1,2)", name = "ids", paramType = "query", required = true)
	})
	@RequestMapping(method = RequestMethod.DELETE, value = "deletes")
	public BasicResultModel delete(Long[] ids) {
		Assert.notNull(ids, "ids is null");
		Collection<Demand> demands = new ArrayList<>();
		for (Long id : ids) {
			Demand demand = new Demand();
			demand.setId(id);
			demands.add(demand);
		}
		demandService.deleteInBatch(demands);
		return getBasicResultModel();
	}
}