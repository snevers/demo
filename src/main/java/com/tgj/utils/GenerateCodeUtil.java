package com.tgj.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class GenerateCodeUtil {
	
	private static final String BASE_DAO = "com.rykj.base.BaseDao";
	private static final String BASE_SERVICE= "com.rykj.base.BaseJpaService";
	private static final String BASE_CONTROLLER = "com.rykj.base.BaseController";
	private static final String TEMPLATE_PATH = "/templates/generate";
	private static final String PROJECT_PATH;
	
	private static final Configuration CONFIGURATION = new Configuration(freemarker.template.Configuration.VERSION_2_3_26);
	
	private static final Map<String, String> PARAS = new HashMap<>();
	
	static {
		String classes = GenerateCodeUtil.class.getResource("/").getPath();
		PROJECT_PATH = classes.substring(0, classes.length() - "/target/classes".length()) + "src/main/java/";
	}
	
	public static void main(String[] args) throws IOException, TemplateException {
		GenerateCodeUtil gcu = new GenerateCodeUtil();
		String entityPackage = "com/rykj/entity";
		List<String> entityNames = gcu.dealEntity(entityPackage);
		for (String entity : entityNames) {
			gcu.initParas();
			entity = entity.replace(".java", "");
			PARAS.put("entity", entity);
			PARAS.put("entityPackage", entityPackage.replace("/", "."));
			
			String daoPackage = gcu.deal("com/rykj/dao", entity, "Dao.java", "daoTemplate.ftl", false);
			PARAS.put("daoPackage", daoPackage);
			PARAS.put("dao", daoPackage.substring(daoPackage.lastIndexOf(".") + 1));
			
			String servicePackage = gcu.deal("com/rykj/service", entity, "Service.java", "serviceTemplate.ftl", false);
			PARAS.put("servicePackage", servicePackage);
			PARAS.put("service", servicePackage.substring(servicePackage.lastIndexOf(".") + 1));
			
			PARAS.put("pathPrefix", (entity.startsWith("Sys") ? "admin/" : "") + "api");
			gcu.deal(entity.startsWith("Sys") ? "com/rykj/api/admin/controller" : "com/rykj/api/controller", entity, "Controller.java", "controllerTemplate.ftl", false);
		}
	}
	
	
	private void initParas() {
		PARAS.clear();
		PARAS.put("BASE_DAO", BASE_DAO);
		PARAS.put("BASE_SERVICE", BASE_SERVICE);
		PARAS.put("BASE_CONTROLLER", BASE_CONTROLLER);
	}
	
	public String deal(String packagePath, String entity, String suffix, String templateName, boolean override) throws IOException, TemplateException {
		String basePath = (packagePath.endsWith("/") ? packagePath : packagePath + "/") + entity + suffix;
		PARAS.put("package", packagePath.replace("/", "."));
		File toPath = new File(PROJECT_PATH, basePath);
		if (!override && toPath.exists()) {
			return basePath.replace("/", ".").replace(".java", "");
		}
		dealTemplate(templateName, toPath);
		return basePath.replace("/", ".").replace(".java", "");
	}
	
	public List<String> dealEntity(String entityPacke) {
		File file = new File(PROJECT_PATH, entityPacke);
		List<String> lists = new ArrayList<>();
		for (File tfile : file.listFiles()) {
			lists.add(tfile.getName());
		}
		return lists;
	}
	
	public void dealTemplate(String templateName, File toPath) throws IOException, TemplateException {
		CONFIGURATION.setClassForTemplateLoading(GenerateCodeUtil.class, TEMPLATE_PATH);
		try (FileWriter fw = new FileWriter(toPath)) {
			CONFIGURATION.getTemplate(templateName).process(PARAS, fw);
		}
	}
}
