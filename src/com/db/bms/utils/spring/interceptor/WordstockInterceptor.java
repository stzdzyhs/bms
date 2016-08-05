package com.db.bms.utils.spring.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.db.bms.service.impl.WordstockServiceImpl;
import com.db.bms.utils.Result2;
import com.db.bms.utils.ResultCode;

public class WordstockInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
	}

	public static void writeResponse(HttpServletResponse response, String responseText) throws IOException {
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Type", "text/json");
			out = response.getWriter();
			out.print(responseText);
			out.flush();
		}
		catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} 
		finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * 在执行controller方法之前执行，返回ture则继续往下执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
				HttpServletResponse response,Object obj) throws Exception {
		Result2<Object> result = new Result2<Object>();
		boolean flag = true;
		Integer totalCount = 0; 
		StringBuffer url = request.getRequestURL();
		String str = null;
		//需要进行生僻字检测的操作
		String urll = url.substring(url.lastIndexOf("/"),url.lastIndexOf("."));
		//更新、保存
		if(urll.contains("save") || urll.contains("update") || urll.contains("add")
			||urll.contains("Save") || urll.contains("Update") || urll.contains("Add")) {
			//加载生僻字库
			List<String> wordstocks = WordstockServiceImpl.wordstocks;
			//获取所有请求参数
			Map<String, String[]> params= request.getParameterMap();
			String wordNo = null;
			//解析参数、校验
			for (String key : params.keySet()) {  				
	            String[] values = params.get(key);
	            if("wordNo".equalsIgnoreCase(key)){
	            	wordNo = values[0];
	            }
	            for (int i = 0; i < values.length; i++) {  
	                String value = values[i];
	                for(String s : wordstocks) { //校验生僻字
	                	if(!value.contains(s)) {
		                	continue;
		                }
	                	totalCount++;
	                }
	            }  
	        }  
			if(totalCount > 0 && (wordNo == null || wordNo.isEmpty())) {
				flag = false;
			}
			str = "包含"+totalCount+"个生僻字--->请修正";
		}

		result.setResult(ResultCode.WORD_ERROR);
		result.setDesc(str);
		result.setData(totalCount);
		if(!flag) {
			System.out.println(str);
			//request.setAttribute("result", result);
			//response.sendRedirect(request.getContextPath()+"/containWordstock.action");
			str = result.toString();
			writeResponse(response, str);
		}
		return flag;
	}
}
