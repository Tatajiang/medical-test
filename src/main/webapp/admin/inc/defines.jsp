<%@page import="com.homolo.framework.web.JspHelper"%>
<%@ page language="java" pageEncoding="UTF-8" %>
<%
//路径
String path = request.getContextPath();
//网站全路径
String basePath = request.getScheme()+"://"+request.getServerName()+path;
if(request.getServerPort()!=80){
	basePath = request.getScheme()+"://"+request.getServerName()+":" + request.getServerPort() +path;
}

//禁止缓存
response.setHeader("Pragma","no-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires", 0);

String title = "体检中心管理系统";

JspHelper helper = new JspHelper(pageContext, "utf-8");
%>