package com.tgj.api.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tgj.base.BaseController;
import com.tgj.entity.Classify;
import com.tgj.service.ClassifyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * <p>
 *		Classify控制器.
 * </p>
 * <p>
 *		Classify相关接口
 * </p>
 * @className ClassifyController
 * @author Server  
 * @date 2018年5月16日 下午4:32:25 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
@RestController
@Api(value = "分类信息", produces = "application/json", consumes = "application/json")
@RequestMapping(value = "/client/api/classify/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClassifyController extends BaseController {

	@Autowired
	private ClassifyService classifyService;
	
	@ApiOperation("查询及排序")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "project's id", name = "pid", paramType = "query", dataType = "string", required = true),
		@ApiImplicitParam(value = "true表示升序，默认按主键降序", name = "asc", paramType = "query", dataType = "boolean", defaultValue = "false"),
		@ApiImplicitParam(value = "当前页(1表示第一页)", name = "pageNow", paramType = "query", dataType = "int", required = true, defaultValue = "1"),
		@ApiImplicitParam(value = "数据条数", name = "pageSize", paramType = "query", dataType = "int", required = true, defaultValue = "10"),
	})
	@RequestMapping(method = RequestMethod.POST, value = "findPage")
	public Page<Classify> findPage(Integer pid, boolean asc, int pageNow, int pageSize) {
		if (Objects.isNull(pid)) {
			throw new IllegalArgumentException("pid is null");
		}
		pageNow--;
		Pageable pageable = new PageRequest(pageNow, pageSize, new Sort(asc ? Direction.ASC : Direction.DESC, "id"));
		return classifyService.findByPid(pid, pageable);
	}
	
	@ApiOperation(value = "按主键查询")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "主键", name = "id", paramType = "query", required = true)
	})
	@RequestMapping(method = RequestMethod.POST, value = "findOne")
	public Classify findOne(Integer id) {
		Assert.notNull(id, "id is null");
		return classifyService.findOne(id);
	}
}
