package com.tgj.api.admin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationHome;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Files;
import com.tgj.base.BaseController;
import com.tgj.model.BasicResultModel;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

/**
 * 
 * <p>
 *		静太化，及部署功能.
 * </p>
 *	<pre>
 *			解析标记，部署静态文件到指定路径
 *	</pre>
 * @className AdminParseController
 * @author tgj  
 * @date 2018年6月8日 下午2:08:06 
 *    
 * @copyright 2018 www.agen.com Inc. All rights reserved.
 */
@RestController
@RequestMapping(value = "admin/api/parse/", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "后台网页编译信息", produces = "application/json", consumes = "application/json", authorizations = {@Authorization(value = "basicAuth")})
public class AdminParseController extends BaseController {
	
	private static final AtomicBoolean ISDOING = new AtomicBoolean(false);
	
	private static String basePath; 
	
	@Value("${java.io.tmpdir}")
	private String tmp;
	
	@Value("${sys.staticFileHome}")
	private String staticFileHome;
	
	private static final Configuration CONFIGURATION = new Configuration(freemarker.template.Configuration.VERSION_2_3_26);
	
	@PostConstruct
	public void init() throws IOException {
		ApplicationHome home = new ApplicationHome(AdminParseController.class);
	    File file = home.getSource();
	    String tempPath = file.getParentFile().getAbsolutePath();
	    tempPath += (tempPath.endsWith("/") ? "" : "/") + "resources/";
	    basePath = tempPath + "WEB-INF/classes/static/";
	    if (file.isDirectory()) {
	    	basePath = AdminParseController.class.getResource("/static/").getPath();
	    	return;
	    }
	    ZipInputStream zin = new ZipInputStream(new FileInputStream(file));
        try {
            ZipEntry ze = null;
            String name = null, path = null;
            while ((ze = zin.getNextEntry()) != null) {
            	name = ze.getName();
            	if (!name.contains("WEB-INF/classes/static/")) continue;
                path = tempPath + name;

                if (ze.isDirectory()) {
                    File unzipFile = new File(path);
                    if(!unzipFile.isDirectory()) {
                        unzipFile.mkdirs();
                    }
                } else {
                    FileOutputStream fout = new FileOutputStream(path, false);
                    try {
                        for (int c = zin.read(); c != -1; c = zin.read()) {
                            fout.write(c);
                        }
                        zin.closeEntry();
                    } finally {
                        fout.close();
                    }
                }
            }
        } finally {
            zin.close();
        }
	}
	
	@RequestMapping(value = "parseHtmls", method = RequestMethod.POST)
	public BasicResultModel parseHtmls(String[] paths) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException, InterruptedException {
		Assert.notNull(paths, "paths is null");
		BasicResultModel basicResultModel = null;
		for (String path : paths) {
			while (true) {
				basicResultModel = parseHtml(path);
				if ("200".equals(basicResultModel.getStatus())) {
					if (StringUtils.isNotBlank(staticFileHome)) {
						
					}
					break;
				}
				Thread.sleep(20);
			}
		}
		copyContent(new File(basePath), staticFileHome, basePath);
		return basicResultModel;
	}
	
	@ApiOperation(value = "按目录解析页面")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "路径(格式：/client)", name = "path", paramType = "query", required = true)
	})
	@RequestMapping(value = "parseHtml", method = RequestMethod.POST)
	public BasicResultModel parseHtml(String path) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		synchronized (AdminDemandController.class) {
			if (ISDOING.get()) {
				BasicResultModel basicResultModel = getBasicResultModel();
				basicResultModel.setStatus("1002");
				basicResultModel.setMessage("正在处理...");
				return basicResultModel;
			} else {
				ISDOING.set(true);
			}
		}
		try {
			Assert.notNull(path, "path is null");
			File basicPath = new File(basePath, path);
			Map<String, String> paras = new HashMap<>();
			paras.put("date", System.currentTimeMillis() + "");
			Map<File, File> records = new HashMap<>();
			dealTeamplate(basicPath, paras, records, "/static" + basicPath.getParentFile().getAbsolutePath().substring(basePath.length() - 1));
			File dest = null;
			for (Entry<File, File> en : records.entrySet()) {
				while (true) {
					try {
						Files.copy(en.getKey(), en.getValue());
						if (StringUtils.isNotBlank(staticFileHome)) {
							dest = new File(staticFileHome, en.getValue().getAbsolutePath().substring(basePath.length()));
							if (!dest.exists()) dest.getParentFile().mkdirs();
							Files.copy(en.getKey(), dest);
						}
						break;
					} catch (Exception e) {}
				}
				en.getKey().delete();
			}
		} finally {
			ISDOING.set(false);
		}
		return getBasicResultModel();
	}
	
	@ApiOperation(value = "初始化静态文件")
	@RequestMapping(value = "initStaticFile", method = RequestMethod.POST)
	public BasicResultModel initStaticFile() throws IOException, TemplateException {
		synchronized (AdminDemandController.class) {
			if (ISDOING.get()) {
				BasicResultModel basicResultModel = getBasicResultModel();
				basicResultModel.setStatus("1002");
				basicResultModel.setMessage("正在处理...");
				return basicResultModel;
			} else {
				if (StringUtils.isNotBlank(staticFileHome)) {
					parseHtml("/client");
					parseHtml("/admin");
					copyContent(new File(basePath), staticFileHome, basePath);
				}
			}
		}
		return getBasicResultModel();
	}
	
	private void copyContent(File from, String staticFileHome, String basicPath) throws IOException {
		if (from.isDirectory()) {
			for (File file : from.listFiles()) {
				copyContent(file, staticFileHome, basicPath);
			}
		} else {
			File to = new File(staticFileHome, from.getAbsolutePath().substring(basicPath.length()));
			if (!to.exists()) to.getParentFile().mkdirs();
			Files.copy(from, to);
		}
	}
	
	private void dealTeamplate(File basicPath, Map<String, String> paras, Map<File, File> records, String tempPath) throws IOException, TemplateException {
		if (basicPath.isDirectory()) {
			for (File file : basicPath.listFiles()) {
				dealTeamplate(file, paras, records, tempPath + (tempPath.endsWith("/") ? "" : "/") + file.getParentFile().getName());
			}
		} else {
			if (!basicPath.getName().endsWith(".html")) return;
			CONFIGURATION.setClassForTemplateLoading(AdminParseController.class, tempPath);
			File outFile = new File(tmp, basicPath.getName());
			records.put(outFile, basicPath);
			try (FileWriter fw = new FileWriter(outFile)) {
				CONFIGURATION.getTemplate(basicPath.getName()).process(paras, fw);
			}
		}
	}
	
	@ApiOperation(value = "获得所有页面")
	@RequestMapping(value = "paths", method = RequestMethod.GET)
	public JSONObject paths() {
		JSONArray clientJsonObject = new JSONArray();
		parsePath(new File(basePath, "client/"), clientJsonObject, "/");
		JSONArray adminJsonObject = new JSONArray();
		parsePath(new File(basePath, "admin/"), adminJsonObject, "/");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("client", clientJsonObject);
		jsonObject.put("admin", adminJsonObject);
		return jsonObject;
	}
	
	private void parsePath(File basicPath, JSONArray result, String tempPath) {
		if (basicPath.isFile() && !basicPath.getName().endsWith(".html"));
		tempPath = tempPath + ("/".equals(tempPath) ? "" : "/") + basicPath.getName();
		JSONObject tempObject = new JSONObject();
		tempObject.put("path", tempPath);
		tempObject.put("text", basicPath.getName());
		result.add(tempObject);
		if (basicPath.isDirectory()) {
			JSONArray tempArray = new JSONArray();
			tempObject.put("children", tempArray);
			for (File file : basicPath.listFiles()) {
				parsePath(file, tempArray, tempPath);
			}
		}
	}
}
