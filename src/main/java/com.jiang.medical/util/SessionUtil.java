package com.jiang.medical.util;

import com.jiang.medical.platform.system.domain.User;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @author IvanHsu
 * @E-Mail xupai_911@163.com 2014-1-20下午1:51:54
 */
public class SessionUtil
{

	private static HashMap<String, HttpSession> sessionMap = new HashMap<String,HttpSession>();
	
	
	public static void addSallerSession(String sallerId,HttpSession session){
		sessionMap.put(sallerId, session);
	}
	
	public static HttpSession getSallerSession(String sallerId){
		return sessionMap.get(sallerId);
	}
	
	/**
	 * 获取当前登录的 平台用户对象 
	 * @param request
	 * @return
	 */
	public static User getCurrentUser( HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("_user");
		return user;
	}
	
	/**
	 * 获取客户端ip
	 * @param request
	 * @return
	 */
	public static String getRemoteIP(HttpServletRequest request) {
		String addr = request.getHeader("x-forwarded-for");
		if (StringUtils.isBlank(addr) || "unknown".equalsIgnoreCase(addr)) {
			addr = request.getHeader("proxy-client-ip");
		}
		if (StringUtils.isBlank(addr) || "unknown".equalsIgnoreCase(addr)) {
			addr = request.getHeader("wl-proxy-client-ip");
		}
		if (!StringUtils.isBlank(addr)) {
			addr = addr.replaceAll("unknown,?", "").trim().replaceAll(",.*", "");
		} else {
			addr = request.getRemoteAddr();
		}
		return addr;
	}
}
