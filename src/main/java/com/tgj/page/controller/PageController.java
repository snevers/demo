package com.tgj.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 
 * <p>
 *		页面跳转控制器.
 * </p>
 * <p>
 *		为了方便日志记录，建议页面都走此类
 * </p>
 * @className PageController
 * @author Server  
 * @date 2018年5月16日 下午4:34:10 
 *    
 * @copyright 2018 www.rykj.com Inc. All rights reserved.
 */
@Controller
public class PageController {
	
	@RequestMapping("/")
	public String index(Model model) {
		return "redirect:/client/index.html";
	}
	
//	@RequestMapping(value = "/{model}/{page}.html")
//	public String pageForward(@PathVariable() String model, @PathVariable() String page) {
//		return model + "/" + page;
//	}
//	
//	@RequestMapping(value = "/{page}.html")
//	public String pageForward(@PathVariable() String page) {
//		return page;
//	}
}
