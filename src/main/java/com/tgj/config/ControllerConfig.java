package com.tgj.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

/**
 * 
 * <p>
 *		添加controller增加，支持md5验证缓存.
 * </p>
 * <p>
 *	支持md5方式缓存失败（配置里面得加上
 *	spring.resources.chain.strategy.content.enabled=true
 *  spring.resources.chain.strategy.content.paths=/**）
 * </p>
 * @className ControllerConfig
 * @author Server  
 * @date 2018年5月21日 上午9:45:24 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
@ControllerAdvice
public class ControllerConfig {

    @Autowired
    private ResourceUrlProvider resourceUrlProvider;

    @ModelAttribute("urls")
    public ResourceUrlProvider urls() {
        return this.resourceUrlProvider;
    }

}
