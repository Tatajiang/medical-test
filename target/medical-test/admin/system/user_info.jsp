<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../inc/defines.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<jsp:include page="../inc/resource.jsp" />
	<script src="<%=path %>/js/jquery-3.3.1.min.js"></script>
</head>
<body style="background: #f7f7f7;" class="nav-md">
	<div class="container-fluid">
		<div class="x_panel">
			<form role="form"  id="searchUserForm" >
				<div class="container">
		            <div class="row">
		                <div class="form-group col-xs-3">
		                    <div class="input-group">
		                        <span class="input-group-addon">登录名</span>
		                        <input id="likeLoginId" name="likeLoginId" class="form-control" type="text">
		                    </div>
		                </div>
		                <div class="form-group col-xs-3">
		                    <div class="input-group">
		                        <span class="input-group-addon">姓名</span>
		                        <input id="likeNickName" name="likeNickName" class="form-control" type="text">
		                    </div>
		                </div>
		                <div class="form-group col-xs-2.5">
		                        <button class="btn btn-primary" type="button" id="searchUser"><i class="fa fa-search" aria-hidden="true" style="margin-right:5px;"></i>查询</button>&nbsp;
								<button class="btn btn-default"  type="button" id="clearUser">清空</button>&nbsp;		
		                </div>
		            </div>
		        </div>
			</form>
			<div class="form-group col-lg-10">
			    <div class="input-group">
			   		 <button class="btn btn-primary"  type="button" data-toggle="modal" data-target="#addUserInfoModal"><i class="fa fa-plus-square" aria-hidden="true" style="margin-right:5px;"></i>添加</button>&nbsp;
			    </div>
			</div>
		
			<table id="userDataList" class="table table-striped jambo_table"></table>
		</div>
	</div>
	<!--新增角色模态框start-->
	<div class="modal fade" id="addUserInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
		<div class="modal-dialog">
			<div class="modal-content"> 
				<div class="modal-header"><button class="close" type="button" onclick="hideEditForm();">x</button>
	             	<h4>新增用户信息</h4>
				</div>
	            <div class="modal-body">
	             	<form class="form-horizontal" role="form" id="addForm">
	             		<input type="hidden" name="id" />
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">姓名：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="nickname" class="form-control" required data-bv-notempty-message="姓名不能为空"/>
	             			</div>
	             		</div>
						<div class="form-group">
	             			<label class="col-sm-2 control-label">登录名：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="loginId" required data-bv-notempty-message="登录名不能为空" class="form-control" />
	             			</div>
	             		</div>
						<div class="form-group">
	             			<label class="col-sm-2 control-label">性别：</label>
	             			<div class="col-sm-10">
	             				<select name="gender" class="form-control">
	             					<option value="Male">男</option>
	             					<option value="FeMale">女</option>
	             				</select>
	             			</div>
	             		</div>	             		
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">登录密码：</label>
	             			<div class="col-sm-10">
	             				<input type="password" name="password" required data-password="password" class="form-control" />
	             			</div>
	             		</div>
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">确认登录密码：</label>
	             			<div class="col-sm-10">
	             				<input type="password" name="repassword" required data-password="password" class="form-control" />
	             			</div>
	             		</div>
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">电话：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="phone" data-phone="phone" class="form-control" />
	             			</div>
	             		</div>
					</form>
             	</div>
             	<div class="modal-footer">
					<button type="button" class="btn btn-primary" onclick="save();">保存</button>
					<button type="button" class="btn btn-default" onclick="hideEditForm();">取消</button>
				</div>
			</div>
		</div>
	</div>
	<!--新增角色模态框end-->
	
	<!--修改角色模态框start-->
	<div class="modal fade" id="updateUserInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> 
		<div class="modal-dialog">
			<div class="modal-content"> 
				<div class="modal-header"><button class="close" type="button" onclick="hideEditForm();">x</button>
	             	<h4>修改用户信息</h4>
				</div>
	            <div class="modal-body">
	             	<form class="form-horizontal" role="form" id="updateForm">
	             		<input type="hidden" name="id" />
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">姓名：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="nickname" class="form-control" required data-bv-notempty-message="姓名不能为空"/>
	             			</div>
	             		</div>
						<div class="form-group">
	             			<label class="col-sm-2 control-label">性别：</label>
	             			<div class="col-sm-10">
								<select name="gender" class="form-control">
									<option value="Male">男</option>
									<option value="FeMale">女</option>
								</select>
	             			</div>
	             		</div>
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">登录名：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="loginId" required data-bv-notempty-message="登录名不能为空" class="form-control" />
	             			</div>
	             		</div>
	             		<div class="form-group">
	             			<label class="col-sm-2 control-label">电话：</label>
	             			<div class="col-sm-10">
	             				<input type="text" name="phone" data-phone="phone" class="form-control" />
	             			</div>
	             		</div>
					</form>
             	</div>
             	<div class="modal-footer">
					<button type="button" class="btn btn-primary" onclick="update();">保存</button>
					<button type="button" class="btn btn-default" onclick="hideEditForm();">取消</button>
				</div>
			</div>
		</div>
	</div>
	<!--修改角色模态框end-->
	
	<table id="commonSelectUserList"></table>
	
	<jsp:include page="../inc/bottom.jsp" />
	<script src="<%=path %>/js/bootstrap-validator/custom-validator.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			//搜索按钮
			$('#searchUser').click(function(){
				var params = $('#searchUserForm').serializeObject();
				$('#userDataList').bootstrapTable("refreshOptions", {
		    		queryParams:function(p){
		    			return serializeTableQueryParams(p, params);
		    		}
				});
			});
			
			// 清空
			$("#clearUser").click(function(){
				$('#searchUserForm').clearForm();
				var params = $('#searchUserForm').serializeObject();
				$('#userDataList').bootstrapTable("refreshOptions", {
		    		queryParams:function(p){
		    			return serializeTableQueryParams(p, params);
		    		}
				});
			})
			
			// 初始化表格
			$('#userDataList').bootstrapTable({
				toolbar:'#toolbar',
				uniqueId:'id',	// 指定主键列
				method:'post',
			    url: $ctx + '/service/rest/user.UserService/collection/query',
			    striped: true,
			    pagination: true,
			    pageNumber: 1,
	            pageSize: 15,
	            showRefresh: true,
	            showToggle: true,
	            showColumns: true,
	            cardView: false,	
	            striped: true,
	            showHeader: true,
	            sortable: true,
	            sortName: "createTime",
	            sortOrder: "desc",
	            sidePagination: "server",
	            pageList: [10, 25, 50, 100],
	          	// 得到查询的参数
	            queryParams : function (params) {
	    			return serializeTableQueryParams(params, {});
	            },
				columns : [
					{field : "loginId",title : "登录名",align:"center",width : 80},
					{field : "nickname",title : "姓名",align:"center",width : 80},
					{field : "genderName",title : "性别",align:"center",width : 50},
					{field : "phone",title : "电话",align:"center",width : 100},
					{field : "isDelete",title : "状态",align:"center",width : 50},
					{field : 'operate',title: '操作',align: 'center', width : 80,
		                formatter: function(value,row,index){
		                	var html = '';
		                	html += '<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#updateUserInfoModal" onclick="updateUser(\''+row.id+'\')"><i class="fa fa-pencil-square-o" aria-hidden="true" style="margin-right:5px;"></i>修改</button>'
		                	if(row.isDelete=="已启用"){
		                		html += '<button type="button" class="btn btn-danger" onclick="delUser(\''+row.id+'\')"><i class="fa fa-minus-square" aria-hidden="true" style="margin-right:5px;"></i>禁用</button>';
		                	}else if(row.isDelete=="已禁用"){
		                		html += '<button type="button" class="btn btn-info" onclick="useUser(\''+row.id+'\')"><i class="fa fa-plus-square" aria-hidden="true" style="margin-right:5px;"></i>启用</button>';
		                	}
		                	return html;
		                }
					},
				],
				onLoadSuccess: function () {// 数据加载成功
					
                },
                onLoadError: function () {// 数据加载失败
                	
                }
			});
		});
		
		// 保存对象
		function save(){
			// 获取表单对象
		    var bootstrapValidator = $('#addForm').data('bootstrapValidator');
		    // 手动触发验证
		    bootstrapValidator.validate();
		    if(bootstrapValidator.isValid()){
				Modal.confirm({ message: "确定保存?",icon:"fa fa-exclamation-circle"}).on(function (r) {
					if(r){
						var url = $ctx + '/service/rest/user.UserService/collection/addOrUpdate';
						var params = $('#addForm').serializeObject();
						if(params.password==params.repassword){
							$.postExtend(url, params, function(data){
								if(data.code == 1){
									Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
										hideEditForm();
										$('#userDataList').bootstrapTable('refresh');
									});
								}else{
									Modal.alert({message: data.description, icon:'fa fa-error'});
								}
							});
						}else{
							Modal.alert({message: "两次密码不一致", icon:'fa fa-error'});
						}
					}
				});
		    }
		}
		
		// 修改对象
		function update(){
			// 获取表单对象
		    var bootstrapValidator = $('#updateForm').data('bootstrapValidator');
		    // 手动触发验证
		    bootstrapValidator.validate();
		    if(bootstrapValidator.isValid()){
				Modal.confirm({ message: "确定修改?",icon:"fa fa-exclamation-circle"}).on(function (r) {
					if(r){
						var url = $ctx + '/service/rest/user.UserService/collection/addOrUpdate';
						var params = $('#updateForm').serializeObject();
						$.postExtend(url, params, function(data){
							if(data.code == 1){
								Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
									hideEditForm();
									$('#userDataList').bootstrapTable('refresh');
								});
							}else{
								Modal.alert({message: data.description, icon:'fa fa-error'});
							}
						});
					}
				});
		    }
		}
		
		// 修改
		function updateUser(id){
			var row = $('#userDataList').bootstrapTable('getRowByUniqueId', id);
			$('#updateForm').loadJson(row);
			if(row.gender == "男"){
				$('#gender').val("10");
			}
			if(row.gender == "女"){
				$('#gender').val("20");
			}
			if(row.gender == "保密"){
				$('#gender').val("30");
			}
		}
		
		function hideEditForm(){
			hideModal('addUserInfoModal');
			$('#addForm')[0].reset();
			hideModal('updateUserInfoModal');
			$('#updateForm')[0].reset();
		}
		
		function showModal(id){
			$('#' + id).modal('show');
		}
		
		function hideModal(id){
			$('#' + id).modal('hide');
		}

		// 禁用
		function delUser(id){
			Modal.confirm({ message: '确定禁用该用户？',icon:"fa fa-exclamation-circle"}).on(function (r) {
				if(r){
					var url = $ctx + '/service/rest/user.UserService/collection/delete';
					$.postExtend(url, {id:id}, function(data){
						if(data.code == 1){
							Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
								$('#userDataList').bootstrapTable('refresh');
							});
						}else{
							Modal.alert({message: data.description, icon:'fa fa-error'});
						}
					});
				}
			});
		}
		
		// 启用
		function useUser(id){
			Modal.confirm({ message: '确定启用该用户？',icon:"fa fa-exclamation-circle"}).on(function (r) {
				if(r){
					var url = $ctx + '/service/rest/user.UserService/collection/use';
					$.postExtend(url, {id:id}, function(data){
						if(data.code == 1){
							Modal.alert({message: data.description, icon:'fa fa-check'}).on(function(){
								$('#userDataList').bootstrapTable('refresh');
							});
						}else{
							Modal.alert({message: data.description, icon:'fa fa-error'});
						}
					});
				}
			});
		}
	</script>
</body>
</html>