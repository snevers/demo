package com.tgj;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.alibaba.fastjson.JSONObject;
import com.tgj.entity.Classify;
import com.tgj.entity.Demand;
import com.tgj.entity.Function;
import com.tgj.entity.Model;
import com.tgj.entity.Project;
import com.tgj.http.HttpTests;
import com.tgj.http.ParseHtml;
import com.tgj.service.ClassifyService;
import com.tgj.service.DemandService;
import com.tgj.service.FunctionService;
import com.tgj.service.ModelService;
import com.tgj.service.ProjectService;

/**
 * 
 * <p>
 *		暂时提交到git的测试注释掉.
 * </p>
 *	<pre>
 *		主要是因为现在测试与jenkins在同一台机子上，里面cache有占用问题
 *	</pre>
 * @className RykjApplicationTests
 * @author tgj  
 * @date 2018年5月29日 下午1:46:29 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class RykjApplicationTests {
	
	@Autowired
	private DemandService demandService;
	
	@Autowired
	private ClassifyService classifyService;
	
	@Autowired
	private FunctionService functionService;
	
	@Autowired
	private ModelService modelService;
	
	@Autowired
	private ProjectService projectService;

	@Test
	public void contextLoads() throws IOException {
//		//保存test
//		assertNotNull(saveTest().getId());
//		//查找test
//		assertNotNull(findPageTest());
		
//		saveToDatabase(); 
//		saveBlog();
		Runtime.getRuntime().exec("sed -i 's/192.168.1.20/127.0.0.1/g' `grep '192.168.1.20' -rl /opt/static/`");
	}
	
	private void saveBlog() throws ClientProtocolException, IOException {
		HttpTests httpTests = new HttpTests();
		HttpTests.init();
		Map<String, JSONObject> tempMap = httpTests.blogTest();
		JSONObject tempObj = null;
		for (Entry<String, JSONObject> en : tempMap.entrySet()) {
			Function function = new Function();
			function.setNo(en.getKey());
			function = functionService.findOne(Example.of(function));
			tempObj = en.getValue().getJSONObject("data");
			function.setFromPrice(tempObj.getFloat("fromPrice"));
			function.setToPrice(tempObj.getFloat("toPrice"));
			functionService.saveAndFlush(function);
		}
		HttpTests.destroy();
	}
	
	private Demand saveTest() {
		int i = 200;
		while (i > 0) {
			Demand demand = new Demand();
			String n = RandomStringUtils.randomAlphabetic(8);
			String c = RandomStringUtils.randomAlphabetic(20);
			demand.setContent(c);
			demand.setEmail(RandomStringUtils.randomAlphabetic(11) + "@163.com");
			demand.setName(n);
			demand.setPhoneNum(RandomStringUtils.randomNumeric(11));
			demand.setNameAndContent(c + n);
			demandService.saveAndFlush(demand);
			i--;
		}
		return null;
	}
	
	private Page<Demand> findPageTest() {
		String nameAndContent = "test";
		String email = "";
		String phoneNum = "";
		Date createDateStart = null;
		Date createDateEnd = null;
		Short status = 0;
		Pageable pageable = new PageRequest(0, 10, new Sort(Direction.DESC, "createDate"));
		return demandService.findPage(nameAndContent, email, phoneNum, createDateStart, createDateEnd, status, pageable);
	}
	
	@SuppressWarnings("unchecked")
	private void saveToDatabase() throws IOException {
		ParseHtml parseHtml = new ParseHtml();
		Object[] objs = parseHtml.parseName(new File(RykjApplicationTests.class.getResource("http/fuotedPrice.html").getPath()), null);
		Map<String, String> noToName = (Map<String, String>) objs[0];
		Map<Object, Map<Object, Map<Object, List<String>>>> map = ((Set<String>) objs[1]).stream().
				collect(Collectors.groupingBy(str -> str.split("\\.")[0], Collectors.groupingBy(str -> str.split("\\.")[1], Collectors.groupingBy(str -> str.split("\\.")[2]))));
		String[] tempNo = null;
		for (Entry<Object, Map<Object, Map<Object, List<String>>>> pro : map.entrySet()) {
			Project project = new Project(pro.getKey().toString(), noToName.get(pro.getKey()));
			System.out.println("projectNo = " + project);
			projectService.saveAndFlush(project);
			for (Entry<Object, Map<Object, List<String>>> cla : pro.getValue().entrySet()) {
				Classify classify = new Classify(noToName.get(cla.getKey()), cla.getKey().toString());
				classify.setProject(project);
				System.out.println("\tclassifyNo = " + classify);
				classifyService.saveAndFlush(classify);
				for (Entry<Object, List<String>> mod : cla.getValue().entrySet()) {
					Model model = new Model(mod.getKey().toString(), noToName.get(mod.getKey()));
					model.setClassify(classify);
					System.out.println("\t\tmodelNo = " + model);
					modelService.saveAndFlush(model);
					for (String fun : mod.getValue()) {
						tempNo = fun.split("\\.");
						Function function = new Function(tempNo[3], noToName.get(tempNo[3]));
						function.setModel(model);
						System.out.println("\t\t\tfunctionNo = " + tempNo[3] + "------>" + function);
						functionService.saveAndFlush(function);
					}
				}
			}
		}
	}

}
