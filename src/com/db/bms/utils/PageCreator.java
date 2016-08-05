package com.db.bms.utils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class PageCreator {

	private static PageCreator instance;
	private static Configuration cfg;
	private static String projectPath;

	public static PageCreator getInstance() {
		if (instance == null) {
			instance = new PageCreator();
			cfg = new Configuration();	
		}
		return instance;
	}

	public void writePage(Map<String, Object> rootMap, String templateName,
			String pagePath,String templatePath) throws Exception {
		// 取得模版文件
		Writer outfile = null;
		try {
			
			File[] fileList = FileUtils.getFileList(templatePath, ".htm");
			cfg.setDirectoryForTemplateLoading(new File(templatePath));
			Template template = cfg.getTemplate(fileList[0].getName(), "UTF-8");
			template.setOutputEncoding("UTF-8");
			if (pagePath.indexOf(System.getProperty("file.separator")) > 0) {
				String path = pagePath.substring(0, pagePath.lastIndexOf("/"));
				File file = new File(path);
				if (file == null || !file.exists()) {
					file.mkdirs();
				}
			}
			outfile = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(pagePath), "UTF-8"));
			// 合并数据模型和模版，并将结果输出到out中
			//rootMap.put("sysCode", ConstConfig.sysCode);
			template.process(rootMap, outfile);// 用模板来开发servlet可以只在代码里面加入动态的数据
			outfile.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("处理Template模版中出现错误", e);
		} finally {
			if (outfile != null) {
				try {
					outfile.close();
				} catch (IOException e) {
				}
			}
		}
	}
	public static void main(String []args){
		
		System.out.println(PageCreator.getInstance().projectPath);
	}
}

