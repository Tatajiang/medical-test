<%@page import="com.jiang.medical.platform.system.domain.User"%>
<%@page import="com.jiang.medical.util.SessionUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="top_nav">
	<div class="nav_menu">
		<nav>
			<div class="nav toggle">
				<a id="menu_toggle"><i class="fa fa-bars"></i></a>
			</div>
			<ul class="nav navbar-nav navbar-right">
				<li class="">
					<a href="javascript:void(0);" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false"> 
						<i class="fa fa-snapchat" style="padding-right: 5px;line-height: 5px;font-size: 20px;vertical-align: middle;"></i><span class=" fa fa-angle-down" style="margin-left: 10px;"></span>
					</a>
					<ul class="dropdown-menu dropdown-usermenu pull-right">
						<li><a href="javascript:void(0);"  data-toggle="modal" data-target="#changePasswordModal"><i class="fa fa-key pull-right"></i>修改密码</a></li>
						<li><a href="javascript:void(0);" onclick="Modal.confirm({ message: '确定退出？',icon:'fa fa-exclamation-circle'}).on(function(r){if(r){window.location.href = $ctx + '/Logout';}});"><i class="fa fa-sign-out pull-right"></i> 安全退出</a></li>
					</ul>
				</li>
			</ul>
		</nav>
	</div>
</div>

<!--修改密码start-->
<div class="modal fade" id="changePasswordModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
		<div class="modal-dialog">
			<div class="modal-content"> 
				<div class="modal-header"><button class="close" type="button" onclick="hideForm();">x</button>
	             	<h4>修改密码</h4>
				</div>
	            <div class="modal-body">
	            	<form class="form-horizontal" role="form" id="changePasswordForm">
	             		<input type="hidden" name="id" />
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">当前密码：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="oldPass" class="form-control" />
	             			</div>
	             		</div>	             		
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">新密码：</label>
	             			<div class="col-sm-10">
	             				<input type="password" name="newPassOne" class="form-control" />
	             			</div>
	             		</div>
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">再次输入：</label>
	             			<div class="col-sm-10">
	             				<input type="password" name="newPassTwo" class="form-control" />
	             			</div>
	             		</div>
					</form>
             	</div>
             	<div class="modal-footer">
					<button type="button" class="btn btn-primary" onclick="save()">保存</button>
					<button type="button" class="btn btn-default" onclick="hideForm();">取消</button>
				</div>
			</div>
		</div>
</div>
<!--修改密码end-->

<script type="text/javascript">
		//保存
		function save(){
			var url = $ctx + '/service/rest/user.UserService/collection/updatePw';
			var params = $('#changePasswordForm').serializeObject();
			$.postExtend(url, params, function(data){
				if(data.code == 1){
					Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
						window.location.href = $ctx + '/Logout';
					});
				}else{
					Modal.alert({message: data.description, icon:'fa fa-error'});
				}
			});
		}
		
		function hideForm(){
			hideModal('changePasswordModal');
			$('#changePasswordForm')[0].reset();
		}

		function hideModal(id){
			$('#' + id).modal('hide');
		}
		
</script>