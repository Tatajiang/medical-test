/**
 * Project : police-affairs-webapp
 * FileName: UserLoginFilter.java
 * Copyright (c) 2018, www.narwell.com All Rights Reserved.
 */
package com.jiang.medical.filter;

import com.jiang.medical.platform.system.domain.User;
import com.jiang.medical.platform.system.manager.OperationManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/** 
* @ClassName: UserLoginFilter 
* @Description: 登录拦截
* @author Tata 
* @date 2019年5月4日 下午12:55:56 
*/
public class UserLoginFilter implements Filter {

	private List<String> otherFile;
	
	private OperationManager operationManager;
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		otherFile = new ArrayList<>();
		otherFile.add("/admin/inc/.*");
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
		operationManager = context.getBean(OperationManager.class);
	}


	@Override
	public void doFilter(ServletRequest servletRrequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRrequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		HttpSession session = request.getSession();
		String uri = request.getRequestURI();
		User user = (User) session.getAttribute("_user");
		
		if(user != null){
			boolean power = operationManager.notHasUrlOperation(user, uri);
			if(power){
				chain.doFilter(request,response);
				return;
			}else{
				response.sendRedirect(request.getContextPath() + "/admin/index.jsp");
				return;
			}
		}else{
			String pageUrl = StringUtils.substringAfterLast(uri, "/admin");
			for(String str : otherFile){
				if(pageUrl.matches(str)) {
					chain.doFilter(request, response);
					return;
				}
			}
			if(StringUtils.isNotBlank(uri) && !uri.endsWith("login")){
				session.setAttribute("user_service", uri);
			}
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
	}


	@Override
	public void destroy() {

	}

}
