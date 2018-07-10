package com.tgj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * 
 * <p>
 *		启动类.
 * </p>
 * <p>
 *		程序启动入口
 * </p>
 * @className RykjApplication
 * @author Server  
 * @date 2018年5月16日 下午2:36:51 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
@SpringBootApplication
@MapperScan(basePackages = "com.rykj.mapper")
public class RykjApplication {

	public static void main(String[] args) {
		SpringApplication.run(RykjApplication.class, args);
	}
}
