<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="./inc/defines.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title><%=title %></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	<script src="<%=path %>/js/bootstrap/js/bootstrap.js"></script>
	<script src="<%=path %>/js/jquery-3.3.1.min.js"></script>
	<jsp:include page="./inc/resource.jsp" />

	<style type="text/css">
		.border{
			border-bottom:5px solid #E4E4E4;
			border-right:5px solid #E4E4E4;
			cursor: pointer;
		}
		ul.charts{list-style-type: none;}
		ul.charts li{float: left;margin-left: 10px;width: 48%;}
	</style>

	<script type="text/javascript">
		$(function(){
			//移入移出
			$(".tile-stats").each(function(){
				$(this).mouseover(function(){
					$(this).addClass("border");
				}).mouseout(function (){
					$(this).removeClass("border");
				});
			});
			$('#tabs-content').css({
				minHeight:$(window).height() - 150
			});
		})
	</script>
</head>
<body class="nav-md">
    <div class="container body">
      	<div class="main_container">
	        <div class="col-md-3 left_col">
	          	<div class="left_col scroll-view">
		          	<!-- LOGO -->
		            <div class="navbar nav_title" style="border: 0;">
		              	<a href="<%=path %>/admin/index.jsp" class="site_title"><span style="padding-left: 20px;"><%=title %></span></a>
		            </div>
		            <div class="clearfix"></div>

		            <!-- 左部菜单 -->
		            <jsp:include page="./inc/leftmenu.jsp" />
		            <!-- 底部按钮 -->
	          	</div>
	        </div>

	        <!-- 顶部导航 -->
			<jsp:include page="./inc/top.jsp" />

	        <!-- 右侧内容 -->
	        <div class="right_col" role="main">
	        	<ul class="nav nav-tabs" role="tablist"  id="tabs-nav">
				  	<li role="presentation" class="active" id="tab_home">
				  		<a href="#home" aria-controls="home" role="tab" data-toggle="tab"><i class="fa fa-home"></i>首页</a>
				  	</li>
				</ul>
				<div class="tab-content x_panel" id="tabs-content">
					<div role="tabpanel" class="tab-pane active" id="home" >

					</div>
				</div>
	        </div>
      	</div>
    </div>

    <!-- 底部导航 -->
	<jsp:include page="./inc/bottom.jsp" />
</body>
</html>