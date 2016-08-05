
package com.db.bms.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public final class XmlUtils {

	public static Map<String,String> readTemplateConfigFile(File file) {

		Map<String,String> map = new HashMap<String,String>();
		try {
			SAXReader sr = new SAXReader();
			Document doc = sr.read(file);
			Element root = doc.getRootElement();
			Iterator<Element> it = root.elementIterator();
			while (it.hasNext()) {
				Element element = it.next();
				String value = element.getText();
				map.put(element.getName(), value);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;


	}
}
