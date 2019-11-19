/**
 * Project : police-affairs-webapp
 * FileName: UserLogoutFilter.java
 * Copyright (c) 2018, www.narwell.com All Rights Reserved.
 */
package com.jiang.medical.filter;

import com.jiang.medical.util.SessionUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/** 
* @ClassName: UserLogoutFilter 
* @Description: 用户注销
* @author Tata 
* @date 2019年5月4日 下午12:56:20 
*  
*/ 
public class UserLogoutFilter implements Filter {


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
	}


	@Override
	public void doFilter(ServletRequest servletRrequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRrequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		request.getSession().setAttribute("_user", SessionUtil.getCurrentUser(request));
		request.getSession().removeAttribute("_user");
		response.sendRedirect(request.getContextPath() + "/admin/index.jsp");
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
