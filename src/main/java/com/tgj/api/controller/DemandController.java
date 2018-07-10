package com.tgj.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tgj.base.BaseController;
import com.tgj.entity.Demand;
import com.tgj.model.BasicResultModel;
import com.tgj.service.DemandService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import tk.mybatis.mapper.entity.Example;

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
@Api(value = "Demand信息", produces = "application/json", consumes = "application/json")
@RequestMapping(value = "/client/api/demand/", produces = MediaType.APPLICATION_JSON_VALUE)
public class DemandController extends BaseController {

	@Autowired
	private DemandService demandService;
	
	@RequestMapping(method = RequestMethod.GET, value = "test")
	public PageInfo<Demand> test() {
		PageHelper.startPage(1, 1, true);
		Example example = new Example(Demand.class);
		example.orderBy("createDate").desc();
		example.createCriteria().andLike("email", "%@163.com");
		PageInfo<Demand> pageInfo = new PageInfo<>(demandService.selectByExample(example));
		return pageInfo;
	}
	
	@ApiOperation("添加需求用户信息")
	@RequestMapping(method = RequestMethod.POST, value = "save")
	public BasicResultModel save(@ApiParam(name = "需求用户对象", value = "传入json格式", required = true) Demand demand) {
		Assert.notNull(demand, "demand is null");
		BasicResultModel result = getBasicResultModel();
		if (!demand.getPhoneNum().matches("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$")) {
			result.setStatus("1001");
			result.setMessage("手机号码不正确");
			return result;
		}
		demand.setEmail(HtmlUtils.htmlEscape(demand.getEmail()));
		if (StringUtils.isNotBlank(demand.getEmail()) && !demand.getEmail().matches("^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
			result.setStatus("1001");
			result.setMessage("邮件地址不正确");
			return result;
		}
		demand.setName(HtmlUtils.htmlEscape(demand.getName()));
		demand.setContent(HtmlUtils.htmlEscape(demand.getContent()));
		demandService.saveAndFlush(demand);
		return result;
	}
											
}
