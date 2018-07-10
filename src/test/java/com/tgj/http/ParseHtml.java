package com.tgj.http;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseHtml {
	
	public Object[] parseName(File file, Function<Element, String> function) throws IOException {
		Map<String, String> noToName = new HashMap<>();
		Document doc = Jsoup.parse(file, "utf-8");
		Elements elements = doc.select("body").get(0).children();
		Elements tempElementsTr = null, tempElementsInput = null, tempElementsTd = null;
		String[] tempNo = null;
		Set<String> sets = new HashSet<>();
		for (Element element : elements) {
			if (element.is("h1")) {
				noToName.put(element.attr("for"), element.text());
			} else if (element.is("div.functions-3wwxx")) {
				tempElementsTr = element.select("tbody>tr");
				for (Element tempElementTr : tempElementsTr) {
					if (!(tempElementsInput = tempElementTr.select("input[type='checkbox'],input[type='radio']")).isEmpty()) {
						tempNo = tempElementsInput.last().attr("name").split("\\.");
						if (tempNo.length >= 4) {
							if ((tempElementsTd = tempElementTr.children()).size() >= 3 || tempElementsTd.get(0).hasAttr("rowspan")) {
								noToName.put(tempNo[1], tempElementsTd.get(0).text());
							}
							noToName.put(tempNo[2], tempElementsTd.size() >= 3 ? tempElementsTd.get(1).text() : tempElementsTd.get(0).text());
							for (Element tempElementInput : tempElementsInput) {
								tempNo = tempElementInput.attr("name").split("\\.");
								if (tempNo.length >= 4) {
									noToName.put(tempNo[3], tempElementInput.parent().text());
									sets.add(tempElementInput.attr("name") + (null != function ? function.apply(tempElementInput) : ""));
								}
							}
						}
					}
				}
			}
		}
		return new Object[] {noToName, sets};
	}
	
	public static void main(String[] args) throws IOException {
		ParseHtml parseHtml = new ParseHtml();
		parseHtml.parseName(new File("f:/quotedPrice.html"), null);
	}
	
}
