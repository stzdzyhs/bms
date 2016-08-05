package com.db.bms.utils.spring.interceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.db.bms.utils.spring.SessionUtil;

public class SecurityInterceptor implements HandlerInterceptor {

	private static final Logger logger = Logger
			.getLogger(SecurityInterceptor.class);

	private List<String> excludeUrls;

	private List<String> authCheckUrls;

	private List<String> authCheckAsynUrls;

	public List<String> getAuthCheckUrls() {
		return authCheckUrls;
	}

	public void setAuthCheckUrls(List<String> authCheckUrls) {
		this.authCheckUrls = authCheckUrls;
	}

	public List<String> getAuthCheckAsynUrls() {
		return authCheckAsynUrls;
	}

	public void setAuthCheckAsynUrls(List<String> authCheckAsynUrls) {
		this.authCheckAsynUrls = authCheckAsynUrls;
	}

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

	/**
	 * 完成页面的render后调用
	 */
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object object, Exception exception)
			throws Exception {
	}

	/**
	 * 在调用controller具体方法后拦截
	 */
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object,
			ModelAndView modelAndView) throws Exception {
	}

	/**
	 * 在调用controller具体方法前拦截
	 */
	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws Exception {
		// String requestUri = request.getRequestURI();
		// String contextPath = request.getContextPath();
		// String url = requestUri.substring(contextPath.length());
		String url = request.getServletPath();// RequestURI=ContextPath+ServletPath
		logger.debug(url);
		request.setAttribute("path", request.getContextPath());
		if (excludeUrls.contains(url)) {
			return true;
		} 
		else {
			/*
			if (authCheckUrls.contains(url)) {
				if (PjAuthorizeUtil.isOverdue()) {
					response.sendRedirect(request.getContextPath()
							+ "/unauthorized.action");
					return false;
				}
			}else if (authCheckAsynUrls.contains(url)){
				if (PjAuthorizeUtil.isOverdue()) {
					response.sendRedirect(request.getContextPath()
							+ "/unauthorizedAsyn.action");
					return false;
				}
			}*/
			// SessionInfo sessionInfo = (SessionInfo)
			// request.getSession().getAttribute(ResourceUtil.getSessionInfoName());
			// String isAdmin = (String) SessionUtil.getAttr(request,
			// "isAdmin");
			if (null != SessionUtil.getAttr(request, "activeOperator")) {// 超管不需要验证权限
				if (SessionUtil.getAttr(request, "moduleMap") != null) {
					Map<String, List<String>> moduleMap = (Map<String, List<String>>) SessionUtil
							.getAttr(request, "moduleMap");
					if (moduleMap != null) {
						if (moduleMap.get(url) != null) {
							request.setAttribute("functionList", moduleMap.get(url));
						}
					}
				}
				return true;
			}
			else {
				List<String> urls = (List<String>) SessionUtil.getAttr(request,
						"authUrls");
				if (null != urls && urls.contains(url)) {
					return true;
				} 
				else {
					/*
					 * //upload.html;JSESSIONID:5CB625DAC2456D7E00D8C74D61444544
					 * 形式登录 if (request.getCookies() != null) for (Cookie cookie
					 * : request.getCookies()) { String JSESSIONID =
					 * cookie.getName(); if
					 * ("JSESSIONID".equalsIgnoreCase(JSESSIONID)) { if
					 * (request.
					 * getSession().getId().equalsIgnoreCase(cookie.getValue()))
					 * { return true; } } }
					 */
					System.out.println("[" + url + "]-无访问权限->请联系管理员或登录!");
					request.setAttribute("url", url);
					response.sendRedirect(request.getContextPath());
					return false;
				}
			}
		}

	}
}
