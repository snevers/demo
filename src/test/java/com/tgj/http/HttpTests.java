package com.tgj.http;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.http.client.ClientProtocolException;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.alibaba.fastjson.JSONObject;

public class HttpTests {

	private static HttpConnectManager httpConnectManager = new HttpConnectManager();
	
	@BeforeClass
	public static void init() throws ClientProtocolException, IOException {
		httpConnectManager.doGet("https://codemart.com");
		Map<String, String> paras = new HashMap<>();
		paras.put("account", "bluesky1");
		paras.put("password", "5766d06afff43b7b03070596455828d4463dcc10");
		paras.put("remember", "true");
		httpConnectManager.doPost("https://codemart.com/api/login", paras);
	}
	
	public Map<String, JSONObject> blogTest() throws ClientProtocolException, IOException {
//		Map<String, String> paras = new HashMap<>();
//		paras.put("codes", "P001>C001>M004>1010405,P001>C001>M004>1010405,P001>C001>M004>1010405,P001>C001>M004>1010405,P001>C001>M004>1010405,P001>C001>M004>1010405,P001>C001>M004>1010405,P001>C001>M004>1010405,P001>C001>M004>1010405,P001>C001>M004>1010405");
//		paras.put("webPageCount", "1");
		Map<String, JSONObject> tempMap = step1(true);
		return tempMap;
	}
	
	@AfterClass
	public static void destroy() {
		httpConnectManager.closeHttpClient();
	}

	@SuppressWarnings("unchecked")
	private TreeMap<String, JSONObject> step1(boolean single) throws IOException {
		ParseHtml parseHtml = new ParseHtml();
		Object[] objs = parseHtml.parseName(new File(HttpTests.class.getResource("fuotedPrice.html").getPath()), null);
//		Map<String, String> noToName = (Map<String, String>) objs[0];
		Map<Object, List<String>> map = ((Set<String>) objs[1]).stream().
				collect(Collectors.groupingBy(str -> str.split("\\.")[0]));
		TreeMap<String, JSONObject> tempMap = new TreeMap<>();
		Set<String> exceptions = new HashSet<>();
		for (Entry<Object, List<String>> pro : map.entrySet()) {
			Map<String, JSONObject> result = single ? step2Single(pro.getValue(), exceptions) : step2(pro.getValue(), exceptions);
			tempMap.putAll(result);
		}
		for (String tempParas : exceptions) {
			tempMap.putAll(changeClaculatePriceAndTerm(tempMap, tempParas, exceptions));
		}
		return tempMap;
	}
	
	private Set<JSONObject> sets = new HashSet<>();
	
	private TreeMap<String, JSONObject> step2Single(List<String> lists, Set<String> exceptions) throws ClientProtocolException, IOException {
		TreeMap<String, JSONObject> result = new TreeMap<>();
		List<String> tempLists = new ArrayList<>(lists);
		JSONObject tempTotal = null, cal = null;
		String second = null, key = null;
		while (!tempLists.isEmpty()) {
			second = tempLists.remove(0).replace(".", ">");
			key = second.split(">")[3];
			tempTotal = calculatePrice(key);
			if (Objects.isNull(tempTotal.getJSONObject("data"))) {
				exceptions.add(second);
				continue;
			}
			tempTotal.put("orig", second);
			result.put(key, tempTotal);
			cal = new JSONObject(tempTotal);
			cal.put("real", true);
			sets.add(cal);
		}
		return result;
	}
	
	private TreeMap<String, JSONObject> step2(List<String> lists, Set<String> exceptions) throws ClientProtocolException, IOException {
		TreeMap<String, JSONObject> result = new TreeMap<>();
		List<String> tempLists = new ArrayList<>(lists);
		StringBuilder strParas = new StringBuilder();
		for (String str : tempLists) {
			strParas.append(str.replace(".", ">"));
			strParas.append(",");
		}
		String tempParas = strParas.substring(0, strParas.length() - 1);
		JSONObject total = calculatePrice(tempParas);
		JSONObject tempTotal = null, tempJSON = null;
		
		String second = null, back = null, key = null;
		while (tempLists.size() > 2) {
			second = tempLists.remove(0).replace(".", ">");
			back = second;
			key = second.split(">")[3];
			if (!tempParas.endsWith(second)) {
				second += ",";
			}
			tempParas = tempParas.replace(second, "");
			tempTotal = calculatePrice(tempParas);
			tempJSON = claculatePriceAndTerm(total, tempTotal);
			if (Objects.isNull(tempJSON)) {
				exceptions.add(tempParas);
				exceptions.add(back);
				continue;
			}
			tempJSON.put("orig", back);
			result.put(key, tempJSON);
			sets.add(tempTotal);
			total = tempTotal;
		}
		result.putAll(changeClaculatePriceAndTerm(result, tempParas, exceptions));
		return result;
	}
	
	private TreeMap<String, JSONObject> changeClaculatePriceAndTerm(TreeMap<String, JSONObject> result, String tempParas, Set<String> exceptions) {
		TreeMap<String, JSONObject> tempResult = new TreeMap<>();
		String para = "", key = "";
		String[] paras = tempParas.split(",");
		int len = 0, basicLen = 1;
		for (int i = 0; i < paras.length; i++) {
			para = paras[i].replace(".", ">");
			key = para.split(">")[3];
			len = basicLen;
			while(!result.isEmpty()) {
				Map<String, JSONObject> tempMap = new HashMap<>();
				while (len >= 0 && !result.isEmpty()) {
					Entry<String, JSONObject> en = result.pollFirstEntry();
					tempMap.put(en.getKey(), en.getValue());
					para += "," + en.getValue().getString("orig");
					len--;
				}
				try {
					JSONObject tempTotal = calculatePrice(para);
					sets.add(tempTotal);
					tempResult.putAll(tempMap);
					for (Entry<String, JSONObject> en : tempMap.entrySet()) {
						tempTotal = claculatePriceAndTerm(tempTotal, en.getValue());
					}
					tempTotal.put("orig", paras[i].replace(".", ">"));
					tempResult.put(key, tempTotal);
					break;
				} catch (Exception e) {
					para = paras[i].replace(".", ">");
					if (Objects.nonNull(exceptions)) {
						exceptions.add(para);
						break;
					}
					basicLen++;
					len = basicLen;
				}
			}
		}
		return tempResult;
	}
	
	private JSONObject claculatePriceAndTerm(JSONObject tempTotal, JSONObject temp) {
		try {
			tempTotal.getJSONObject("data").put("fromPrice", tempTotal.getJSONObject("data").getFloat("fromPrice") - temp.getJSONObject("data").getFloat("fromPrice"));
			tempTotal.getJSONObject("data").put("toPrice", tempTotal.getJSONObject("data").getFloat("toPrice") - temp.getJSONObject("data").getFloat("toPrice"));
			tempTotal.getJSONObject("data").put("fromTerm", tempTotal.getJSONObject("data").getFloat("fromTerm") - temp.getJSONObject("data").getFloat("fromTerm"));
			tempTotal.getJSONObject("data").put("toTerm", tempTotal.getJSONObject("data").getFloat("toTerm") - temp.getJSONObject("data").getFloat("toTerm"));
		} catch (Exception e) {
			return null;
		}
		return tempTotal;
	}
	
	private JSONObject calculatePrice(String strParas) throws ClientProtocolException, IOException {
		Map<String, String> paras = new HashMap<>();
		paras.put("codes", strParas);
		paras.put("webPageCount", "1");
		String result = httpConnectManager.doPost("https://codemart.com/api/quote/pre-save", paras);
		return JSONObject.parseObject(result);
	}
}
