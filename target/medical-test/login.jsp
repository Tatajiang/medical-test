<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="./admin/inc/defines.jsp" %>
<html>
<head>
	<base href="<%=basePath%>">
	<title><%=title %></title>
	<meta http-equiv=Content-Type content="text/html; charset=utf-8">
	<link href="favicon.ico" rel="SHORTCUT ICON" />
	<link type="text/css" rel="stylesheet" href="<%=path %>/js/bootstrap/css/bootstrap.css">
	<link type="text/css" rel="stylesheet" href="<%=path %>/js/bootstrap-ladda/ladda-themeless.css">
	<link type="text/css" rel="stylesheet" href="<%=path %>/res/css/login.css">
	<script src="<%=path %>/js/jquery-3.3.1.min.js"></script>
	<script src="<%=path %>/js/artDialog/jquery.artDialog.js?skin=default"></script>
	<script src="<%=path %>/js/bootstrap-ladda/spin.js"></script>
	<script src="<%=path %>/js/bootstrap-ladda/ladda.js"></script>
	<script type="text/javascript">
	var $ctx = '<%=path %>';
	</script>
	<script type="text/javascript" src="<%=path %>/js/md5.js"></script>
	<script src="<%=path %>/res/js/login.js"></script>
</head>

<body>
	<!-- 结束 -->
	<div id="container">
		<div id="bd">
			<div class="login">
            	<div class="login-top"><!-- <h1 class="logo"></h1> --></div>
                <div class="login-input">
                	<p class="user ue-clear">
                    	<label>用户名</label>
                        <input type="text" name="loginId" id="username" />
                    </p>
                    <p class="password ue-clear">
                    	<label>密　码</label>
                        <input type="password" name="password" id="pwd" />
                    </p>
                </div>
                <div class="login-btn ue-clear" id="loginBtnWrap" >
                	<a href="javascript:void(0);"  class="btn btn-primary ladda-button"  data-style="zoom-out"  id="loginBtn" >登录</a>
                </div>
            </div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(function(){
		// Bind normal buttons
		Ladda.bind( '.ladda-button', { timeout: 2000 } );

		var height = $(window).height();
		$("#container").height(height);
		$("#bd").css("padding-top",height/2 - $("#bd").height()/2);
		
		$(window).resize(function(){
			var height = $(window).height();
			$("#bd").css("padding-top",$(window).height()/2 - $("#bd").height()/2);
			$("#container").height(height);
		});
		
		$('#remember').focus(function(){
		   $(this).blur();
		});

		$('#remember').click(function(e) {
			checkRemember($(this));
		});
	})
	function checkRemember($this){
		if(!-[1,]){
			 if($this.prop("checked")){
				$this.parent().addClass('checked');
			}else{
				$this.parent().removeClass('checked');
			}
		}
	}
</script>
</html>
